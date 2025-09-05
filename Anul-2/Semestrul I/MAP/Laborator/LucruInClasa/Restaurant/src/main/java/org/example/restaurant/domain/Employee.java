package org.example.restaurant.domain;

public class Employee extends Entity<Long>{
    private String name;
    private int age;

    public Employee(Long aLong, String name, int age) {
        super(aLong);
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }
}
