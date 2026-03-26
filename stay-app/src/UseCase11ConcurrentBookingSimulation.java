import java.util.*;
import java.util.concurrent.*;

// Represents a booking request from a guest
class BookingRequest {
    private final String guestName;

    public BookingRequest(String guestName) {
        this.guestName = guestName;
    }

    public String getGuestName() {
        return guestName;
    }
}

// Represents the hotel inventory
class HotelInventory {
    private int availableRooms;

    public HotelInventory(int rooms) {
        this.availableRooms = rooms;
    }

    // Critical section: synchronized to prevent race conditions
    public synchronized boolean allocateRoom(String guestName) {
        if (availableRooms > 0) {
            availableRooms--;
            System.out.println("Room allocated to " + guestName + ". Remaining rooms: " + availableRooms);
            return true;
        } else {
            System.out.println("No rooms available for " + guestName);
            return false;
        }
    }

    public int getAvailableRooms() {
        return availableRooms;
    }
}

// Processor that handles booking requests concurrently
class ConcurrentBookingProcessor implements Runnable {
    private final BlockingQueue<BookingRequest> bookingQueue;
    private final HotelInventory inventory;

    public ConcurrentBookingProcessor(BlockingQueue<BookingRequest> bookingQueue, HotelInventory inventory) {
        this.bookingQueue = bookingQueue;
        this.inventory = inventory;
    }

    @Override
    public void run() {
        try {
            while (true) {
                BookingRequest request = bookingQueue.poll(1, TimeUnit.SECONDS);
                if (request == null) {
                    break; // Exit when no more requests
                }
                inventory.allocateRoom(request.getGuestName());
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

public class UseCase11ConcurrentBookingSimulation {
    public static void main(String[] args) throws InterruptedException {
        int totalRooms = 5;
        HotelInventory inventory = new HotelInventory(totalRooms);

        // Shared booking queue
        BlockingQueue<BookingRequest> bookingQueue = new LinkedBlockingQueue<>();

        // Simulate multiple guests submitting requests
        String[] guests = {"Alice", "Bob", "Charlie", "David", "Eva", "Frank", "Grace"};
        for (String guest : guests) {
            bookingQueue.add(new BookingRequest(guest));
        }

        // Create multiple threads to process bookings
        int processorCount = 3;
        Thread[] processors = new Thread[processorCount];
        for (int i = 0; i < processorCount; i++) {
            processors[i] = new Thread(new ConcurrentBookingProcessor(bookingQueue, inventory));
            processors[i].start();
        }

        // Wait for all processors to finish
        for (Thread processor : processors) {
            processor.join();
        }

        System.out.println("Final available rooms: " + inventory.getAvailableRooms());
    }
}