# Mini Payment Ledger & Invoice Service

## Overview
A mini internal payment ledger and invoice tracking system built with Spring Boot (Java), MySQL, and React. 

## Requirements Handled
* **Core Ledger:** Double-entry accounting is strictly enforced. Account balances are never stored as mutable properties; they are dynamically derived by summing credits and debits via SQL.
* **Money:** All monetary amounts are stored as integers representing cents to prevent floating-point inaccuracies.
* **Invoice Flow:** Invoices support Line Items, partial payments, and status lifecycles (`DRAFT` → `SENT` → `PAID`).
* **Double-Payment Prevention:** Idempotency is enforced using a unique constraint on `referenceId` (webhook ID) in the `Transaction` table. 
* **Edge Case (Concurrency):** Addressed the race condition of concurrent payments hitting the same invoice simultaneously. Used JPA `@Version` to implement **Optimistic Locking**. If two threads attempt to apply a payment simultaneously, one succeeds and the other throws an `ObjectOptimisticLockingFailureException`.

## How to Run It Locally
1. **Database:** Ensure MySQL is running locally on port 3306. Create a schema called `ledgerdb`. Update `application.properties` with your MySQL username and password.
2. **Backend:** Navigate to `/backend`. Run `mvn spring-boot:run`. The server will start on `localhost:8080` and Hibernate will automatically create the tables.
3. **Frontend:** Navigate to `/frontend`. Run `npm install`, then `npm run dev`. Open `http://localhost:5173` in your browser.

*Note: Since there is no POST endpoint implemented to seed invoices, you will need to manually insert an Account and an Invoice into the MySQL database to test the UI.*

## Shortcuts Taken
* Seed data logic was omitted. Real systems would have endpoints to generate the initial Chart of Accounts and Invoices.
* Hardcoded UUIDs were used for the "Cash" and "Accounts Receivable" accounts during the payment application flow.
* No global exception handler (`@ControllerAdvice`) was built to translate JPA exceptions into clean 409 Conflict JSON responses. 

## What I'd Do With More Time
* Implement the GraphQL layer (as mentioned in the preferred stack) using `spring-boot-starter-graphql`.
* Build out a refund flow that reverses ledger entries (debiting Revenue/Liability and crediting Cash).
* Add Docker Compose to orchestrate the Spring Boot App, React App, and MySQL database with a single `docker-compose up` command.
