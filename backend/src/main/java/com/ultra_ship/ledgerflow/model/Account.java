package com.ultra_ship.ledgerflow.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

/**
 * Title: Account
 * Description: Account Table
 * Created by Ashan Sandeep on 7/10/2026
 * Email: ashansandeep06@gmail.com
 * Java Version: 21
 */

@Entity
@Data
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String name;
    private String type; // ASSET, LIABILITY, REVENUE, EXPENSE
}
