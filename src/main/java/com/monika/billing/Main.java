package com.monika.billing;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

import com.monika.billing.dao.CustomerDAO;
import com.monika.billing.dao.ProductDAO;
import com.monika.billing.dao.ReportDAO;
import com.monika.billing.model.Product;
import com.monika.billing.service.BillService;

public class Main {
    private static final Scanner in = new Scanner(System.in);

    public static void main(String[] args) {
        ProductDAO products = new ProductDAO();
        CustomerDAO customers = new CustomerDAO();
        BillService billService = new BillService();
        ReportDAO reports = new ReportDAO();

        boolean running = true;
        while (running) {
            System.out.println("\n===== Billing System =====");
            System.out.println("1. Add product");
            System.out.println("2. View products");
            System.out.println("3. Create bill");
            System.out.println("4. Recent bills");
            System.out.println("5. Daily sales report");
            System.out.println("6. Low stock report");
            System.out.println("7. Top selling products");
            System.out.println("0. Exit");
            System.out.print("Choose: ");
            String choice = in.nextLine().trim();

            try {
                switch (choice) {
                    case "1":
                        System.out.print("Name: ");
                        String name = in.nextLine().trim();
                        BigDecimal price = new BigDecimal(ask("Price: "));
                        int stock = Integer.parseInt(ask("Stock: "));
                        products.add(name, price, stock);
                        System.out.println("Product added.");
                        break;
                    case "2":
                        for (Product p : products.listAll()) System.out.println(p);
                        break;
                    case "3":
                        createBill(customers, billService);
                        break;
                    case "4":
                        reports.recentBills();
                        break;
                    case "5":
                        reports.dailySales();
                        break;
                    case "6":
                        reports.lowStock();
                        break;
                    case "7":
                        reports.topSelling();
                        break;
                    case "0":
                        running = false;
                        System.out.println("Bye!");
                        break;
                    default:
                        System.out.println("Invalid option.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private static void createBill(CustomerDAO customers, BillService billService) throws Exception {
        String name = ask("Customer name: ");
        String phone = ask("Phone: ");
        int customerId = customers.getOrCreate(name, phone);

        Map<Integer, Integer> items = new LinkedHashMap<>();
        while (true) {
            String pid = ask("Product id (blank to finish): ");
            if (pid.isEmpty()) break;
            int qty = Integer.parseInt(ask("Quantity: "));
            items.merge(Integer.parseInt(pid), qty, Integer::sum);
        }
        if (items.isEmpty()) {
            System.out.println("No items. Bill cancelled.");
            return;
        }
        int billId = billService.createBill(customerId, items);
        System.out.println("Bill created: #" + billId);
    }

    private static String ask(String label) {
        System.out.print(label);
        return in.nextLine().trim();
    }
}