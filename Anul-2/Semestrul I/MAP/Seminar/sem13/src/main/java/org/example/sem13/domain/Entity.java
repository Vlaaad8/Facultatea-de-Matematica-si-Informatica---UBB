package org.example.sem13.domain;

public class Entity<ID> {
    //The id of the entity
    private ID id;

    public Entity(ID id) {
        this.id = id;
    }

    /**
     * Getter for entity
     * @return id - the id of the entity
     */
    public ID getId() {
        return id;
    }


    /**
     * Setter for entity
     * @param id - the new id
     */
    public void setId(ID id) {
        this.id = id;
    }
}