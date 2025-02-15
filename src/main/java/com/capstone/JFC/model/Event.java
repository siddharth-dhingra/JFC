package com.capstone.JFC.model;

public interface Event<T> {
    String getEventId();
    EventTypes getType();
    T getPayload();
}