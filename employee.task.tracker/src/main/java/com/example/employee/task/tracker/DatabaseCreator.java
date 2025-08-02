package com.example.employee.task.tracker;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DatabaseCreator implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext context) {
        Environment env = context.getEnvironment();

        // Read from application.properties or environment variables
        String dbName = env.getProperty("datasource.db_name");
        String adminUrl = env.getProperty("datasource.admin.url" );
        String username = env.getProperty("spring.datasource.username");
        String password = env.getProperty("spring.datasource.password");

        try {
            assert adminUrl != null;
            try (Connection conn = DriverManager.getConnection(adminUrl, username, password);
                     Statement stmt = conn.createStatement()) {

                ResultSet rs = stmt.executeQuery("SELECT 1 FROM pg_database WHERE datname = '" + dbName + "'");
                if (!rs.next()) {
                    stmt.executeUpdate("CREATE DATABASE " + dbName);
                    System.out.println("✅ Created database: " + dbName);
                } else {
                    System.out.println("ℹ️ Database already exists: " + dbName);
                }

            }
        } catch (Exception e) {
            throw new RuntimeException("❌ Could not create database: " + dbName, e);
        }
    }
}
