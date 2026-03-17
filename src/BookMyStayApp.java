import java.util.*;

class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

class BookingValidator {

    private static final Set<String> VALID_ROOM_TYPES =
            new HashSet<>(Arrays.asList("Single", "Double", "Suite"));

    public static void validateRoomType(String roomType) throws InvalidBookingException {

        if (!VALID_ROOM_TYPES.contains(roomType)) {
            throw new InvalidBookingException("Invalid room type selected.");
        }
    }
}

class InventoryService {

    private Map<String, Integer> inventory = new HashMap<>();

    public InventoryService() {
        inventory.put("Single", 2);
        inventory.put("Double", 2);
        inventory.put("Suite", 1);
    }

    public void checkAvailability(String roomType) throws InvalidBookingException {

        int count = inventory.getOrDefault(roomType, 0);

        if (count <= 0) {
            throw new InvalidBookingException("No rooms available for type: " + roomType);
        }
    }

    public void reserveRoom(String roomType) {
        inventory.put(roomType, inventory.get(roomType) - 1);
    }
}

public class BookMyStayApp {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        InventoryService inventory = new InventoryService();

        try {

            System.out.println("Booking Validation");

            System.out.print("Enter guest name: ");
            String guestName = scanner.nextLine();

            System.out.print("Enter room type (Single/Double/Suite): ");
            String roomType = scanner.nextLine();

            // Step 1: Validate input
            BookingValidator.validateRoomType(roomType);

            // Step 2: Validate inventory
            inventory.checkAvailability(roomType);

            // Step 3: Reserve room
            inventory.reserveRoom(roomType);

            System.out.println("Booking confirmed for guest: " + guestName);

        } catch (InvalidBookingException e) {

            System.out.println("Booking failed: " + e.getMessage());

        } finally {
            scanner.close();
        }
    }
}