package org.example.clientfx;

import jakarta.persistence.Column;
import jakarta.persistence.Table;

@jakarta.persistence.Entity
@Table(name="employees")
public class Employee extends Entity<Integer> {
    @Column(nullable = false)
    private String user;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;

    public Employee(String user, String password, String firstName, String lastName) {
        this.user = user;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }
    public Employee(){
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "user='" + user + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
