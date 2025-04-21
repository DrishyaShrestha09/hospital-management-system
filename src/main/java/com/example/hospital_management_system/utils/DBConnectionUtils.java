package com.example.hospital_management_system.utils;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class DBConnectionUtils {
    private static String url;
    private static String username;
    private static String password;
    private static String driverClass;

    static {
        try {
            Properties props = new Properties();
            InputStream in = DBConnectionUtils.class.getClassLoader().getResourceAsStream("db.properties");

            if (in == null) {
                throw new RuntimeException("Failed to load database configuration: db.properties not found.");
            }

            props.load(in);

            url = props.getProperty("db.url");
            username = props.getProperty("db.username");
            password = props.getProperty("db.password");
            driverClass = props.getProperty("db.driverClass");

            Class.forName(driverClass);

        } catch (Exception e) {
            throw new RuntimeException("Failed to load database configuration", e);
        }
    }

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            throw new RuntimeException("Database connection failed", e);
        }
    }
}
