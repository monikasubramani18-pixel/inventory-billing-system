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
