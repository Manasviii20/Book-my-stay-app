import java.io.*;
import java.util.*;

// Serializable class to represent booking requests
class BookingRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String guestName;

    public BookingRequest(String guestName) {
        this.guestName = guestName;
    }

    public String getGuestName() {
        return guestName;
    }

    @Override
    public String toString() {
        return "BookingRequest{guest='" + guestName + "'}";
    }
}

// Serializable class to represent hotel inventory
class HotelInventory implements Serializable {
    private static final long serialVersionUID = 1L;
    private int availableRooms;
    private final List<String> allocatedGuests;

    public HotelInventory(int rooms) {
        this.availableRooms = rooms;
        this.allocatedGuests = new ArrayList<>();
    }

    public synchronized boolean allocateRoom(String guestName) {
        if (availableRooms > 0) {
            availableRooms--;
            allocatedGuests.add(guestName);
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

    public List<String> getAllocatedGuests() {
        return allocatedGuests;
    }

    @Override
    public String toString() {
        return "HotelInventory{availableRooms=" + availableRooms + ", allocatedGuests=" + allocatedGuests + "}";
    }
}

// Persistence service for saving and restoring state
class PersistenceService {
    private final String filename;

    public PersistenceService(String filename) {
        this.filename = filename;
    }

    public void saveState(HotelInventory inventory) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(inventory);
            System.out.println("System state saved to " + filename);
        } catch (IOException e) {
            System.err.println("Error saving state: " + e.getMessage());
        }
    }

    public HotelInventory restoreState() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            HotelInventory inventory = (HotelInventory) ois.readObject();
            System.out.println("System state restored from " + filename);
            return inventory;
        } catch (FileNotFoundException e) {
            System.out.println("No persistence file found. Starting fresh.");
            return new HotelInventory(5); // default rooms if no file
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error restoring state: " + e.getMessage());
            return new HotelInventory(5); // safe fallback
        }
    }
}

public class UseCase12DataPersistenceRecovery {
    public static void main(String[] args) {
        String persistenceFile = "hotel_state.ser";
        PersistenceService persistenceService = new PersistenceService(persistenceFile);

        // Restore state if available
        HotelInventory inventory = persistenceService.restoreState();

        // Simulate new booking requests
        String[] guests = {"Alice", "Bob", "Charlie", "David", "Eva"};
        for (String guest : guests) {
            inventory.allocateRoom(guest);
        }

        // Save state before shutdown
        persistenceService.saveState(inventory);

        System.out.println("Final state before shutdown: " + inventory);
    }
}