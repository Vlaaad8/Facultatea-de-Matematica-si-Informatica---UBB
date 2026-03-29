package org.example.DTO;

import java.io.Serializable;

public class ListDTO implements Serializable {
    int column;
    int config;

    public ListDTO(int column, int config) {
        this.column = column;
        this.config = config;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public int getConfig() {
        return config;
    }

    public void setConfig(int config) {
        this.config = config;
    }
}
