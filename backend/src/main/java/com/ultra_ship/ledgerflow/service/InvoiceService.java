package com.ultra_ship.ledgerflow.service;

import com.ultra_ship.ledgerflow.model.Account;
import com.ultra_ship.ledgerflow.model.Invoice;
import com.ultra_ship.ledgerflow.repository.AccountRepository;
import com.ultra_ship.ledgerflow.repository.InvoiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InvoiceService {
    private final InvoiceRepository invoiceRepo;
    private final LedgerService ledgerService;
    private final AccountRepository accountRepo;

    @Transactional
    public Invoice applyPayment(UUID invoiceId, Integer amountInCents, String webhookId) {

        if (ledgerService.isTransactionProcessed(webhookId)) {
            return invoiceRepo.findById(invoiceId)
                    .orElseThrow(() -> new RuntimeException("Invoice not found"));
        }

        Invoice invoice = invoiceRepo.findById(invoiceId)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));

        if ("PAID".equals(invoice.getStatus())) {
            throw new IllegalStateException("Already fully paid");
        }

        int newPaid = invoice.getAmountPaid() + amountInCents;

        if (newPaid > invoice.getTotalInCents()) {
            throw new IllegalArgumentException("Cannot overpay invoice");
        }

        invoice.setAmountPaid(newPaid);
        invoice.setStatus(newPaid == invoice.getTotalInCents() ? "PAID" : "SENT");
        invoice = invoiceRepo.save(invoice);

        // --> FIX: Fetch the real UUIDs dynamically from the database by name
        Account cashAccount = accountRepo.findByName("Operating Cash")
                .orElseThrow(() -> new RuntimeException("Setup Error: Operating Cash account missing"));
        Account arAccount = accountRepo.findByName("Accounts Receivable")
                .orElseThrow(() -> new RuntimeException("Setup Error: Accounts Receivable account missing"));

        // Use the dynamically fetched IDs
        ledgerService.recordTransaction(cashAccount.getId(), arAccount.getId(), amountInCents, webhookId);

        return invoice;
    }
}
