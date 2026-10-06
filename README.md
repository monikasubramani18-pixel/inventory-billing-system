# Inventory & Billing System

A console-based billing system built with Java, JDBC and PostgreSQL.

## Features
- Add and view products
- Create bills with multiple items; stock updates automatically
- Transaction-safe billing: if any item fails (for example, not enough stock), the whole bill is rolled back
- Row locking (SELECT ... FOR UPDATE) to prevent stock going negative
- Reports: recent bills, daily sales, low stock, top-selling products

## Tech stack
Java 17, Maven, JDBC, PostgreSQL

## Database
Tables: products, customers, bills, bill_items (see schema.sql)

## How to run
1. Create a database named `billing_db` and run `schema.sql`.
2. Set environment variables (PowerShell):
   $env:DB_USER="postgres"
   $env:DB_PASS="your-password"
3. Run: mvn compile exec:java

## Screenshots
(Add menu, bill creation and report screenshots here)
<img width="1365" height="717" alt="Screenshot 2026-10-06 161107" src="https://github.com/user-attachments/assets/7a4db5f1-a816-4885-bd9e-0b93a1eaac11" />
<img width="1366" height="768" alt="Screenshot 2026-10-06 160930" src="https://github.com/user-attachments/assets/3eb822bb-3268-44ff-b1ac-4c78447a03ad" />
<img width="1366" height="768" alt="Screenshot 2026-10-06 160736" src="https://github.com/user-attachments/assets/48a279dc-af1c-4781-9639-8b1029c95aaa" />
<img width="1366" height="768" alt="Screenshot 2026-10-06 160754" src="https://github.com/user-attachments/assets/380ef5c8-3b13-4f2b-8b57-d24ddb9f414e" />



