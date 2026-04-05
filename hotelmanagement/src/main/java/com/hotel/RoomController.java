package com.hotel;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class RoomController {

    @FXML
    private TextField roomNumberField;

    @FXML
    private ComboBox<String> roomTypeCombo;

    @FXML
    private TextField priceField;

    @FXML
    private TableView<Room> roomsTable;

    @FXML
    private TableColumn<Room, Number> colNumber;

    @FXML
    private TableColumn<Room, String> colType;

    @FXML
    private TableColumn<Room, Number> colPrice;

    @FXML
    private TableColumn<Room, String> colStatus;

    @FXML
    private Button updateButton;
    
    private Room editingRoom = null;

    @FXML
    private void initialize() {
        roomTypeCombo.setItems(FXCollections.observableArrayList("Single", "Double", "Deluxe"));

        // Bind columns to bean getters (getRoomNumber, getType, getPrice, getStatus)
        colNumber.setCellValueFactory(new PropertyValueFactory<>("roomNumber"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Table is backed by the ObservableList wrapper which is backed by an ArrayList
        roomsTable.setItems(DataStore.rooms);

        // initialize update button state
        updateButton.setDisable(true);

        // when selecting a room populate form for editing
        roomsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                editingRoom = newSel;
                roomNumberField.setText(String.valueOf(newSel.getRoomNumber()));
                roomTypeCombo.setValue(newSel.getType());
                priceField.setText(String.valueOf(newSel.getPrice()));
                updateButton.setDisable(false);
            } else {
                editingRoom = null;
                updateButton.setDisable(true);
            }
        });
    }

    @FXML
    private void handleAddRoom() {
        String numberText = roomNumberField.getText() == null ? "" : roomNumberField.getText().trim();
        String type = roomTypeCombo.getValue();
        String priceText = priceField.getText() == null ? "" : priceField.getText().trim();

        if (numberText.isEmpty() || type == null || type.isEmpty() || priceText.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Please fill in all fields.");
            return;
        }

        int number;
        try {
            number = Integer.parseInt(numberText);
        } catch (NumberFormatException ex) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Room number must be numeric.");
            return;
        }

        // Check uniqueness against the underlying ArrayList
        boolean exists = DataStore.roomsList.stream().anyMatch(r -> r.getRoomNumber() == number);
        if (exists) {
            showAlert(Alert.AlertType.WARNING, "Duplicate Room", "Room already exists");
            return;
        }

        double price;
        try {
            price = Double.parseDouble(priceText);
        } catch (NumberFormatException ex) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Price must be a number.");
            return;
        }

        Room room = new Room(number, type, price, "Available");

        // Add via ObservableList wrapper so TableView updates immediately; underlying storage is ArrayList
        DataStore.rooms.add(room);

        roomNumberField.clear();
        roomTypeCombo.getSelectionModel().clearSelection();
        priceField.clear();

        showAlert(Alert.AlertType.INFORMATION, "Success", "Room added successfully");
    }

    @FXML
    private void handleUpdateRoom() {
        if (editingRoom == null) {
            showAlert(Alert.AlertType.WARNING, "No selection", "Select a room to update.");
            return;
        }

        String numberText = roomNumberField.getText() == null ? "" : roomNumberField.getText().trim();
        String type = roomTypeCombo.getValue();
        String priceText = priceField.getText() == null ? "" : priceField.getText().trim();

        if (numberText.isEmpty() || type == null || type.isEmpty() || priceText.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Please fill in all fields.");
            return;
        }

        int number;
        try {
            number = Integer.parseInt(numberText);
        } catch (NumberFormatException ex) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Room number must be numeric.");
            return;
        }

        // ensure uniqueness (allow same number if it's the same room being edited)
        boolean exists = DataStore.roomsList.stream().anyMatch(r -> r.getRoomNumber() == number && r != editingRoom);
        if (exists) {
            showAlert(Alert.AlertType.WARNING, "Duplicate Room", "Room already exists");
            return;
        }

        double price;
        try {
            price = Double.parseDouble(priceText);
        } catch (NumberFormatException ex) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Price must be a number.");
            return;
        }

        // apply updates
        editingRoom.setRoomNumber(number);
        editingRoom.setType(type);
        editingRoom.setPrice(price);

        // trigger ObservableList change so UIs update
        int idx = DataStore.roomsList.indexOf(editingRoom);
        if (idx >= 0) DataStore.rooms.set(idx, editingRoom);

        roomsTable.refresh();
        roomsTable.getSelectionModel().clearSelection();
        roomNumberField.clear();
        roomTypeCombo.getSelectionModel().clearSelection();
        priceField.clear();
        updateButton.setDisable(true);

        showAlert(Alert.AlertType.INFORMATION, "Success", "Room updated successfully");
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
