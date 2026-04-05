package com.hotel;

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
            showAlert(Alert.AlertType.WARNING, "Validation Error",
                    "Select a room, enter customer name, and choose check-in/check-out dates.");
            return;
        }

        // Validate check-in is not in the past
        if (checkIn.isBefore(LocalDate.now())) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Check-in date cannot be in the past.");
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

        // Only add customer if not already present (avoid duplicates)
        boolean customerExists = DataStore.customers.stream()
                .anyMatch(c -> c.getName().equalsIgnoreCase(customer));
        if (!customerExists) {
            DataStore.customers.add(new Customer(customer));
        }

        // Mark room as booked and trigger list update so UI refreshes
        room.setStatus("Booked");
        int idx = DataStore.roomsList.indexOf(room);
        if (idx >= 0) {
            DataStore.rooms.set(idx, room);
        }

        roomSelect.getSelectionModel().clearSelection();
        customerField.clear();
        checkinPicker.setValue(null);
        checkoutPicker.setValue(null);
        messageLabel.setText(String.format("Booked room %s for %s. Total: $%.2f",
                room.getRoomNumber(), customer, total));
    }

    @FXML
    private void handleCheckout() {
        Booking b = bookingsTable.getSelectionModel().getSelectedItem();
        if (b == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a booking to checkout.");
            return;
        }

        // Find room by room number and mark it available again
        Room room = DataStore.roomsList.stream()
                .filter(r -> String.valueOf(r.getRoomNumber()).equals(b.getRoomNumber()))
                .findFirst().orElse(null);

        if (room != null) {
            room.setStatus("Available");
            int idx = DataStore.roomsList.indexOf(room);
            if (idx >= 0) DataStore.rooms.set(idx, room);
        }

        // Add to cumulative earnings BEFORE removing the booking
        DataStore.totalEarnings += b.getTotalPrice();

        DataStore.bookings.remove(b);
        messageLabel.setText(String.format("Checked out. Earned: $%.2f", b.getTotalPrice()));
        showAlert(Alert.AlertType.INFORMATION, "Checkout",
                String.format("Checkout complete. Total due: $%.2f", b.getTotalPrice()));
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
