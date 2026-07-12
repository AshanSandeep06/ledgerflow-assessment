package com.ultra_ship.ledgerflow.contoller;

/**
 * Title: InvoiceController
 * Description: InvoiceController class
 * Created by Ashan Sandeep on 7/10/2026
 * Email: ashansandeep06@gmail.com
 * Java Version: 21
 */

import com.ultra_ship.ledgerflow.model.Invoice;
import com.ultra_ship.ledgerflow.repository.InvoiceRepository;
import com.ultra_ship.ledgerflow.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/invoices")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // Allows React local testing
public class InvoiceController {
    private final InvoiceService invoiceService;
    private final InvoiceRepository invoiceRepo;

    @GetMapping
    public List<Invoice> getAllInvoices() {
        return invoiceRepo.findAll();
    }

    @PostMapping("/{id}/pay")
    public Invoice payInvoice(@PathVariable UUID id, @RequestParam Integer amountInCents, @RequestParam String webhookId) {
        return invoiceService.applyPayment(id, amountInCents, webhookId);
    }
}
