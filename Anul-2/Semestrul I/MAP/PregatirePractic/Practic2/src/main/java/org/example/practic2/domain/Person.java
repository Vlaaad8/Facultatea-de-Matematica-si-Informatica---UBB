package org.example.practic2.domain;

public class Person extends Entity{
    private String lastName;
    private String firstName;
    private String username;
    private String password;
    private String town;
    private String street;
    private String numberStreet;
    private String telephone;

    public Person(String lastName, String firstName, String username, String password, String town, String street, String numberStreet, String telephone) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.username = username;
        this.password = password;
        this.town = town;
        this.street = street;
        this.numberStreet = numberStreet;
        this.telephone = telephone;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getUsername() {
        return username;
    }

    public String getTown() {
        return town;
    }

    public String getPassword() {
        return password;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getStreet() {
        return street;
    }

    public String getNumberStreet() {
        return numberStreet;
    }
}
