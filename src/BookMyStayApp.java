 abstract class Room {
        protected int numberOfBeds;
        protected int sizeSqFt;
        protected double price;
        protected String roomTypeName;

        public Room(String name, int beds, int size, double price) {
            this.roomTypeName = name;
            this.numberOfBeds = beds;
            this.sizeSqFt = size;
            this.price = price;
        }

        public void displayDetails() {
            System.out.println("--- " + roomTypeName + " ---");
            System.out.println("Beds: " + numberOfBeds);
            System.out.println("Size: " + sizeSqFt + " sq. ft.");
            System.out.printf("Price: $%.2f%n", price);
        }

    public class SingleRoom extends Room {
        public SingleRoom() {
            super("Single Room", 1, 250, 1500.0);
        }
    }

    public class DoubleRoom extends Room {
        public DoubleRoom() {
            super("Double Room", 2, 400, 2200.0);
        }
    }

    public class SuiteRoom extends Room {
        public SuiteRoom() {
            super("Executive Suite", 2, 800, 5000.0);
        }
    }

    public class BookMyStay {

        private static int singleAvailability = 10;
        private static int doubleAvailability = 5;
        private static int suiteAvailability = 2;

        public void main(String[] args) {
            System.out.println("HOTEL INVENTORY SYSTEM - INITIALIZED\n");

            Room mySingle = new SingleRoom();
            Room myDouble = new DoubleRoom();
            Room mySuite = new SuiteRoom();

            mySingle.displayDetails();
            System.out.println("Current Availability: " + singleAvailability + " left");
            System.out.println("-----------------------------------");

            myDouble.displayDetails();
            System.out.println("Current Availability: " + doubleAvailability + " left");
            System.out.println("-----------------------------------");

            mySuite.displayDetails();
            System.out.println("Current Availability: " + suiteAvailability + " left");
            System.out.println("-----------------------------------");

            System.out.println("\nApplication Terminated.");
        }
    }
}