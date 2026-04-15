package com.sms.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConfig {
    private static final String URL = "jdbc:mysql://erp-vigneshwaran-6b62.k.aivencloud.com:16990/sms_console";
    private static final String USER = "avnadmin";
    private static final String PASS = "AVNS_pglAGGn58sK9XJ7RYqD";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}