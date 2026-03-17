import java.util.*;

    class BookingRequest {
        String guestName;
        String roomType;

        public BookingRequest(String guestName, String roomType) {
            this.guestName = guestName;
            this.roomType = roomType;
        }
    }

    class InventoryService {

        private Map<String, Integer> inventory = new HashMap<>();

        public InventoryService() {
            inventory.put("Single", 2);
            inventory.put("Suite", 1);
        }

        public boolean isAvailable(String roomType) {
            return inventory.getOrDefault(roomType, 0) > 0;
        }

        public void decrement(String roomType) {
            inventory.put(roomType, inventory.get(roomType) - 1);
        }
    }

    class BookingService {

        private Map<String, Set<String>> allocatedRooms = new HashMap<>();
        private Map<String, Integer> roomCounters = new HashMap<>();
        private InventoryService inventoryService;

        public BookingService(InventoryService inventoryService) {
            this.inventoryService = inventoryService;
        }

        public void processBookings(Queue<BookingRequest> queue) {

            System.out.println("Room allocation processing");

            while (!queue.isEmpty()) {

                BookingRequest request = queue.poll();
                String roomType = request.roomType;

                if (!inventoryService.isAvailable(roomType)) {
                    System.out.println("No rooms available for " + request.guestName);
                    continue;
                }

                int nextId = roomCounters.getOrDefault(roomType, 0) + 1;
                roomCounters.put(roomType, nextId);

                String roomId = roomType + "-" + nextId;

                allocatedRooms
                        .computeIfAbsent(roomType, k -> new HashSet<>())
                        .add(roomId);

                inventoryService.decrement(roomType);

                System.out.println("Booking confirmed for guest: "
                        + request.guestName + ", Room ID: " + roomId);
            }
        }
    }

    public class BookMyStayApp {

        public static void main(String[] args) {

            Queue<BookingRequest> bookingQueue = new LinkedList<>();

            bookingQueue.add(new BookingRequest("Abhi", "Single"));
            bookingQueue.add(new BookingRequest("Subha", "Single"));
            bookingQueue.add(new BookingRequest("Vanmathi", "Suite"));

            InventoryService inventoryService = new InventoryService();
            BookingService bookingService = new BookingService(inventoryService);

            bookingService.processBookings(bookingQueue);
        }
    }