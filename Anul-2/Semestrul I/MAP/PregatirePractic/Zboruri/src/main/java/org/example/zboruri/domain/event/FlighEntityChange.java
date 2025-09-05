package org.example.zboruri.domain.event;

import org.example.zboruri.domain.Flight;

public class FlighEntityChange extends jdk.jfr.Event implements Event {
    private ChangeEventType type;
    private Flight data, oldData;

    public FlighEntityChange(ChangeEventType type, Flight data) {
        this.type = type;
        this.data = data;
    }

    public FlighEntityChange(ChangeEventType type, Flight data, Flight oldData) {
        this.type = type;
        this.data = data;
        this.oldData = oldData;
    }

    public ChangeEventType getType() {
        return type;
    }

    public Flight getData() {
        return data;
    }

    public Flight getOldData() {
        return oldData;
    }
}
