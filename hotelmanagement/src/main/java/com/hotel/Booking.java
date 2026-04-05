package com.hotel;

import java.time.LocalDate;

public class Booking {
    private final String roomNumber;
    private final String customerName;
    private final LocalDate checkIn;
    private final LocalDate checkOut;
    private final double totalPrice;

    public Booking(String roomNumber, String customerName, LocalDate checkIn, LocalDate checkOut, double totalPrice) {
        this.roomNumber = roomNumber;
        this.customerName = customerName;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.totalPrice = totalPrice;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public LocalDate getCheckIn() {
        return checkIn;
    }

    public LocalDate getCheckOut() {
        return checkOut;
    }

    public double getTotalPrice() {
        return totalPrice;
    }
}

