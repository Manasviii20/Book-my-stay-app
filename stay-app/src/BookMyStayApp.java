// Version 6.1

import java.util.*;

// Reservation (Request)
class Reservation {
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }
}

// Inventory Service
class RoomInventory {
    private HashMap<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
        inventory.put("Single Room", 2);
        inventory.put("Double Room", 1);
        inventory.put("Suite Room", 1);
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    public void reduceAvailability(String roomType) {
        inventory.put(roomType, inventory.get(roomType) - 1);
    }

    public void displayInventory() {
        System.out.println("\n=== Updated Inventory ===");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }
    }
}

// Booking Queue
class BookingRequestQueue {
    private Queue<Reservation> queue = new LinkedList<>();

    public void addRequest(Reservation r) {
        queue.add(r);
    }

    public Reservation getNextRequest() {
        return queue.poll(); // FIFO
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}

// Booking Service (CORE LOGIC)
class BookingService {

    private RoomInventory inventory;

    // Track allocated rooms
    private HashMap<String, Set<String>> allocatedRooms;

    public BookingService(RoomInventory inventory) {
        this.inventory = inventory;
        this.allocatedRooms = new HashMap<>();
    }

    // Generate unique room ID
    private String generateRoomId(String roomType, int count) {
        return roomType.replace(" ", "") + "-" + count;
    }

    public void processBookings(BookingRequestQueue queue) {

        System.out.println("\n=== Processing Bookings ===");

        while (!queue.isEmpty()) {

            Reservation r = queue.getNextRequest();
            String type = r.getRoomType();

            System.out.println("\nProcessing request for: " + r.getGuestName());

            // Check availability
            if (inventory.getAvailability(type) > 0) {

                // Initialize set if not exists
                allocatedRooms.putIfAbsent(type, new HashSet<>());

                Set<String> roomSet = allocatedRooms.get(type);

                // Generate unique ID
                String roomId = generateRoomId(type, roomSet.size() + 1);

                // Ensure uniqueness (Set prevents duplicates)
                roomSet.add(roomId);

                // Update inventory
                inventory.reduceAvailability(type);

                // Confirm booking
                System.out.println("Booking Confirmed!");
                System.out.println("Guest: " + r.getGuestName());
                System.out.println("Room Type: " + type);
                System.out.println("Allocated Room ID: " + roomId);

            } else {
                System.out.println("Booking Failed! No rooms available for " + type);
            }
        }
    }
}

// Main Class
class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("Welcome to the Hotel Booking Management System!");
        System.out.println("Application: Book My Stay App");
        System.out.println("Version: 6.1\n");

        // Inventory
        RoomInventory inventory = new RoomInventory();

        // Queue
        BookingRequestQueue queue = new BookingRequestQueue();

        // Add Requests
        queue.addRequest(new Reservation("Alice", "Single Room"));
        queue.addRequest(new Reservation("Bob", "Single Room"));
        queue.addRequest(new Reservation("Charlie", "Single Room")); // should fail
        queue.addRequest(new Reservation("David", "Suite Room"));

        // Booking Service
        BookingService bookingService = new BookingService(inventory);

        // Process Bookings
        bookingService.processBookings(queue);

        // Show Final Inventory
        inventory.displayInventory();

        System.out.println("\nAll bookings processed.");
    }
}