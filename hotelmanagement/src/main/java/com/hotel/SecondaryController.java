package com.hotel;

import java.io.IOException;
import java.util.ArrayList;

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

    private final ArrayList<Room> rooms = new ArrayList<>();

    public static class Room {
        private final String number;
        private final String type;
        private final double price;

        public Room(String number, String type, double price) {
            this.number = number;
            this.type = type;
            this.price = price;
        }

        @Override
        public String toString() {
            return "Room[number=" + number + ", type=" + type + ", price=" + price + "]";
        }
    }

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

        double price;
        try {
            price = Double.parseDouble(priceText);
        } catch (NumberFormatException ex) {
            showAlert("Validation Error", "Price must be a number.");
            return;
        }

        Room room = new Room(number, type, price);
        rooms.add(room);
        System.out.println("Added room: " + room);

        roomNumberField.clear();
        roomTypeCombo.getSelectionModel().clearSelection();
        priceField.clear();
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