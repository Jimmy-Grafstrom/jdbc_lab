package se.jimmy.iths.repository;

import se.jimmy.iths.model.Person;

import java.util.List;

public interface PersonDAO {
    void insert(Person person);
    List<Person> findAll();
}
