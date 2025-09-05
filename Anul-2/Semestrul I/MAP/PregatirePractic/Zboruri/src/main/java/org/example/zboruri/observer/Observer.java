package org.example.zboruri.observer;

import jdk.jfr.Event;

public interface Observer<E extends Event> {
    void update(E e);
}