package com.ultra_ship.ledgerflow.config;

/**
 * Title: DataSeeder
 * Description: DataSeeder class
 * Created by Ashan Sandeep on 7/10/2026
 * Email: ashansandeep06@gmail.com
 * Java Version: 21
 */

import com.ultra_ship.ledgerflow.model.Account;
import com.ultra_ship.ledgerflow.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {
    private final AccountRepository accountRepository;

    @Override
    public void run(String... args) {
        // Check by NAME instead of hardcoded UUID
        if (accountRepository.findByName("Operating Cash").isEmpty()) {
            Account cash = new Account();
            cash.setName("Operating Cash");
            cash.setType("ASSET");
            accountRepository.save(cash);
            System.out.println("Created Operating Cash account");
        }

        if (accountRepository.findByName("Accounts Receivable").isEmpty()) {
            Account ar = new Account();
            ar.setName("Accounts Receivable");
            ar.setType("ASSET");
            accountRepository.save(ar);
            System.out.println("Created Accounts Receivable account");
        }
    }
}