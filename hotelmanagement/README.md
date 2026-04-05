# 🏨 Hotel Management System

A JavaFX desktop application for managing hotel rooms, bookings, customers, and revenue — all with a modern dark-themed UI.

---

## Screenshots

> Login → Dashboard → Rooms → Bookings → Customers

---

## Features

- **Login** — Secure credential check with a modern dark UI (demo: `admin` / `admin123`)
- **Dashboard** — Live stats cards: Total Rooms, Available Rooms, Active Bookings, Customers, and Total Earnings
- **Rooms** — Add, update, and view all rooms with number, type, price, and availability status
- **Bookings** — Book available rooms with customer name, check-in/check-out dates, and automatic price calculation. Checkout marks the room available again and records earnings
- **Customers** — Add, search, and remove customers. Shows each customer's active booking count and total spent

---

## Tech Stack

| Layer     | Technology              |
|-----------|-------------------------|
| Language  | Java 17                 |
| UI        | JavaFX 17 + FXML + CSS  |
| Build     | Maven 3.6+              |
| Storage   | In-memory (runtime only)|

---

## Project Structure

```
hotelmanagement/
├── pom.xml
└── src/main/
    ├── java/com/hotel/
    │   ├── App.java                  # Entry point, scene switching
    │   ├── PrimaryController.java    # Login screen
    │   ├── DashboardController.java  # Main shell + nav + dashboard cards
    │   ├── RoomController.java       # Rooms view (add, update, table)
    │   ├── BookingController.java    # Bookings view (book, checkout)
    │   ├── CustomerController.java   # Customers view (add, search, remove)
    │   ├── SecondaryController.java  # Legacy room form (secondary.fxml)
    │   ├── Room.java                 # Room model
    │   ├── Booking.java              # Booking model
    │   ├── Customer.java             # Customer model
    │   └── DataStore.java            # Shared in-memory state
    └── resources/com/hotel/
        ├── primary.fxml              # Login layout
        ├── dashboard.fxml            # Main shell layout
        ├── rooms.fxml                # Rooms view layout
        ├── bookings.fxml             # Bookings view layout
        ├── customers.fxml            # Customers view layout
        ├── secondary.fxml            # Legacy add-room form
        └── style.css                 # Full app stylesheet
```

---

## Requirements

- **JDK 17+** — [Download](https://adoptium.net/)
- **Maven 3.6+** — [Download](https://maven.apache.org/download.cgi)
- Internet access on first run (Maven downloads JavaFX 17.0.8 from Maven Central)

---

## Running the App

### Recommended (Maven)

```bash
cd hotelmanagement
mvn clean javafx:run
```

> ⚠️ Make sure you `cd hotelmanagement` first — the `pom.xml` lives there.

### Windows (PowerShell)

```powershell
cd hotelmanagement
mvn clean javafx:run
```

### Running from an IDE (IntelliJ / Eclipse)

If your IDE needs an explicit JavaFX SDK on the module path, add these VM options to your run configuration:

```
--module-path "C:\path\to\javafx-sdk-17\lib" --add-modules=javafx.controls,javafx.fxml
```

---

## Demo Credentials

| Username | Password  |
|----------|-----------|
| admin    | admin123  |

---

## How It Works

1. **Login** with the credentials above
2. Go to **Rooms** → add a room (number, type, price)
3. Go to **Bookings** → select a room, enter a customer name and dates → click **Book**
4. The room status changes to *Booked* and disappears from the available room list
5. Select a booking and click **Checkout** → the room becomes available again and the revenue is recorded
6. The **Dashboard** updates its cards live — including **Total Earnings**, which accumulates with every checkout
7. Go to **Customers** to search, add, or remove guests and see their spending history

---

## Notes

- All data is **in-memory only** — it resets when the app is closed. There is no database or file persistence.
- Duplicate room numbers and duplicate customer names are both prevented.
- Check-in dates in the past are rejected at booking time.