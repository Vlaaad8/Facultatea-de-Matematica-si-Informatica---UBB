package org.example.apeleromane.domain.event;

import org.example.apeleromane.domain.River;

public class RiverEntityChange extends jdk.jfr.Event implements Event {
    private ChangeEventType type;
    private River data, oldData;

    public RiverEntityChange(ChangeEventType type, River data) {
        this.type = type;
        this.data = data;
    }

    public RiverEntityChange(ChangeEventType type, River data, River oldData) {
        this.type = type;
        this.data = data;
        this.oldData = oldData;
    }

    public ChangeEventType getType() {
        return type;
    }

    public River getData() {
        return data;
    }

    public River getOldData() {
        return oldData;
    }
}
