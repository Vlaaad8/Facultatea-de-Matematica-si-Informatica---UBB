package org.example.practic2.domain.event;

import org.example.practic2.domain.Need;

public class NeedEntityChange extends jdk.jfr.Event implements Event {
    private ChangeEventType type;
    private Need data, oldData;

    public NeedEntityChange(ChangeEventType type, Need data) {
        this.type = type;
        this.data = data;
    }

    public NeedEntityChange(ChangeEventType type, Need data, Need oldData) {
        this.type = type;
        this.data = data;
        this.oldData = oldData;
    }

    public ChangeEventType getType() {
        return type;
    }

    public Need getData() {
        return data;
    }

    public Need getOldData() {
        return oldData;
    }
}
