package se.jimmy.iths.repository;

import se.jimmy.iths.model.Person;

import java.util.List;

public interface PersonDAO {
    List<Person> findAll();
    Person findById(Long id);
    void insert(Person person);
    void update(Person person);
    void delete(Long id);
}
