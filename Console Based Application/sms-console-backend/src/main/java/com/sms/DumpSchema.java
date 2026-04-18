package com.sms;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.DatabaseMetaData;

public class DumpSchema {
    public static void main(String[] args) {
        String url = "jdbc:mysql://erp-vigneshwaran-6b62.k.aivencloud.com:16990/sms_console";
        String user = "avnadmin";
        String pass = "AVNS_pglAGGn58sK9XJ7RYqD";

        try (Connection con = DriverManager.getConnection(url, user, pass)) {
            DatabaseMetaData metaData = con.getMetaData();
            ResultSet tables = metaData.getTables(null, null, "%", new String[] { "TABLE" });

            while (tables.next()) {
                String tableName = tables.getString("TABLE_NAME");
                System.out.println("---- Table: " + tableName + " ----");
                
                try (Statement stmt = con.createStatement();
                     ResultSet rs = stmt.executeQuery("SHOW CREATE TABLE " + tableName)) {
                    if (rs.next()) {
                        System.out.println(rs.getString(2) + ";\n");
                    }
                } catch (Exception inner) {
                    System.out.println("-- Error describing " + tableName + ": " + inner.getMessage());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
