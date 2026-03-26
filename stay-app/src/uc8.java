

import java.util.ArrayList;
import java.util.List;

class Reserve {
    private String guestName;
    private String roomType;
    private String checkInDate;
    private String checkOutDate;

    public Reserve(String guestName, String roomType, String checkInDate, String checkOutDate) {
        this.guestName = guestName;
        this.roomType = roomType;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
    }

    @Override
    public String toString() {
        return "Reservation [Guest=" + guestName + ", Room=" + roomType +
                ", CheckIn=" + checkInDate + ", CheckOut=" + checkOutDate + "]";
    }
}

class BookingHistory {
    private List<Reservation> reservations = new ArrayList<>();

    public void addReservation(Reservation reservation) {
        reservations.add(reservation);
    }

    public List<Reservation> getReservations() {
        return reservations;
    }
}

class BookingReportService {
    public void generateReport(List<Reservation> reservations) {
        System.out.println("\n--- Booking Report      ---");
        System.out.println("Total Reservations: " + reservations.size());
        for (Reservation r : reservations) {
            System.out.println(r);
        }
        System.out.println("--- End of Report     ---\n");
    }
}

class uc8 {
    public static void main(String[] args) {
        BookingHistory history = new BookingHistory();
        BookingReportService reportService = new BookingReportService();

        // Add some sample reservations
        history.addReservation(new Reservation("Alice", "Deluxe"));
        history.addReservation(new Reservation("Bob", "Suite"));
        history.addReservation(new Reservation("Charlie", "Standard"));

        // Show booking history
        System.out.println("Booking History:");
        for (Reservation r : history.getReservations()) {
            System.out.println(r);
        }

        // Generate report
        reportService.generateReport(history.getReservations());
    }
}