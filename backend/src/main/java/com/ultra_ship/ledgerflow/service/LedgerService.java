package com.ultra_ship.ledgerflow.service;

import com.ultra_ship.ledgerflow.model.Transaction;
import com.ultra_ship.ledgerflow.repository.AccountRepository;
import com.ultra_ship.ledgerflow.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Title: LedgerService
 * Description:
 * Created by Ashan Sandeep on 7/10/2026
 * Email: ashansandeep06@gmail.com
 * Java Version: 21
 */

@Service
@RequiredArgsConstructor
public class LedgerService {

    private final TransactionRepository transactionRepo;
    private final AccountRepository accountRepo;

    @Transactional
    public Transaction recordTransaction(UUID debitId, UUID creditId, Integer amountInCents, String refId) {
        if (amountInCents <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }

        // Failsafe: Throw an exception rather than returning silently to ensure DB rollback
        if (isTransactionProcessed(refId)) {
            throw new IllegalStateException("Transaction with this reference ID already exists");
        }

        Transaction tx = new Transaction();
        tx.setDebitAccount(accountRepo.findById(debitId)
                .orElseThrow(() -> new IllegalArgumentException("Debit account not found")));
        tx.setCreditAccount(accountRepo.findById(creditId)
                .orElseThrow(() -> new IllegalArgumentException("Credit account not found")));
        tx.setAmountInCents(amountInCents);
        tx.setReferenceId(refId);

        return transactionRepo.save(tx);
    }

    public Integer getAssetAccountBalance(UUID accountId) {
        Integer debits = transactionRepo.getSumOfDebits(accountId);
        Integer credits = transactionRepo.getSumOfCredits(accountId);

        // Null checks in case the COALESCE query fails or returns null
        return (debits != null ? debits : 0) - (credits != null ? credits : 0);
    }

    // Helper method to allow InvoiceService to check idempotency early
    public boolean isTransactionProcessed(String refId) {
        return refId != null && transactionRepo.existsByReferenceId(refId);
    }
}
