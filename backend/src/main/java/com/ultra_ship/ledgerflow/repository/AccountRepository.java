package com.ultra_ship.ledgerflow.repository;

import com.ultra_ship.ledgerflow.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Title: AccountRepository
 * Description: AccountRepository jpa class
 * Created by Ashan Sandeep on 7/10/2026
 * Email: ashansandeep06@gmail.com
 * Java Version: 21
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, UUID> {
    Optional<Account> findByName(String name);
}
