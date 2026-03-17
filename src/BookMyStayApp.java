import java.util.*;

class AddOnService {
    String serviceName;
    double cost;

    public AddOnService(String serviceName, double cost) {
        this.serviceName = serviceName;
        this.cost = cost;
    }
}

class AddOnServiceManager {

    // ReservationID -> List of services
    private Map<String, List<AddOnService>> reservationServices = new HashMap<>();

    public void addService(String reservationId, AddOnService service) {

        reservationServices
                .computeIfAbsent(reservationId, k -> new ArrayList<>())
                .add(service);
    }

    public double calculateTotalCost(String reservationId) {

        List<AddOnService> services = reservationServices.getOrDefault(reservationId, new ArrayList<>());

        double total = 0;

        for (AddOnService service : services) {
            total += service.cost;
        }

        return total;
    }

    public void printServices(String reservationId) {

        List<AddOnService> services = reservationServices.getOrDefault(reservationId, new ArrayList<>());

        System.out.println("Add-On Service Selection");
        System.out.println("Reservation ID: " + reservationId);

        System.out.println("Total Add-On Cost: " + calculateTotalCost(reservationId));
    }
}

public class BookMyStayApp {

    public static void main(String[] args) {

        AddOnServiceManager manager = new AddOnServiceManager();

        String reservationId = "Single-1";

        AddOnService breakfast = new AddOnService("Breakfast", 500);
        AddOnService airportPickup = new AddOnService("Airport Pickup", 1000);

        manager.addService(reservationId, breakfast);
        manager.addService(reservationId, airportPickup);

        manager.printServices(reservationId);
    }
}