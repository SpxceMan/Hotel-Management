package com.hotel;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;

public class DashboardController {

    @FXML
    private StackPane centerPane;

    @FXML
    private void initialize() {
        showDashboard();
    }

    @FXML
    private void handleDashboard() {
        showDashboard();
    }

    @FXML
    private void handleRooms() {
        try {
            loadCenter("rooms");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBookings() {
        try {
            loadCenter("bookings");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCustomers() {
        centerPane.getChildren().clear();
        Label lbl = new Label("Customers view (not implemented)");
        centerPane.getChildren().add(lbl);
    }

    @FXML
    private void handleLogout() throws IOException {
        App.setRoot("primary");
    }

    private void loadCenter(String fxml) throws IOException {
        Parent node = FXMLLoader.load(getClass().getResource("/com/hotel/" + fxml + ".fxml"));
        centerPane.getChildren().setAll(node);
    }

    private void showDashboard() {
        centerPane.getChildren().clear();
        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(20);
        grid.setAlignment(Pos.CENTER);

        long totalRooms = DataStore.rooms.size();
        long availableRooms = DataStore.rooms.stream().filter(r -> "Available".equals(r.getStatus())).count();
        long bookings = DataStore.bookings.size();
        long customers = DataStore.customers.size();

        grid.add(createCard(String.valueOf(totalRooms), "Total Rooms", "card-total"), 0, 0);
        grid.add(createCard(String.valueOf(availableRooms), "Available Rooms", "card-available"), 1, 0);
        grid.add(createCard(String.valueOf(bookings), "Bookings", "card-bookings"), 0, 1);
        grid.add(createCard(String.valueOf(customers), "Customers", "card-customers"), 1, 1);

        centerPane.getChildren().add(grid);
    }

    private VBox createCard(String number, String title, String styleClass) {
        VBox box = new VBox(6);
        box.getStyleClass().add("card");
        box.getStyleClass().add(styleClass);
        box.setPrefSize(220, 120);
        box.setAlignment(Pos.CENTER);

        Label num = new Label(number);
        num.getStyleClass().add("card-number");
        Label t = new Label(title);
        t.getStyleClass().add("card-title");

        box.getChildren().addAll(num, t);
        return box;
    }
}
