import java.util.*;
import java.util.concurrent.*;

// Booking Request Model
class BookingRequest {
    String guestName;
    String roomType;

    public BookingRequest(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }
}

// Inventory Service with synchronized updates
class InventoryService {

    private final Map<String, Integer> inventory = new HashMap<>();

    public InventoryService() {
        inventory.put("Single", 2);
        inventory.put("Double", 2);
        inventory.put("Suite", 1);
    }

    // Synchronized method for thread-safe availability check
    public synchronized boolean isAvailable(String roomType) {
        return inventory.getOrDefault(roomType, 0) > 0;
    }

    // Synchronized method for thread-safe decrement
    public synchronized boolean decrement(String roomType) {
        int count = inventory.getOrDefault(roomType, 0);
        if (count > 0) {
            inventory.put(roomType, count - 1);
            return true;
        }
        return false;
    }
}

// Booking Service with thread-safe processing
class BookingService {

    private final InventoryService inventoryService;
    private final Map<String, Set<String>> allocatedRooms = new HashMap<>();
    private final Map<String, Integer> roomCounters = new HashMap<>();

    public BookingService(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    // Process a booking request atomically
    public void processBooking(BookingRequest request) {
        synchronized (this) {
            String roomType = request.roomType;

            if (!inventoryService.isAvailable(roomType)) {
                System.out.println("No rooms available for guest: " + request.guestName);
                return;
            }

            boolean decremented = inventoryService.decrement(roomType);
            if (!decremented) {
                System.out.println("Inventory changed before allocation for guest: " + request.guestName);
                return;
            }

            int nextId = roomCounters.getOrDefault(roomType, 0) + 1;
            roomCounters.put(roomType, nextId);

            String roomId = roomType + "-" + nextId;

            allocatedRooms.computeIfAbsent(roomType, k -> new HashSet<>()).add(roomId);

            System.out.println("Booking confirmed for guest: " + request.guestName + ", Room ID: " + roomId);
        }
    }
}

// Main class to simulate concurrent booking requests
public class BookMyStayApp {

    public static void main(String[] args) throws InterruptedException {

        InventoryService inventoryService = new InventoryService();
        BookingService bookingService = new BookingService(inventoryService);

        // Shared booking queue simulated by a thread-safe queue
        BlockingQueue<BookingRequest> bookingQueue = new LinkedBlockingQueue<>();

        // Add booking requests concurrently
        bookingQueue.add(new BookingRequest("Alice", "Single"));
        bookingQueue.add(new BookingRequest("Bob", "Single"));
        bookingQueue.add(new BookingRequest("Charlie", "Suite"));
        bookingQueue.add(new BookingRequest("David", "Single"));
        bookingQueue.add(new BookingRequest("Eve", "Double"));
        bookingQueue.add(new BookingRequest("Frank", "Double"));
        bookingQueue.add(new BookingRequest("Grace", "Suite"));

        // Create a fixed thread pool
        ExecutorService executor = Executors.newFixedThreadPool(3);

        Runnable bookingProcessor = () -> {
            while (true) {
                BookingRequest request = null;
                try {
                    request = bookingQueue.poll(1, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
                if (request == null) break;  // No more requests

                bookingService.processBooking(request);
            }
        };

        // Start 3 concurrent threads processing the booking queue
        for (int i = 0; i < 3; i++) {
            executor.submit(bookingProcessor);
        }

        // Shutdown executor and wait for threads to finish
        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);

        System.out.println("All booking requests processed.");
    }
}