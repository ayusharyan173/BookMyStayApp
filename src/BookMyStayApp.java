public class BookMyStayApp {

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

    static class SingleRoom extends Room {
        SingleRoom() {
            super(1, 2000,300);
        }
    }

    static class DoubleRoom extends Room {
        DoubleRoom() {
            super(2, 3000, 500);
        }
    }

    static class SuiteRoom extends Room {
        SuiteRoom() {
            super(3, 5000, 900);
        }
    }

    public static void main(String[] args) {

        Room single = new SingleRoom();
        Room doubleroom = new DoubleRoom();
        Room suite = new SuiteRoom();

        int singleAvailable = 5;
        int doubleAvailable = 3;
        int suiteAvailable = 2;

        System.out.println("Welcome to BookMyStay\n");

        System.out.println("Single Room:");
        single.displayDetails();
        System.out.println("Available: " + singleAvailable);

        System.out.println("\nDouble Room:");
        doubleroom.displayDetails();
        System.out.println("Available: " + doubleAvailable);

        System.out.println("\nSuite Room:");
        suite.displayDetails();
        System.out.println("Available: " + suiteAvailable);
    }
}