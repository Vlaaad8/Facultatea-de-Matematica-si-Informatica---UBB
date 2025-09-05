package org.example.examen.domain.event;


import org.example.examen.domain.Entity;

public class RiverEntityChange extends jdk.jfr.Event implements Event {
    private ChangeEventType type;
    private Entity data, oldData;

    public RiverEntityChange(ChangeEventType type, Entity data) {
        this.type = type;
        this.data = data;
    }

    public RiverEntityChange(ChangeEventType type, Entity data, Entity oldData) {
        this.type = type;
        this.data = data;
        this.oldData = oldData;
    }

    public ChangeEventType getType() {
        return type;
    }

    public Entity getData() {
        return data;
    }

    public Entity getOldData() {
        return oldData;
    }
}
