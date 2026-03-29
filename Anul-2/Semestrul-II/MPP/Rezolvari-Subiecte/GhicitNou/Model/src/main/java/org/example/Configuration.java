package org.example;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name="configurations")
public class Configuration extends org.example.Entity<Integer> {
    @Column(nullable = false)
    String value1;
    @Column(nullable = false)
    String value2;
    @Column(nullable = false)
    String value3;
    @Column(nullable = false)
    String value4;

    public Configuration(){

    }


    public Configuration(String value1, String value2, String value3, String value4) {
        this.value1 = value1;
        this.value2 = value2;
        this.value3 = value3;
        this.value4 = value4;
    }

    public String getValue1() {
        return value1;
    }

    public void setValue1(String value1) {
        this.value1 = value1;
    }

    public String getValue2() {
        return value2;
    }

    public void setValue2(String value2) {
        this.value2 = value2;
    }

    public String getValue3() {
        return value3;
    }

    public void setValue3(String value3) {
        this.value3 = value3;
    }

    public String getValue4() {
        return value4;
    }

    public void setValue4(String value4) {
        this.value4 = value4;
    }
}
