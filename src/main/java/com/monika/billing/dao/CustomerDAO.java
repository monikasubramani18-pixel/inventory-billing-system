package com.monika.billing.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.monika.billing.util.DBConnection;

public class CustomerDAO {

    // phone already irundha andha customer id, illana puthusa create pannum
    public int getOrCreate(String name, String phone) throws SQLException {
        try (Connection c = DBConnection.get()) {
            try (PreparedStatement ps = c.prepareStatement(
                    "SELECT id FROM customers WHERE phone = ?")) {
                ps.setString(1, phone);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) return rs.getInt("id");
            }
            try (PreparedStatement ps = c.prepareStatement(
                    "INSERT INTO customers (name, phone) VALUES (?, ?) RETURNING id")) {
                ps.setString(1, name);
                ps.setString(2, phone);
                ResultSet rs = ps.executeQuery();
                rs.next();
                return rs.getInt(1);
            }
        }
    }
}