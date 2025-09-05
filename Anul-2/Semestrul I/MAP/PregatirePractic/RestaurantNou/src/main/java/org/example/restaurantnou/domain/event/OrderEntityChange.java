package org.example.restaurantnou.domain.event;

import org.example.restaurantnou.domain.OrderItem;

public class OrderEntityChange extends jdk.jfr.Event implements Event {
    private ChangeEventType type;
    private OrderItem data, oldData;

    public OrderEntityChange(ChangeEventType type, OrderItem data) {
        this.type = type;
        this.data = data;
    }

    public OrderEntityChange(ChangeEventType type, OrderItem data, OrderItem oldData) {
        this.type = type;
        this.data = data;
        this.oldData = oldData;
    }

    public ChangeEventType getType() {
        return type;
    }

    public OrderItem getData() {
        return data;
    }

    public OrderItem getOldData() {
        return oldData;
    }
}
