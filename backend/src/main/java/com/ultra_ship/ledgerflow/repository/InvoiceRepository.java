package com.ultra_ship.ledgerflow.repository;

import com.ultra_ship.ledgerflow.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Title: InvoiceRepository
 * Description:
 * Created by Ashan Sandeep on 7/10/2026
 * Email: ashansandeep06@gmail.com
 * Java Version: 21
 */
@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, UUID> {

}
