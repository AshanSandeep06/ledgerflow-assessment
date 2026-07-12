package com.ultra_ship.ledgerflow.service;

import com.ultra_ship.ledgerflow.repository.AccountRepository;
import com.ultra_ship.ledgerflow.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * Title: LedgerServiceTest
 * Description: LedgerServiceTest test class
 * Created by Ashan Sandeep on 7/10/2026
 * Email: ashansandeep06@gmail.com
 * Java Version: 21
 */

@ExtendWith(MockitoExtension.class)
class LedgerServiceTest {
    @Mock
    private TransactionRepository transactionRepo;
    @Mock
    private AccountRepository accountRepo;
    @InjectMocks
    private LedgerService ledgerService;

    @Test
    void getAssetAccountBalance_isDerivableAndCalculatesCorrectly() {
        UUID accountId = UUID.randomUUID();
        when(transactionRepo.getSumOfDebits(accountId)).thenReturn(1000); // 10.00
        when(transactionRepo.getSumOfCredits(accountId)).thenReturn(250); // 2.50

        // For an Asset account: Balance = Debits - Credits
        Integer balance = ledgerService.getAssetAccountBalance(accountId);
        assertEquals(750, balance); // Balance dynamically derived, not stored
    }

    @Test
    void recordTransaction_rejectsNegativeAmounts() {
        UUID debit = UUID.randomUUID();
        UUID credit = UUID.randomUUID();

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            ledgerService.recordTransaction(debit, credit, -500, "ref123");
        });
        assertTrue(thrown.getMessage().contains("positive"));
    }
}
