package com.hotel;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class BookingController {

    @FXML
    private ComboBox<Room> roomSelect;

    @FXML
    private TextField customerField;

    @FXML
    private DatePicker checkinPicker;

    @FXML
    private DatePicker checkoutPicker;

    @FXML
    private Label messageLabel;

    @FXML
    private TableView<Booking> bookingsTable;

    @FXML
    private TableColumn<Booking, String> colRoom;

    @FXML
    private TableColumn<Booking, String> colCustomer;

    @FXML
    private TableColumn<Booking, LocalDate> colCheckIn;

    @FXML
    private TableColumn<Booking, LocalDate> colCheckOut;

    @FXML
    private TableColumn<Booking, Double> colTotal;

    private FilteredList<Room> availableRooms;

    @FXML
    private void initialize() {
        availableRooms = new FilteredList<>(DataStore.rooms, r -> "Available".equals(r.getStatus()));
        roomSelect.setItems(availableRooms);

        colRoom.setCellValueFactory(new PropertyValueFactory<>("roomNumber"));
        colCustomer.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        colCheckIn.setCellValueFactory(new PropertyValueFactory<>("checkIn"));
        colCheckOut.setCellValueFactory(new PropertyValueFactory<>("checkOut"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));

        bookingsTable.setItems(DataStore.bookings);
    }

    @FXML
    private void handleBook() {
        Room room = roomSelect.getValue();
        String customer = customerField.getText() == null ? "" : customerField.getText().trim();
        LocalDate checkIn = checkinPicker.getValue();
        LocalDate checkOut = checkoutPicker.getValue();

        if (room == null || customer.isEmpty() || checkIn == null || checkOut == null) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Select a room, enter customer name, and choose check-in/check-out dates.");
            return;
        }

        long nights = ChronoUnit.DAYS.between(checkIn, checkOut);
        if (nights <= 0) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Check-out must be after check-in.");
            return;
        }

        double total = nights * room.getPrice();

        Booking b = new Booking(String.valueOf(room.getRoomNumber()), customer, checkIn, checkOut, total);
        DataStore.bookings.add(b);
        DataStore.customers.add(new Customer(customer));

        // mark room as booked and trigger list update so UI updates
        room.setStatus("Booked");
        int idx = DataStore.roomsList.indexOf(room);
        if (idx >= 0) {
            DataStore.rooms.set(idx, room);
        }

        roomSelect.getSelectionModel().clearSelection();
        customerField.clear();
        checkinPicker.setValue(null);
        checkoutPicker.setValue(null);
        messageLabel.setText("Booked room " + room.getRoomNumber() + " for " + customer + ". Total: " + total);
    }

    @FXML
    private void handleCheckout() {
        Booking b = bookingsTable.getSelectionModel().getSelectedItem();
        if (b == null) {
            showAlert(Alert.AlertType.WARNING, "No selection", "Please select a booking to checkout.");
            return;
        }

        // find room by room number
        Room room = DataStore.roomsList.stream()
                .filter(r -> String.valueOf(r.getRoomNumber()).equals(b.getRoomNumber()))
                .findFirst().orElse(null);

        if (room != null) {
            room.setStatus("Available");
            int idx = DataStore.roomsList.indexOf(room);
            if (idx >= 0) DataStore.rooms.set(idx, room);
        }

        DataStore.bookings.remove(b);
        showAlert(Alert.AlertType.INFORMATION, "Checkout", "Checkout complete. Total due: " + b.getTotalPrice());
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
