// Main Class
public class BookMyStayApp {

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
