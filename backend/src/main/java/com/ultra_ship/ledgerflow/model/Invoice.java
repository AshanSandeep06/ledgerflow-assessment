package com.ultra_ship.ledgerflow.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * Title: Invoice
 * Description: Invoice Table
 * Created by Ashan Sandeep on 7/10/2026
 * Email: ashansandeep06@gmail.com
 * Java Version: 21
 */
@Entity
@Data
public class Invoice {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private Instant dueDate;
    private String status = "DRAFT"; // DRAFT, SENT, PAID, OVERDUE
    private Integer totalInCents;
    private Integer amountPaid = 0;

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL)
    private List<LineItem> lineItems;

    @Version
    private Integer version; // Optimistic locking for concurrency edge case
}
