package com.example.scheletseminar9.util.observer;

import com.example.scheletseminar9.util.event.Event;

public interface Observable<E extends Event> {
    void addObserver(Observer<E> e);
    void removeObserver(Observer<E> e);
    void notifyObservers(E e);
}
