package se.jimmy.iths;

import se.jimmy.iths.connector.DatabaseConnector;

import java.sql.*;

public class Main {
    public static void main() {

        Connection conn = DatabaseConnector.getConnection();
        if (conn == null) {
            System.err.println("Could not get connection to database.");
        }
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Closing database connection...");
            DatabaseConnector.closeConnection();
        }));

        String sql = "SELECT * FROM person";

        try {
            DatabaseMetaData meta = conn.getMetaData();
            System.out.println("Ansluten till databas:");
            System.out.println("#---------------------#");
            System.out.println("- Produkt: " + meta.getDatabaseProductName());
            System.out.println("- Version: " + meta.getDatabaseProductVersion());
            System.out.println("- Driver: " + meta.getDriverName());
            System.out.println("- URL: " + meta.getURL());
            System.out.println("#---------------------#");

            PreparedStatement statement = conn.prepareStatement(sql);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("person_id");
                    String firstName = rs.getString("first_name");
                    String lastName = rs.getString("last_name");
                    java.sql.Date dob = rs.getDate("dob");
                    double income = rs.getDouble("income");
                    System.out.println(id + ": " + firstName + " " + lastName + " - " + dob + " - " + income);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("#---------------------#");
        String sqlFilter = "SELECT * FROM person WHERE income > ?";
        System.out.println("Personer som ");

        try (PreparedStatement statement = conn.prepareStatement(sqlFilter)) {
            statement.setDouble(1, 150000.0);

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("person_id");
                    String firstName = rs.getString("first_name");
                    String lastName = rs.getString("last_name");
                    java.sql.Date dob = rs.getDate("dob");
                    double income = rs.getDouble("income");
                    System.out.println(id + ": " + firstName + " " + lastName + " - " + dob + " - " + income);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("#---------------------#");
        String insertNewPerson = "INSERT INTO person (first_name, last_name, dob, income) VALUES (?, ?, ?, ?)";

        try (PreparedStatement statement = conn.prepareStatement(insertNewPerson)) {
            statement.setString(1, "Lisa");
            statement.setString(2, "Larsson");
            statement.setDate(3, new java.sql.Date(System.currentTimeMillis()));
            statement.setDouble(4, 150000.0);
            int rowsInserted = statement.executeUpdate();
            System.out.println("Antal rader infogade: " + rowsInserted);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println("#---------------------#");
        String updateIncomePerson = "UPDATE person SET income = ? WHERE person_id >= ?";

        try (PreparedStatement statement = conn.prepareStatement(updateIncomePerson)) {
            statement.setDouble(1, 250000.0);
            statement.setInt(2, 6);
            int rowsInserted = statement.executeUpdate();
            System.out.println("Antal rader uppdaterade: " + rowsInserted);


        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("#---------------------#");
        String deletePerson = "DELETE FROM person WHERE person_id >= ?";
        try (PreparedStatement statement = conn.prepareStatement(deletePerson)) {
            statement.setInt(1, 6);
            int rowsDeleted = statement.executeUpdate();
            System.out.println("Antal rader borttagna: " + rowsDeleted);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("#---------------------#");

//        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
//            System.out.println("Closing database connection...");
//            DatabaseConnector.closeConnection();
//        }));
    }
}
