package com.monika.billing.service;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import com.monika.billing.util.DBConnection;

public class BillService {

    // items: productId -> quantity
    public int createBill(int customerId, Map<Integer, Integer> items) throws SQLException {
        try (Connection c = DBConnection.get()) {
            c.setAutoCommit(false);
            try {
                int billId;
                try (PreparedStatement ps = c.prepareStatement(
                        "INSERT INTO bills (customer_id) VALUES (?) RETURNING id")) {
                    ps.setInt(1, customerId);
                    ResultSet rs = ps.executeQuery();
                    rs.next();
                    billId = rs.getInt(1);
                }

                BigDecimal total = BigDecimal.ZERO;
                for (Map.Entry<Integer, Integer> e : items.entrySet()) {
                    int productId = e.getKey();
                    int qty = e.getValue();

                    BigDecimal price;
                    int stock;
                    try (PreparedStatement ps = c.prepareStatement(
                            "SELECT price, stock FROM products WHERE id = ? FOR UPDATE")) {
                        ps.setInt(1, productId);
                        ResultSet rs = ps.executeQuery();
                        if (!rs.next()) {
                            throw new IllegalArgumentException("Product not found: " + productId);
                        }
                        price = rs.getBigDecimal("price");
                        stock = rs.getInt("stock");
                    }
                    if (stock < qty) {
                        throw new IllegalStateException(
                                "Not enough stock for product " + productId + " (available: " + stock + ")");
                    }

                    try (PreparedStatement ps = c.prepareStatement(
                            "UPDATE products SET stock = stock - ? WHERE id = ?")) {
                        ps.setInt(1, qty);
                        ps.setInt(2, productId);
                        ps.executeUpdate();
                    }
                    try (PreparedStatement ps = c.prepareStatement(
                            "INSERT INTO bill_items (bill_id, product_id, quantity, price) VALUES (?, ?, ?, ?)")) {
                        ps.setInt(1, billId);
                        ps.setInt(2, productId);
                        ps.setInt(3, qty);
                        ps.setBigDecimal(4, price);
                        ps.executeUpdate();
                    }
                    total = total.add(price.multiply(BigDecimal.valueOf(qty)));
                }

                try (PreparedStatement ps = c.prepareStatement(
                        "UPDATE bills SET total = ? WHERE id = ?")) {
                    ps.setBigDecimal(1, total);
                    ps.setInt(2, billId);
                    ps.executeUpdate();
                }
                c.commit();
                return billId;
            } catch (Exception ex) {
                c.rollback();
                throw ex;
            }
        }
    }
}