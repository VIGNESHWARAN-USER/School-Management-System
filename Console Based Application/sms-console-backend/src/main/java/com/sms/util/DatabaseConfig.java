package com.sms.util;

// Author: Vigneshwaran M
/*
 * This class is used to create database connection for the entire application
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConfig {

    // Database URL, Username and Password
    private static final String URL = "jdbc:mysql://erp-vigneshwaran-6b62.k.aivencloud.com:16990/sms_console";
    private static final String USER = "avnadmin";
    private static final String PASS = "AVNS_pglAGGn58sK9XJ7RYqD";

    // Returning database connection object
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}