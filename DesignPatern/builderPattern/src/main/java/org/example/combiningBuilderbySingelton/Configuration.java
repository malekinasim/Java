package org.example.combiningBuilderbySingelton;

import java.util.logging.Handler;

public class Configuration {
    private final String dbUrl;
    private final String dbUser;
    private final String dbPassword;
    private final String dbName;
    private final String dbSchema;
    private final String tableName;
    private final String tableSchema;
    private final int maxConnections;

    public static Configuration getInstance() {
        if (ConfigurationHolder.INSTANCE == null) {
            throw new IllegalStateException("Configuration not built yet. Call build() first.");
        } else {
            return ConfigurationHolder.INSTANCE;
        }
    }

    public static void build(ConfigurationBuilder configurationBuilder) {
        if (ConfigurationHolder.INSTANCE == null) {
            synchronized (Configuration.class) {
                if (ConfigurationHolder.INSTANCE == null) {
                    ConfigurationHolder.INSTANCE = configurationBuilder.build();
                } else {
                    throw new IllegalStateException("Configuration  built before. ");
                }
            }
        } else {
            throw new IllegalStateException("Configuration  built before. ");
        }
    }

    private Configuration(ConfigurationBuilder conf) {
        this.dbUrl = conf.dbUrl;
        this.dbUser = conf.dbUser;
        this.dbPassword = conf.dbPassword;
        this.dbName = conf.dbName;
        this.dbSchema = conf.dbSchema;
        this.tableName = conf.tableName;
        this.tableSchema = conf.tableSchema;
        this.maxConnections = conf.maxConnections;

    }

    private static class ConfigurationHolder {
        private static volatile Configuration INSTANCE;
        private static ConfigurationBuilder BUILDER_INSTANCE;
    }

    public static class ConfigurationBuilder {
        protected String dbUrl;
        protected String dbUser;
        protected String dbPassword;
        protected String dbName;
        protected String dbSchema;
        protected String tableName;
        protected String tableSchema;
        protected int maxConnections;

        public ConfigurationBuilder() {

        }

        public ConfigurationBuilder setDbUrl(String dbUrl) {
            this.dbUrl = dbUrl;
            return this;
        }

        public ConfigurationBuilder setDbUser(String dbUser) {
            this.dbUser = dbUser;
            return this;
        }

        public ConfigurationBuilder setDbPassword(String dbPassword) {
            this.dbPassword = dbPassword;
            return this;
        }

        public ConfigurationBuilder setDbName(String dbName) {
            this.dbName = dbName;
            return this;
        }

        public ConfigurationBuilder setDbSchema(String dbSchema) {
            this.dbSchema = dbSchema;
            return this;
        }

        public ConfigurationBuilder setTableName(String tableName) {
            this.tableName = tableName;
            return this;
        }

        public ConfigurationBuilder setTableSchema(String tableSchema) {
            this.tableSchema = tableSchema;
            return this;
        }

        public ConfigurationBuilder setMaxConnections(int maxConnections) {
            this.maxConnections = maxConnections;
            return this;
        }

        private Configuration build() {
            return new Configuration(this);
        }
    }

    public String getDbUrl() {
        return dbUrl;
    }

    public String getDbUser() {
        return dbUser;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public String getDbName() {
        return dbName;
    }

    public String getDbSchema() {
        return dbSchema;
    }

    public String getTableName() {
        return tableName;
    }

    public String getTableSchema() {
        return tableSchema;
    }

    public int getMaxConnections() {
        return maxConnections;
    }
}
