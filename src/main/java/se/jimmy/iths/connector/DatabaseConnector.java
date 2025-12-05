package se.jimmy.iths.connector;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

public class DatabaseConnector {

    private static volatile Connection connection;

    private DatabaseConnector() {
    }

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                synchronized (DatabaseConnector.class) {
                    if (connection == null || connection.isClosed()) {
                        connection = createConnection();
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Misslyckades med att kontrollera eller återskapa databasanslutning", e);
        }
        return connection;
    }

    @SuppressWarnings("unchecked")
    private static Connection createConnection() throws SQLException {
        Yaml yaml = new Yaml();
        try (InputStream in = DatabaseConnector.class
                .getClassLoader()
                .getResourceAsStream("application.yml")) {
            if (in == null) {
                throw new RuntimeException("yml file not found in resource folder");
            }
            Map<String, Object> yamlData = (Map<String, Object>) yaml.load(in);

            Map<String, Object> jdbcConfig = (Map<String, Object>) yamlData.get("jdbc");
            if (jdbcConfig == null) {
                throw new RuntimeException("Missing 'jdbc' configuration in application.yml");
            }

            Map<String, Object> mysqlConfig = (Map<String, Object>) jdbcConfig.get("mysql");
            if (mysqlConfig == null) {
                throw new RuntimeException("Missing 'mysql' configuration under 'jdbc' in application.yml");
            }

            String url = (String) mysqlConfig.get("url");
            String user = (String) mysqlConfig.get("user");
            String pass = (String) mysqlConfig.get("pass");

            if (url == null || user == null || pass == null) {
                throw new RuntimeException("Missing database connection details (url, user, or pass) in application.yml");
            }
            return DriverManager.getConnection("jdbc:mysql:" + url, user, pass);
        } catch (Exception e) {
            if (e instanceof SQLException) {
                throw (SQLException) e;
            } else {
                throw new SQLException("Error loading or parsing application.yml for database configuration", e);
            }
        }
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
            } catch (SQLException e) {
                System.err.println("Error closing database connection: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}