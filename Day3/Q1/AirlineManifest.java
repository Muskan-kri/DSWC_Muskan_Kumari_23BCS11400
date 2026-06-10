import java.util.*;

class Passenger {

    private String passportNumber;
    private String fullName;
    private String nationality;

    public Passenger(String passportNumber, String fullName, String nationality) {
        this.passportNumber = passportNumber;
        this.fullName = fullName;
        this.nationality = nationality;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public String getFullName() {
        return fullName;
    }

    public String getNationality() {
        return nationality;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (!(obj instanceof Passenger))
            return false;

        Passenger other = (Passenger) obj;

        return passportNumber.equals(other.passportNumber)
                && nationality.equals(other.nationality);
    }

    @Override
    public int hashCode() {
        return Objects.hash(passportNumber, nationality);
    }

    @Override
    public String toString() {
        return fullName + " [" + passportNumber + "/" + nationality + "]";
    }
}

class ManifestManager {

    private Set<Passenger> globalNoFlyList = new HashSet<>();

    private Map<String, List<Passenger>> flightRosters = new HashMap<>();

    private Map<Passenger, String> globalPassengerDirectory = new HashMap<>();

    public void addToNoFlyList(Passenger p) {
        globalNoFlyList.add(p);
        System.out.println("[NO-FLY] " + p + " added to restricted list.");
    }

    public boolean processCheckIn(String flightNumber, Passenger p) {

        if (globalNoFlyList.contains(p)) {
            System.out.println(
                    "[REJECTED] " + p
                            + " is on the No-Fly List. Check-in denied.");
            return false;
        }

        flightRosters.computeIfAbsent(
                flightNumber,
                k -> new ArrayList<>()
        );

        flightRosters.get(flightNumber).add(p);

        globalPassengerDirectory.put(p, flightNumber);

        System.out.println(
                "[CHECKED IN] " + p
                        + " → Flight " + flightNumber
        );

        return true;
    }

    public String locatePassengerFlight(Passenger p) {
        return globalPassengerDirectory.get(p);
    }

    public void printFlightRoster(String flightNumber) {

        List<Passenger> roster =
                flightRosters.getOrDefault(
                        flightNumber,
                        new ArrayList<>()
                );

        System.out.println(
                "\n--- Roster for Flight "
                        + flightNumber + " ---"
        );

        if (roster.isEmpty()) {
            System.out.println("  No passengers checked in.");
        } else {
            for (int i = 0; i < roster.size(); i++) {
                System.out.println(
                        "  " + (i + 1)
                                + ". "
                                + roster.get(i)
                );
            }
        }
    }
}

public class AirlineManifest {

    public static void main(String[] args) {

        ManifestManager manager = new ManifestManager();

        Passenger alice =
                new Passenger(
                        "P1001",
                        "Alice Johnson",
                        "US"
                );

        Passenger bob =
                new Passenger(
                        "P1002",
                        "Bob Smith",
                        "UK"
                );

        Passenger charlie =
                new Passenger(
                        "P1003",
                        "Charlie Xu",
                        "CN"
                );

        Passenger dan =
                new Passenger(
                        "P1004",
                        "Dan Patel",
                        "IN"
                );

        System.out.println(
                "===== NO-FLY LIST SETUP ====="
        );

        manager.addToNoFlyList(charlie);

        System.out.println(
                "\n===== CHECK-IN PROCESS ====="
        );

        manager.processCheckIn(
                "SK-101",
                alice
        );

        manager.processCheckIn(
                "SK-101",
                bob
        );

        manager.processCheckIn(
                "SK-202",
                dan
        );

        manager.processCheckIn(
                "SK-101",
                charlie
        );

        System.out.println(
                "\n===== FLIGHT ROSTERS ====="
        );

        manager.printFlightRoster("SK-101");
        manager.printFlightRoster("SK-202");

        System.out.println(
                "\n===== GLOBAL PASSENGER LOOKUP ====="
        );

        System.out.println(
                "Alice's flight:   "
                        + manager.locatePassengerFlight(alice)
        );

        System.out.println(
                "Bob's flight:     "
                        + manager.locatePassengerFlight(bob)
        );

        System.out.println(
                "Dan's flight:     "
                        + manager.locatePassengerFlight(dan)
        );

        System.out.println(
                "Charlie's flight: "
                        + manager.locatePassengerFlight(charlie)
        );

        System.out.println(
                "\n===== IDENTITY TEST (New Object, Same Passport) ====="
        );

        Passenger searchAlice =
                new Passenger(
                        "P1001",
                        "Alice Johnson-Williams",
                        "US"
                );

        System.out.println(
                "Lookup with new object (name changed): "
                        + manager.locatePassengerFlight(searchAlice)
        );
    }
}
