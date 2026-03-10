import java.util.HashMap;
import java.util.Map;

public class BookMyStayApp {

    // Abstract Room class
    static abstract class Room {
        int beds;
        double price;
        int size;

        Room(int beds, double price, int size) {
            this.beds = beds;
            this.price = price;
            this.size = size;
        }

        void displayDetails() {
            System.out.println("Beds: " + beds);
            System.out.println("Price: Rs " + price);
            System.out.println("Size: " + size + " ft");
        }
    }

    // Single Room
    static class SingleRoom extends Room {
        SingleRoom() {
            super(1, 2000, 300);
        }
    }

    // Double Room
    static class DoubleRoom extends Room {
        DoubleRoom() {
            super(2, 3000, 500);
        }
    }

    // Suite Room
    static class SuiteRoom extends Room {
        SuiteRoom() {
            super(3, 5000, 900);
        }
    }

    static class RoomInventory {


        private Map<String, Integer> roomAvailability;

        // Constructor
        public RoomInventory() {
            roomAvailability = new HashMap<>();
            initializeInventory();
        }

        private void initializeInventory() {
            roomAvailability.put("Single Room", 5);
            roomAvailability.put("Double Room", 3);
            roomAvailability.put("Suite Room", 2);
        }


        public Map<String, Integer> getRoomAvailability() {
            return roomAvailability;
        }

        public void updateAvailability(String roomType, int count) {
            roomAvailability.put(roomType, count);
        }
    }

    public static void main(String[] args) {

        Room single = new SingleRoom();
        Room doubleroom = new DoubleRoom();
        Room suite = new SuiteRoom();

        RoomInventory inventory = new RoomInventory();

        System.out.println("Single Room:");
        single.displayDetails();
        System.out.println("Available: " + inventory.getRoomAvailability().get("Single Room"));

        System.out.println("\nDouble Room:");
        doubleroom.displayDetails();
        System.out.println("Available: " + inventory.getRoomAvailability().get("Double Room"));

        System.out.println("\nSuite Room:");
        suite.displayDetails();
        System.out.println("Available: " + inventory.getRoomAvailability().get("Suite Room"));
    }
}