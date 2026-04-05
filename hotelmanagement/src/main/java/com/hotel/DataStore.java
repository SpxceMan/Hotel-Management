package com.hotel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;

public class DataStore {
    // Primary storage required by user: an ArrayList that persists at runtime
    public static final ArrayList<Room> roomsList = new ArrayList<>();

    // ObservableList wrapper backed by the ArrayList for UI bindings
    public static final ObservableList<Room> rooms = FXCollections.observableList(roomsList);

    public static final ObservableList<Booking> bookings = FXCollections.observableArrayList();
    public static final ObservableList<Customer> customers = FXCollections.observableArrayList();

    // Cumulative earnings — only increases when a booking is checked out
    public static double totalEarnings = 0.0;
}
