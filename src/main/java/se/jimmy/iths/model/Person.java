package se.jimmy.iths.model;

import java.sql.Date;

public class Person {
    private String firstName;
    private String lastName;
    private Date dob;
    private double income;

    public Person(String firstName, String lastName, Date dob, double income) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dob = dob;
        this.income = income;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Date getDob() {
        return dob;
    }

    public double getIncome() {
        return income;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public void setIncome(double income) {
        this.income = income;
    }
}
