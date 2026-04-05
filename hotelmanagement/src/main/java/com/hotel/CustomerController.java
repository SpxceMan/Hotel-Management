package com.hotel;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class CustomerController {

    @FXML private TableView<Customer> customersTable;
    @FXML private TableColumn<Customer, Integer> colIndex;
    @FXML private TableColumn<Customer, String>  colName;
    @FXML private TableColumn<Customer, Integer> colBookings;
    @FXML private TableColumn<Customer, String>  colTotal;

    @FXML private TextField searchField;
    @FXML private TextField addNameField;
    @FXML private Label     countLabel;
    @FXML private Label     messageLabel;

    private FilteredList<Customer> filteredCustomers;

    @FXML
    private void initialize() {
        // Row-number column
        colIndex.setCellValueFactory(col -> {
            int idx = customersTable.getItems().indexOf(col.getValue()) + 1;
            return new SimpleIntegerProperty(idx).asObject();
        });

        // Name column
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));

        // Active bookings count derived from DataStore.bookings
        colBookings.setCellValueFactory(col -> {
            String name = col.getValue().getName();
            long count = DataStore.bookings.stream()
                    .filter(b -> b.getCustomerName().equalsIgnoreCase(name))
                    .count();
            return new SimpleIntegerProperty((int) count).asObject();
        });

        // Total spent across all bookings for this customer
        colTotal.setCellValueFactory(col -> {
            String name = col.getValue().getName();
            double total = DataStore.bookings.stream()
                    .filter(b -> b.getCustomerName().equalsIgnoreCase(name))
                    .mapToDouble(Booking::getTotalPrice)
                    .sum();
            return new SimpleStringProperty(String.format("$%.2f", total));
        });

        // Filtered list wired to search field
        filteredCustomers = new FilteredList<>(DataStore.customers, c -> true);
        searchField.textProperty().addListener((obs, old, text) -> {
            filteredCustomers.setPredicate(c ->
                text == null || text.isBlank() ||
                c.getName().toLowerCase().contains(text.toLowerCase())
            );
            refreshCount();
        });

        customersTable.setItems(filteredCustomers);

        // Keep count badge live
        DataStore.customers.addListener(
            (javafx.collections.ListChangeListener<Customer>) change -> refreshCount());
        refreshCount();

        messageLabel.setText("");
    }

    @FXML
    private void handleAdd() {
        String name = addNameField.getText() == null ? "" : addNameField.getText().trim();
        if (name.isEmpty()) {
            showMsg("Please enter a customer name.", true);
            return;
        }

        boolean exists = DataStore.customers.stream()
                .anyMatch(c -> c.getName().equalsIgnoreCase(name));
        if (exists) {
            showMsg("Customer \"" + name + "\" already exists.", true);
            return;
        }

        DataStore.customers.add(new Customer(name));
        addNameField.clear();
        refreshCount();
        showMsg("Customer \"" + name + "\" added.", false);
    }

    @FXML
    private void handleRemove() {
        Customer selected = customersTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showMsg("Select a customer to remove.", true);
            return;
        }

        // Check for active bookings
        long active = DataStore.bookings.stream()
                .filter(b -> b.getCustomerName().equalsIgnoreCase(selected.getName()))
                .count();
        if (active > 0) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Active Bookings");
            alert.setHeaderText(null);
            alert.setContentText("\"" + selected.getName() + "\" has " + active +
                    " active booking(s). Remove anyway?");
            alert.showAndWait().ifPresent(btn -> {
                if (btn == ButtonType.OK) {
                    DataStore.customers.remove(selected);
                    refreshCount();
                    showMsg("Customer removed.", false);
                }
            });
        } else {
            DataStore.customers.remove(selected);
            refreshCount();
            showMsg("Customer \"" + selected.getName() + "\" removed.", false);
        }
    }

    private void refreshCount() {
        int total = DataStore.customers.size();
        countLabel.setText(total + (total == 1 ? " customer" : " customers"));
    }

    private void showMsg(String text, boolean warn) {
        messageLabel.setText(text);
        messageLabel.setStyle(warn
                ? "-fx-text-fill: #dc2626; -fx-font-weight:600;"
                : "-fx-text-fill: #16a34a; -fx-font-weight:600;");
    }
}
