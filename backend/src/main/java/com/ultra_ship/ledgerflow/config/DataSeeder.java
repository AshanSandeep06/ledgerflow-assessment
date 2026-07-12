package com.ultra_ship.ledgerflow.config;

/**
 * Title: DataSeeder
 * Description: DataSeeder class
 * Created by Ashan Sandeep on 7/10/2026
 * Email: ashansandeep06@gmail.com
 * Java Version: 21
 */

import com.ultra_ship.ledgerflow.model.Account;
import com.ultra_ship.ledgerflow.model.Invoice;
import com.ultra_ship.ledgerflow.model.LineItem;
import com.ultra_ship.ledgerflow.repository.AccountRepository;
import com.ultra_ship.ledgerflow.repository.InvoiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final AccountRepository accountRepository;
    private final InvoiceRepository invoiceRepository; // Injected for invoices

    @Override
    public void run(String... args) {

        // 1. Seed Accounts
        if (accountRepository.findByName("Operating Cash").isEmpty()) {
            Account cash = new Account();
            cash.setName("Operating Cash");
            cash.setType("ASSET");
            accountRepository.save(cash);
            System.out.println("✅ Created Operating Cash account");
        }

        if (accountRepository.findByName("Accounts Receivable").isEmpty()) {
            Account ar = new Account();
            ar.setName("Accounts Receivable");
            ar.setType("ASSET");
            accountRepository.save(ar);
            System.out.println("✅ Created Accounts Receivable account");
        }

        // 2. Seed Sample Invoices
        if (invoiceRepository.count() == 0) {
            System.out.println("🌱 Seeding sample invoices...");

            // Invoice 1 (Unpaid)
            Invoice invoice1 = new Invoice();
            invoice1.setStatus("SENT");
            invoice1.setTotalInCents(150000); // $1,500.00
            invoice1.setAmountPaid(0);
            invoice1.setDueDate(Instant.now().plus(30, ChronoUnit.DAYS));

            LineItem item1 = new LineItem();
            item1.setDescription("Freight Shipping (NY to LA)");
            item1.setAmountInCents(120000);
            item1.setInvoice(invoice1);

            LineItem item2 = new LineItem();
            item2.setDescription("Fuel Surcharge");
            item2.setAmountInCents(30000);
            item2.setInvoice(invoice1);

            invoice1.setLineItems(List.of(item1, item2));
            invoiceRepository.save(invoice1);

            // Invoice 2 (Partially Paid)
            Invoice invoice2 = new Invoice();
            invoice2.setStatus("SENT");
            invoice2.setTotalInCents(45000); // $450.00
            invoice2.setAmountPaid(10000);   // $100.00 paid
            invoice2.setDueDate(Instant.now().plus(15, ChronoUnit.DAYS));

            LineItem item3 = new LineItem();
            item3.setDescription("Warehouse Storage (May)");
            item3.setAmountInCents(45000);
            item3.setInvoice(invoice2);

            invoice2.setLineItems(List.of(item3));
            invoiceRepository.save(invoice2);

            System.out.println("✅ Sample invoices created");
        }
    }
}