package org.example;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name="configurations")
public class Configuration extends org.example.Entity<Integer> {
    private int mainConfig;
    private int i;
    private int j;

    public Configuration(int focusColumn, int i, int j) {
        this.mainConfig = focusColumn;
        this.i = i;
        this.j = j;
    }

    public Configuration(){

    }

    public int getFocusColumn() {
        return mainConfig;
    }

    public void setFocusColumn(int focusColumn) {
        this.mainConfig = focusColumn;
    }

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

    public int getJ() {
        return j;
    }

    public void setJ(int j) {
        this.j = j;
    }
}
