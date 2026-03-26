import java.util.*;

// Booking class to store booking details
class Booking {
    String bookingId;
    String roomType;
    String roomId;
    boolean isActive;

    public Booking(String bookingId, String roomType, String roomId) {
        this.bookingId = bookingId;
        this.roomType = roomType;
        this.roomId = roomId;
        this.isActive = true;
    }
}

// Inventory Manager
class InventoryManager {
    private Map<String, Integer> roomInventory = new HashMap<>();
    private Map<String, Queue<String>> roomPool = new HashMap<>();

    public InventoryManager() {
        // Initialize inventory
        addRoomType("Deluxe", 2);
        addRoomType("Suite", 2);
    }

    private void addRoomType(String type, int count) {
        roomInventory.put(type, count);
        Queue<String> rooms = new LinkedList<>();

        for (int i = 1; i <= count; i++) {
            rooms.add(type + "-R" + i);
        }
        roomPool.put(type, rooms);
    }

    public String allocateRoom(String type) {
        if (roomInventory.getOrDefault(type, 0) > 0) {
            roomInventory.put(type, roomInventory.get(type) - 1);
            return roomPool.get(type).poll();
        }
        return null;
    }

    public void releaseRoom(String type, String roomId) {
        roomInventory.put(type, roomInventory.get(type) + 1);
        roomPool.get(type).offer(roomId);
    }

    public void displayInventory() {
        System.out.println("Current Inventory:");
        for (String type : roomInventory.keySet()) {
            System.out.println(type + " -> Available: " + roomInventory.get(type));
        }
    }
}

// Booking Service
class BookingService {
    private Map<String, Booking> bookings = new HashMap<>();
    private InventoryManager inventoryManager;
    private Stack<String> rollbackStack = new Stack<>();

    public BookingService(InventoryManager inventoryManager) {
        this.inventoryManager = inventoryManager;
    }

    public void createBooking(String bookingId, String roomType) {
        String roomId = inventoryManager.allocateRoom(roomType);

        if (roomId == null) {
            System.out.println("No rooms available for type: " + roomType);
            return;
        }

        Booking booking = new Booking(bookingId, roomType, roomId);
        bookings.put(bookingId, booking);

        System.out.println("Booking Confirmed: " + bookingId + " | Room: " + roomId);
    }

    public void cancelBooking(String bookingId) {
        System.out.println("\nProcessing cancellation for: " + bookingId);

        // Validation
        if (!bookings.containsKey(bookingId)) {
            System.out.println("Cancellation Failed: Booking does not exist.");
            return;
        }

        Booking booking = bookings.get(bookingId);

        if (!booking.isActive) {
            System.out.println("Cancellation Failed: Booking already cancelled.");
            return;
        }

        // Step 1: Record room ID in rollback stack
        rollbackStack.push(booking.roomId);

        // Step 2: Restore inventory
        inventoryManager.releaseRoom(booking.roomType, booking.roomId);

        // Step 3: Update booking state
        booking.isActive = false;

        // Step 4: Log cancellation
        System.out.println("Booking Cancelled Successfully: " + bookingId);
        System.out.println("Room Released: " + rollbackStack.peek());
    }

    public void showBookings() {
        System.out.println("\nBooking History:");
        for (Booking b : bookings.values()) {
            System.out.println(
                    b.bookingId + " | " + b.roomType + " | " + b.roomId +
                            " | Status: " + (b.isActive ? "ACTIVE" : "CANCELLED")
            );
        }
    }
}

// Main Class
public class UseCase10BookingCancellation {
    public static void main(String[] args) {
        InventoryManager inventory = new InventoryManager();
        BookingService bookingService = new BookingService(inventory);

        // Create bookings
        bookingService.createBooking("B101", "Deluxe");
        bookingService.createBooking("B102", "Suite");

        inventory.displayInventory();

        // Cancel booking
        bookingService.cancelBooking("B101");

        inventory.displayInventory();

        // Attempt invalid cancellations
        bookingService.cancelBooking("B101"); // already cancelled
        bookingService.cancelBooking("B999"); // non-existent

        bookingService.showBookings();
    }
}
