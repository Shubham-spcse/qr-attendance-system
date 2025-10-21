package com.attendance.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * DatabaseConnection - Manages database connections
 * Supports both local development and Railway deployment
 */
public class DatabaseConnection {

    private static String DB_URL;
    private static String DB_USERNAME;
    private static String DB_PASSWORD;
    private static String DB_DRIVER;

    // Connection pool settings
    private static final int MAX_POOL_SIZE = 20;
    private static int activeConnections = 0;

    static {
        loadDatabaseProperties();
    }

    /**
     * Load database configuration from database.properties file
     * Supports Railway environment variables override
     */
    private static void loadDatabaseProperties() {
        Properties props = new Properties();
        InputStream input = null;

        try {
            // Load properties file from classpath
            input = DatabaseConnection.class.getClassLoader()
                    .getResourceAsStream("database.properties");

            if (input == null) {
                System.err.println("Warning: database.properties not found, using environment variables");
                props = new Properties();
            } else {
                props.load(input);
            }

            // Check for Railway environment variables first
            String railwayMysqlUrl = System.getenv("MYSQL_URL");
            String railwayHost = System.getenv("MYSQL_HOST");
            String railwayPort = System.getenv("MYSQL_PORT");
            String railwayDatabase = System.getenv("MYSQL_DATABASE");
            String railwayUser = System.getenv("MYSQL_USER");
            String railwayPassword = System.getenv("MYSQL_PASSWORD");

            // Load driver
            DB_DRIVER = props.getProperty("db.driver", "com.mysql.cj.jdbc.Driver");
            Class.forName(DB_DRIVER);

            // If Railway variables exist, use them (PRODUCTION)
            if (railwayMysqlUrl != null && !railwayMysqlUrl.isEmpty()) {
                System.out.println("🚀 Railway environment detected - Using Railway MySQL");

                // Railway provides full connection URL
                DB_URL = railwayMysqlUrl;

                // Add SSL and timezone parameters
                if (!DB_URL.contains("?")) {
                    DB_URL += "?useSSL=true&serverTimezone=UTC";
                } else if (!DB_URL.contains("useSSL")) {
                    DB_URL += "&useSSL=true&serverTimezone=UTC";
                }

                DB_USERNAME = railwayUser;
                DB_PASSWORD = railwayPassword;

                System.out.println("✅ Connected to Railway MySQL Database");

            } else if (railwayHost != null && railwayPort != null && railwayDatabase != null) {
                // Alternative: Build URL from individual Railway variables
                System.out.println("🚀 Railway environment detected - Building connection URL");

                DB_URL = String.format(
                        "jdbc:mysql://%s:%s/%s?useSSL=true&serverTimezone=UTC&allowPublicKeyRetrieval=true",
                        railwayHost, railwayPort, railwayDatabase
                );
                DB_USERNAME = railwayUser;
                DB_PASSWORD = railwayPassword;

                System.out.println("✅ Railway MySQL configured");

            } else {
                // Use local configuration (DEVELOPMENT)
                System.out.println("💻 Local environment detected - Using database.properties");

                DB_URL = props.getProperty("db.url");
                DB_USERNAME = props.getProperty("db.username");
                DB_PASSWORD = props.getProperty("db.password");

                // Add connection parameters for local MySQL
                String useSSL = props.getProperty("db.useSSL", "false");
                String timezone = props.getProperty("db.serverTimezone", "Asia/Kolkata");
                String allowPublicKey = props.getProperty("db.allowPublicKeyRetrieval", "true");
                String useUnicode = props.getProperty("db.useUnicode", "true");
                String characterEncoding = props.getProperty("db.characterEncoding", "UTF-8");

                if (!DB_URL.contains("?")) {
                    DB_URL += "?useSSL=" + useSSL
                            + "&serverTimezone=" + timezone
                            + "&allowPublicKeyRetrieval=" + allowPublicKey
                            + "&useUnicode=" + useUnicode
                            + "&characterEncoding=" + characterEncoding;
                }

                System.out.println("✅ Local MySQL configured");
            }

            // Print connection info (without password)
            System.out.println("Database configuration loaded successfully");
            String safeUrl = DB_URL.replaceAll("://([^:]+):([^@]+)@", "://***:***@");
            System.out.println("Database URL: " + safeUrl);

        } catch (IOException e) {
            System.err.println("❌ Error loading database.properties: " + e.getMessage());
            throw new RuntimeException("Error loading database.properties: " + e.getMessage(), e);
        } catch (ClassNotFoundException e) {
            System.err.println("❌ MySQL JDBC Driver not found: " + e.getMessage());
            throw new RuntimeException("MySQL JDBC Driver not found: " + e.getMessage(), e);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Get a database connection
     * @return Connection object
     * @throws SQLException if connection fails
     */
    public static synchronized Connection getConnection() throws SQLException {

        if (activeConnections >= MAX_POOL_SIZE) {
            throw new SQLException("Maximum pool size reached. Active connections: " + activeConnections);
        }

        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            activeConnections++;

            System.out.println("Connection established. Active connections: " + activeConnections);

            return connection;

        } catch (SQLException e) {
            System.err.println("❌ Database connection failed: " + e.getMessage());
            System.err.println("URL: " + DB_URL);
            System.err.println("Username: " + DB_USERNAME);
            throw new SQLException("Unable to connect to database: " + e.getMessage(), e);
        }
    }

    /**
     * Close database connection
     * @param connection Connection to close
     */
    public static synchronized void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
                activeConnections--;
                System.out.println("Connection closed. Active connections: " + activeConnections);
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }

    /**
     * Test database connectivity
     * @return true if connection successful
     */
    public static boolean testConnection() {
        Connection conn = null;
        try {
            System.out.println("🔍 Testing database connection...");
            conn = getConnection();

            if (conn != null && !conn.isClosed()) {
                System.out.println("✅ Database connection test PASSED");
                System.out.println("Database: " + conn.getMetaData().getDatabaseProductName());
                System.out.println("Version: " + conn.getMetaData().getDatabaseProductVersion());
                return true;
            }

        } catch (SQLException e) {
            System.err.println("❌ Database connection test FAILED");
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
            return false;

        } finally {
            closeConnection(conn);
        }

        return false;
    }

    /**
     * Get current active connections count
     */
    public static int getActiveConnectionsCount() {
        return activeConnections;
    }

    /**
     * Get database URL (without credentials)
     */
    public static String getDatabaseURL() {
        if (DB_URL != null) {
            return DB_URL.replaceAll("://([^:]+):([^@]+)@", "://***:***@");
        }
        return "Not configured";
    }
}
