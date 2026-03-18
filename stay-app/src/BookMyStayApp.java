// Version 3.1

import java.util.HashMap;
import java.util.Map;

// Abstract Room Class
abstract class Room {
    private int beds;
    private double size;
    private double price;

    public Room(int beds, double size, double price) {
        this.beds = beds;
        this.size = size;
        this.price = price;
    }

    public int getBeds() {
        return beds;
    }

    public double getSize() {
        return size;
    }

    public double getPrice() {
        return price;
    }

    public abstract String getRoomType();

    public void displayDetails() {
        System.out.println("Room Type: " + getRoomType());
        System.out.println("Beds: " + beds);
        System.out.println("Size: " + size + " sq.ft");
        System.out.println("Price: ₹" + price);
    }
}

// Concrete Room Types
class SingleRoom extends Room {
    public SingleRoom() {
        super(1, 200, 1500);
    }

    public String getRoomType() {
        return "Single Room";
    }
}

class DoubleRoom extends Room {
    public DoubleRoom() {
        super(2, 350, 2500);
    }

    public String getRoomType() {
        return "Double Room";
    }
}

class SuiteRoom extends Room {
    public SuiteRoom() {
        super(3, 600, 5000);
    }

    public String getRoomType() {
        return "Suite Room";
    }
}

// Inventory Class (NEW – Core of Use Case 3)
class RoomInventory {

    private HashMap<String, Integer> inventory;

    // Constructor → Initialize inventory
    public RoomInventory() {
        inventory = new HashMap<>();

        // Initial room counts
        inventory.put("Single Room", 5);
        inventory.put("Double Room", 3);
        inventory.put("Suite Room", 2);
    }

    // Get availability
    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    // Update availability (controlled update)
    public void updateAvailability(String roomType, int count) {
        if (inventory.containsKey(roomType)) {
            inventory.put(roomType, count);
        } else {
            System.out.println("Room type not found!");
        }
    }

    // Display full inventory
    public void displayInventory() {
        System.out.println("=== Current Room Inventory ===");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " -> Available: " + entry.getValue());
        }
        System.out.println();
    }
}

// Main Class
public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("Welcome to the Hotel Booking Management System!");
        System.out.println("Application: Book My Stay App");
        System.out.println("Version: 3.1\n");

        // Create Room Objects (Domain)
        Room single = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suite = new SuiteRoom();

        // Create Inventory (Centralized State)
        RoomInventory inventory = new RoomInventory();

        // Display Room Details
        System.out.println("=== Room Details ===\n");
        single.displayDetails();
        System.out.println();

        doubleRoom.displayDetails();
        System.out.println();

        suite.displayDetails();
        System.out.println();

        // Display Inventory
        inventory.displayInventory();

        // Example Update
        System.out.println("Updating Single Room availability to 4...\n");
        inventory.updateAvailability("Single Room", 4);

        // Display Updated Inventory
        inventory.displayInventory();

        System.out.println("Application Terminated.");
    }
}