package se.jimmy.iths.connector;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

public class DatabaseConnector {

    private DatabaseConnector() {
    }

    @SuppressWarnings("unchecked")
    public static Connection getConnection() throws SQLException {
        Yaml yaml = new Yaml();
        try (InputStream in = DatabaseConnector.class
                .getClassLoader()
                .getResourceAsStream("application.yml")) {
            if (in == null) {
                throw new SQLException("yml file not found in resource folder");
            }
            Map<String, Object> yamlData = (Map<String, Object>) yaml.load(in);
            Map<String, Object> jdbcConfig = (Map<String, Object>) yamlData.get("jdbc");
            Map<String, Object> mysqlConfig = (Map<String, Object>) jdbcConfig.get("mysql");

            String url = (String) mysqlConfig.get("url");
            String user = (String) mysqlConfig.get("user");
            String pass = (String) mysqlConfig.get("pass");

            return DriverManager.getConnection("jdbc:mysql:" + url, user, pass);
        } catch (Exception e) {
            if (e instanceof SQLException) {
                throw (SQLException) e;
            }
            throw new SQLException("Error configuring database connection", e);
        }
    }
}