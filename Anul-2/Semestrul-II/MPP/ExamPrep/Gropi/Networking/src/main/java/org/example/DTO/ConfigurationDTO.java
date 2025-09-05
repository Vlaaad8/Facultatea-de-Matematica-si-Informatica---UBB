package org.example.DTO;

import java.io.Serializable;

public class ConfigurationDTO implements Serializable {
    private int id;
    private int i;
    private int j;
    private int focusColumn;

    public ConfigurationDTO( int id,int focusColumn, int iCoordinate, int jCoordinate) {
        this.i = iCoordinate;
        this.j = jCoordinate;
        this.id=id;
        this.focusColumn=focusColumn;
    }

    public int getI() {
        return i;
    }

    public int getJ() {
        return j;
    }

    public int getFocusColumn() {
        return focusColumn;
    }

    public int getId() {
        return id;
    }

}
