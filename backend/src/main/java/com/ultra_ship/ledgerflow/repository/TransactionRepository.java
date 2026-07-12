package com.ultra_ship.ledgerflow.repository;

import com.ultra_ship.ledgerflow.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Title: TransactionRepository
 * Description:
 * Created by Ashan Sandeep on 7/10/2026
 * Email: ashansandeep06@gmail.com
 * Java Version: 21
 */
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    boolean existsByReferenceId(String referenceId);

    @Query("SELECT COALESCE(SUM(t.amountInCents), 0) FROM Transaction t WHERE t.creditAccount.id = :accountId")
    Integer getSumOfCredits(UUID accountId);

    @Query("SELECT COALESCE(SUM(t.amountInCents), 0) FROM Transaction t WHERE t.debitAccount.id = :accountId")
    Integer getSumOfDebits(UUID accountId);
}
