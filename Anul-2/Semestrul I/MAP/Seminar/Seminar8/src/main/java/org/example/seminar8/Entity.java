package org.example.seminar8;

public class Entity<ID> {
    private ID identityKey;


    public ID getId() {
        return identityKey;
    }

    public void setId(ID identityKey) {
        this.identityKey = identityKey;
    }
}


