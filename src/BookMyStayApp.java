import java.io.*;
import java.util.*;

// Serializable Reservation model
class Reservation implements Serializable {
    private static final long serialVersionUID = 1L;

    String guestName;
    String roomType;
    String roomId;

    public Reservation(String guestName, String roomType, String roomId) {
        this.guestName = guestName;
        this.roomType = roomType;
        this.roomId = roomId;
    }

    @Override
    public String toString() {
        return "Guest: " + guestName + ", Room Type: " + roomType + ", Room ID: " + roomId;
    }
}

// Serializable Inventory model
class Inventory implements Serializable {
    private static final long serialVersionUID = 1L;

    Map<String, Integer> roomCounts = new HashMap<>();

    public Inventory() {
        roomCounts.put("Single", 2);
        roomCounts.put("Double", 2);
        roomCounts.put("Suite", 1);
    }

    public int getCount(String roomType) {
        return roomCounts.getOrDefault(roomType, 0);
    }

    public void decrement(String roomType) {
        roomCounts.put(roomType, getCount(roomType) - 1);
    }

    public void increment(String roomType) {
        roomCounts.put(roomType, getCount(roomType) + 1);
    }

    @Override
    public String toString() {
        return roomCounts.toString();
    }
}

// System state container for serialization
class SystemState implements Serializable {
    private static final long serialVersionUID = 1L;

    List<Reservation> bookingHistory;
    Inventory inventory;

    public SystemState(List<Reservation> bookingHistory, Inventory inventory) {
        this.bookingHistory = bookingHistory;
        this.inventory = inventory;
    }
}

// Persistence service handling save/load
class PersistenceService {

    private static final String FILE_PATH = "system_state.ser";

    public void saveState(SystemState state) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            out.writeObject(state);
            System.out.println("System state saved successfully.");
        } catch (IOException e) {
            System.err.println("Failed to save system state: " + e.getMessage());
        }
    }

    public SystemState loadState() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            System.out.println("No saved state found, starting fresh.");
            return null;
        }
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            Object obj = in.readObject();
            if (obj instanceof SystemState) {
                System.out.println("System state loaded successfully.");
                return (SystemState) obj;
            } else {
                System.err.println("Invalid system state data.");
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Failed to load system state: " + e.getMessage());
        }
        return null;
    }
}

// Main booking system
class PersistentBookingSystem {

    private List<Reservation> bookingHistory = new ArrayList<>();
    private Inventory inventory = new Inventory();
    private PersistenceService persistenceService = new PersistenceService();
    private int roomCounter = 0;

    // Start system and restore state
    public void start() {
        SystemState restoredState = persistenceService.loadState();
        if (restoredState != null) {
            bookingHistory = restoredState.bookingHistory;
            inventory = restoredState.inventory;
            roomCounter = bookingHistory.size(); // simple room ID counter
        }

        System.out.println("\nCurrent Booking History:");
        for (Reservation r : bookingHistory) {
            System.out.println(r);
        }
        System.out.println("Current Inventory: " + inventory);
    }

    // Add a new booking
    public void addBooking(String guestName, String roomType) {
        if (inventory.getCount(roomType) <= 0) {
            System.out.println("No rooms available for type: " + roomType);
            return;
        }
        roomCounter++;
        String roomId = roomType + "-" + roomCounter;
        Reservation res = new Reservation(guestName, roomType, roomId);
        bookingHistory.add(res);
        inventory.decrement(roomType);
        System.out.println("Booking added: " + res);
    }

    // Shutdown and save state
    public void shutdown() {
        SystemState state = new SystemState(bookingHistory, inventory);
        persistenceService.saveState(state);
    }
}

// Main class to run the system
public class BookMyStayApp {

    public static void main(String[] args) {
        PersistentBookingSystem system = new PersistentBookingSystem();

        // Load previous state
        system.start();

        // Add new bookings
        system.addBooking("Alice", "Single");
        system.addBooking("Bob", "Double");
        system.addBooking("Charlie", "Suite");

        // Save state on shutdown
        system.shutdown();
    }
}