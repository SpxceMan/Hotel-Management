package com.hotel;

import javafx.beans.property.SimpleStringProperty;

public class Customer {
    private final SimpleStringProperty name;

    public Customer(String name) {
        this.name = new SimpleStringProperty(name);
    }

    public String getName() {
        return name.get();
    }
}
