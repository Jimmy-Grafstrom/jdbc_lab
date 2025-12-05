package se.jimmy.iths;

import se.jimmy.iths.connector.DatabaseConnector;
import se.jimmy.iths.model.Person;
import se.jimmy.iths.repository.PersonDAO;
import se.jimmy.iths.repository.PersonDAOImpl;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;

public class Main {
    public static void main() {

        Connection conn = DatabaseConnector.getConnection();
//        if (conn == null) {
//            System.err.println("Could not get connection to database.");
//        }
//        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
//            System.out.println("Closing database connection...");
//            DatabaseConnector.closeConnection();
//        }));
        PersonDAO personDAO = new PersonDAOImpl();

        try {
            DatabaseMetaData meta = conn.getMetaData();
            System.out.println("Ansluten till databas:");
            System.out.println("#---------------------#");
            System.out.println("- Produkt: " + meta.getDatabaseProductName());
            System.out.println("- Version: " + meta.getDatabaseProductVersion());
            System.out.println("- Driver: " + meta.getDriverName());
            System.out.println("- URL: " + meta.getURL());
            System.out.println("#---------------------#");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("---findAll---");
        personDAO.findAll().forEach(System.out::println);

        System.out.println("---Insert---");
        Person newPerson = new Person("Annika", "Bengtsson", Date.valueOf(LocalDate.of(1990, 1, 15)), 35000.0);
        personDAO.insert(newPerson);

        System.out.println("---findById---");
        System.out.println("Hittade: " + personDAO.findById(1L).getFirstName() + ", genom att söka efter Id=1");

        System.out.println("---Update---");
        Person person1 = personDAO.findById(1L);
        person1.setFirstName("Hedvig");
        personDAO.update(person1);
        System.out.println("#---------------------#");
        personDAO.delete(29L);
        System.out.println("#---------------------#");
    }
}

