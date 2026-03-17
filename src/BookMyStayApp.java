import java.util.*;

// Reservation Model
class Reservation {
    String guestName;
    String roomType;
    String roomId;
    boolean cancelled;

    public Reservation(String guestName, String roomType, String roomId) {
        this.guestName = guestName;
        this.roomType = roomType;
        this.roomId = roomId;
        this.cancelled = false;
    }
}

// Inventory Service
class InventoryService {

    private Map<String, Integer> inventory = new HashMap<>();

    public InventoryService() {
        inventory.put("Single", 2);
        inventory.put("Double", 2);
        inventory.put("Suite", 1);
    }

    public void decrement(String roomType) {
        inventory.put(roomType, inventory.get(roomType) - 1);
    }

    public void increment(String roomType) {
        inventory.put(roomType, inventory.get(roomType) + 1);
    }
}

// Booking History
class BookingHistory {

    private List<Reservation> reservations = new ArrayList<>();

    public void addReservation(Reservation reservation) {
        reservations.add(reservation);
    }

    public Reservation findReservation(String roomId) {
        for (Reservation r : reservations) {
            if (r.roomId.equals(roomId)) {
                return r;
            }
        }
        return null;
    }
}

// Cancellation Service
class CancellationService {

    private Stack<String> rollbackStack = new Stack<>();
    private InventoryService inventoryService;
    private BookingHistory history;

    public CancellationService(InventoryService inventoryService, BookingHistory history) {
        this.inventoryService = inventoryService;
        this.history = history;
    }

    public void cancelReservation(String roomId) {

        Reservation reservation = history.findReservation(roomId);

        if (reservation == null) {
            System.out.println("Cancellation failed: Reservation not found.");
            return;
        }

        if (reservation.cancelled) {
            System.out.println("Cancellation failed: Booking already cancelled.");
            return;
        }

        // Record rollback
        rollbackStack.push(reservation.roomId);

        // Restore inventory
        inventoryService.increment(reservation.roomType);

        // Update reservation state
        reservation.cancelled = true;

        System.out.println("Booking cancelled successfully for Room ID: " + roomId);
    }

    public void printRollbackStack() {
        System.out.println("Rollback Stack: " + rollbackStack);
    }
}

// Main System
public class BookMyStayApp {

    public static void main(String[] args) {

        InventoryService inventory = new InventoryService();
        BookingHistory history = new BookingHistory();

        // Simulated confirmed bookings
        Reservation r1 = new Reservation("Abhi", "Single", "Single-1");
        Reservation r2 = new Reservation("Subha", "Single", "Single-2");
        Reservation r3 = new Reservation("Vanmathi", "Suite", "Suite-1");

        history.addReservation(r1);
        history.addReservation(r2);
        history.addReservation(r3);

        // Inventory already reduced when booking happened
        inventory.decrement("Single");
        inventory.decrement("Single");
        inventory.decrement("Suite");

        CancellationService cancelService =
                new CancellationService(inventory, history);

        // Guest cancels booking
        cancelService.cancelReservation("Single-2");

        // Show rollback stack
        cancelService.printRollbackStack();
    }
}