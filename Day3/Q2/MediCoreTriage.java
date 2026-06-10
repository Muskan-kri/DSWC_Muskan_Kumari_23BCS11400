import java.util.PriorityQueue;

enum TriageLevel {
    CRITICAL,
    URGENT,
    STABLE
}

class Patient implements Comparable<Patient> {

    private String name;
    private TriageLevel severity;
    private long arrivalTime;

    public Patient(String name, TriageLevel severity, long arrivalTime) {
        this.name = name;
        this.severity = severity;
        this.arrivalTime = arrivalTime;
    }

    public String getName() {
        return name;
    }

    public TriageLevel getSeverity() {
        return severity;
    }

    public long getArrivalTime() {
        return arrivalTime;
    }

    @Override
    public int compareTo(Patient other) {
        int severityCompare = this.severity.compareTo(other.severity);

        if (severityCompare != 0) {
            return severityCompare;
        }

        return Long.compare(this.arrivalTime, other.arrivalTime);
    }

    @Override
    public String toString() {
        return name + " [" + severity +
               ", arrived at T+" + arrivalTime + "ms]";
    }
}

class TriageManager {

    private PriorityQueue<Patient> waitingRoom =
            new PriorityQueue<>();

    public void admitPatient(Patient p) {
        waitingRoom.offer(p);
        System.out.println("[ADMITTED] " + p);
    }

    public Patient getNextPatient() {
        Patient next = waitingRoom.poll();

        if (next != null) {
            System.out.println("[TREATING] " + next);
        } else {
            System.out.println("[WAITING ROOM EMPTY]");
        }

        return next;
    }

    public int getQueueSize() {
        return waitingRoom.size();
    }
}

public class MediCoreTriage {

    public static void main(String[] args) {

        TriageManager triage = new TriageManager();

        System.out.println("===== MASS CASUALTY INTAKE =====\n");

        triage.admitPatient(
                new Patient("Alice", TriageLevel.STABLE, 100));
        triage.admitPatient(
                new Patient("Bob", TriageLevel.CRITICAL, 200));
        triage.admitPatient(
                new Patient("Carol", TriageLevel.URGENT, 150));
        triage.admitPatient(
                new Patient("Dan", TriageLevel.CRITICAL, 120));
        triage.admitPatient(
                new Patient("Eve", TriageLevel.URGENT, 300));
        triage.admitPatient(
                new Patient("Frank", TriageLevel.STABLE, 80));

        System.out.println("\n===== TREATMENT ORDER =====\n");

        while (triage.getQueueSize() > 0) {
            triage.getNextPatient();
        }
    }
}
