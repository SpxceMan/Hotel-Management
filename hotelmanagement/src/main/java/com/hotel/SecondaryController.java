package com.hotel;

import java.io.IOException;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

public class SecondaryController {

    @FXML
    private TextField roomNumberField;

    @FXML
    private ComboBox<String> roomTypeCombo;

    @FXML
    private TextField priceField;

    @FXML
    private void initialize() {
        roomTypeCombo.setItems(FXCollections.observableArrayList("Single", "Double", "Deluxe"));
    }

    @FXML
    private void handleAddRoom() {
        String number = roomNumberField.getText() == null ? "" : roomNumberField.getText().trim();
        String type = roomTypeCombo.getValue();
        String priceText = priceField.getText() == null ? "" : priceField.getText().trim();

        if (number.isEmpty() || type == null || type.isEmpty() || priceText.isEmpty()) {
            showAlert("Validation Error", "Please fill in all fields.");
            return;
        }

        int roomNumber;
        try {
            roomNumber = Integer.parseInt(number);
        } catch (NumberFormatException ex) {
            showAlert("Validation Error", "Room number must be numeric.");
            return;
        }

        // Check uniqueness against the shared DataStore
        boolean exists = DataStore.roomsList.stream().anyMatch(r -> r.getRoomNumber() == roomNumber);
        if (exists) {
            showAlert("Duplicate Room", "A room with this number already exists.");
            return;
        }

        double price;
        try {
            price = Double.parseDouble(priceText);
        } catch (NumberFormatException ex) {
            showAlert("Validation Error", "Price must be a number.");
            return;
        }

        // Add to the shared DataStore so other views (RoomController, BookingController) can see it
        Room room = new Room(roomNumber, type, price, "Available");
        DataStore.rooms.add(room);

        roomNumberField.clear();
        roomTypeCombo.getSelectionModel().clearSelection();
        priceField.clear();

        showAlert("Success", "Room " + roomNumber + " added successfully.");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("primary");
    }
}
