package org.example.restaurantnou.domain.observer;

import jdk.jfr.Event;

public interface Observer<E extends Event> {
    void update(E e);
}