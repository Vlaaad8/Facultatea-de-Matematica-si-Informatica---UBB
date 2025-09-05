package org.example.demo.domain.event;


import org.example.demo.domain.Looking;

public class LookingEntityChange extends jdk.jfr.Event implements Event {
    private ChangeEventType type;
    private Looking data, oldData;

    public LookingEntityChange(ChangeEventType type, Looking data) {
        this.type = type;
        this.data = data;
    }

    public LookingEntityChange(ChangeEventType type, Looking data, Looking oldData) {
        this.type = type;
        this.data = data;
        this.oldData = oldData;
    }

    public ChangeEventType getType() {
        return type;
    }

    public Looking getData() {
        return data;
    }

    public Looking getOldData() {
        return oldData;
    }
}
