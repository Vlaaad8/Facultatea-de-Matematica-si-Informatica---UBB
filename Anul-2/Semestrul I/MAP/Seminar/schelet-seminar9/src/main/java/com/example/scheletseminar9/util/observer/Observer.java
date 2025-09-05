package com.example.scheletseminar9.util.observer;
import com.example.scheletseminar9.util.event.Event;

public interface Observer<E extends Event> {
    void update(E e);
}