package se.jimmy.iths.repository;

import se.jimmy.iths.connector.DatabaseConnector;
import se.jimmy.iths.model.Person;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PersonDAOImpl implements PersonDAO {


    @Override
    public List<Person> findAll() {

        List<Person> list = new ArrayList<>();
        String sql =  "SELECT * FROM person";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Person p = new Person(
                        rs.getLong("person_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getDate("dob"),
                        rs.getDouble("income")
                );
                list.add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }


    @Override
    public Person findById(Long id) {
        String sql = "SELECT * FROM person WHERE person_id = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Person(
                            rs.getLong("person_id"),
                            rs.getString("first_name"),
                            rs.getString("last_name"),
                            rs.getDate("dob"),
                            rs.getDouble("income")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void insert(Person person) {
        String sql = "INSERT INTO person (first_name, last_name, dob, income) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, person.getFirstName());
            ps.setString(2, person.getLastName());
            ps.setDate(3, person.getDob());
            ps.setDouble(4, person.getIncome());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    person.setId(rs.getLong(1));
                    System.out.println("DAO: Lade till person: " + person.getFirstName() + " med ID: " + person.getId());
                } else {
                    System.out.println("DAO: Kunde inte hämta genererat ID för ny person.");
                }
            }
        } catch (SQLException e) {
            System.err.println("DAO: FEL vid insättning av person: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void update(Person person) {
        String sql = "UPDATE person SET first_name = ?, last_name = ?, dob = ?, income = ? WHERE person_id = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, person.getFirstName());
            ps.setString(2, person.getLastName());
            ps.setDate(3, person.getDob());
            ps.setDouble(4, person.getIncome());
            ps.setLong(5, person.getId());
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("DAO: Uppdaterade person med ID " + person.getId() + ". Rader påverkade: " + rowsAffected);
            } else {
                System.out.println("DAO: Hittade ingen person med ID " + person.getId() + " att uppdatera.");
            }

        } catch (Exception e) {
            System.err.println("DAO: FEL vid uppdatering av person med ID " + person.getId() + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM person WHERE person_id = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("DAO: Tog bort person med ID " + id + ". Rader påverkade: " + rowsAffected);
            } else {
                System.out.println("DAO: Hittade ingen person med ID " + id + " att ta bort.");
            }
        } catch (Exception e) {
            System.err.println("DAO: FEL vid borttagning av person med ID " + id + ": " + e.getMessage());
            e.printStackTrace();
        }
    }
}
