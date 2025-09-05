package org.example.apeleromane.observer;

import jdk.jfr.Event;

public interface Observer<E extends Event> {
    void update(E e);
}