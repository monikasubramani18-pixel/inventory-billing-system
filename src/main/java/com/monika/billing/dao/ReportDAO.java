package com.monika.billing.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import com.monika.billing.util.DBConnection;

public class ReportDAO {

    public void recentBills() throws SQLException {
        print("SELECT b.id AS bill_id, c.name AS customer, b.bill_date, b.total "
            + "FROM bills b LEFT JOIN customers c ON c.id = b.customer_id "
            + "ORDER BY b.id DESC LIMIT 10");
    }

    public void dailySales() throws SQLException {
        print("SELECT DATE(bill_date) AS day, COUNT(*) AS bills, SUM(total) AS sales "
            + "FROM bills GROUP BY DATE(bill_date) ORDER BY day DESC");
    }

    public void lowStock() throws SQLException {
        print("SELECT id, name, stock FROM products WHERE stock < 10 ORDER BY stock");
    }

    public void topSelling() throws SQLException {
        print("SELECT p.name, SUM(bi.quantity) AS sold "
            + "FROM bill_items bi JOIN products p ON p.id = bi.product_id "
            + "GROUP BY p.name ORDER BY sold DESC LIMIT 5");
    }

    private void print(String sql) throws SQLException {
        try (Connection c = DBConnection.get();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            ResultSetMetaData md = rs.getMetaData();
            int n = md.getColumnCount();
            for (int i = 1; i <= n; i++) System.out.print(md.getColumnLabel(i) + "\t");
            System.out.println();
            boolean any = false;
            while (rs.next()) {
                any = true;
                for (int i = 1; i <= n; i++) System.out.print(rs.getString(i) + "\t");
                System.out.println();
            }
            if (!any) System.out.println("(no data)");
        }
    }
}