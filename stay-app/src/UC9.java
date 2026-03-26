

import java.util.HashMap;
import java.util.Map;


class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}



// Validator class
class Booking{
    private Map<String, Integer> inventory;

    public Booking() {
        inventory = new HashMap<>();
        inventory.put("Deluxe", 2);
        inventory.put("Suite", 1);
        inventory.put("Standard", 3);
    }

    public void validate(Reservation reservation) throws InvalidBookingException {
        String roomType = reservation.getRoomType();

        // Validate room type
        if (!inventory.containsKey(roomType)) {
            throw new InvalidBookingException("Invalid room type: " + roomType);
        }

        // Validate inventory availability
        int available = inventory.get(roomType);
        if (available <= 0) {
            throw new InvalidBookingException("No rooms available for type: " + roomType);
        }

        // Reduce inventory if valid
        inventory.put(roomType, available - 1);
    }
}

// Main class
public class UC9{
    public static void main(String[] args) {
        BookingValidator validator = new BookingValidator();

        // Test cases
        Reservation r1 = new Reservation("Alice", "Deluxe", "2026-03-26", "2026-03-28");
        Reservation r2 = new Reservation("Bob", "Suite", "2026-03-27", "2026-03-30");
        Reservation r3 = new Reservation("Charlie", "Penthouse", "2026-03-28", "2026-03-29"); // Invalid room type
        Reservation r4 = new Reservation("David", "Suite", "2026-03-29", "2026-03-31"); // Suite already booked

        processBooking(validator, r1);
        processBooking(validator, r2);
        processBooking(validator, r3);
        processBooking(validator, r4);
    }

    private static void processBooking(BookingValidator validator, Reservation reservation) {
        try {
            validator.validate(reservation);
            System.out.println("Booking confirmed: " + reservation);
        } catch (InvalidBookingException e) {
            System.out.println("Booking failed: " + e.getMessage());
        }
    }
}