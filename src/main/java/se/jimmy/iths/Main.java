package se.jimmy.iths;

import se.jimmy.iths.connector.DatabaseConnector;
import se.jimmy.iths.model.Person;
import se.jimmy.iths.repository.PersonDAO;
import se.jimmy.iths.repository.PersonDAOImpl;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;

public class Main {
    public static void main() {

        try (Connection conn = DatabaseConnector.getConnection()) {
            DatabaseMetaData meta = conn.getMetaData();
            System.out.println("Ansluten till databas:");
            System.out.println("#---------------------#");
            System.out.println("- Produkt: " + meta.getDatabaseProductName());
            System.out.println("- Version: " + meta.getDatabaseProductVersion());
            System.out.println("- Driver: " + meta.getDriverName());
            System.out.println("- URL: " + meta.getURL());
            System.out.println("#---------------------#");
        } catch (SQLException e) {
            System.err.println("Kunde inte ansluta till databasen för att hämta metadata.");
            e.printStackTrace();
        }

        PersonDAO personDAO = new PersonDAOImpl();

        System.out.println("---findAll---");
        personDAO.findAll().forEach(System.out::println);

        System.out.println("---Insert---");
        Person newPerson = new Person("Annika", "Bengtsson", Date.valueOf(LocalDate.of(1990, 1, 15)), 35000.0);
        personDAO.insert(newPerson);

        System.out.println("---findById---");
        Person foundPerson = personDAO.findById(1L);
        if (foundPerson != null) {
            System.out.println("Hittade: " + foundPerson.getFirstName() + ", genom att söka efter Id=1");
        } else {
            System.out.println("Ingen person hittades med Id=1");
        }

        System.out.println("---Update---");
        Person person1 = personDAO.findById(1L);
        if (person1 != null) {
            person1.setFirstName("Hedvig");
            personDAO.update(person1);
        }

        System.out.println("#---------------------#");
        personDAO.delete(29L);
        System.out.println("#---------------------#");
    }
}

