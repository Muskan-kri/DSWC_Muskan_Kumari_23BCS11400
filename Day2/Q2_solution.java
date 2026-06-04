abstract class SmartDevice {
    protected String deviceId;
    protected String deviceName;


    public SmartDevice(String deviceId, String deviceName) {
        this.deviceId = deviceId;
        this.deviceName = deviceName;
    }

    public abstract void runDiagnostic();
}

interface BatteryOperated {
    int getBatteryLevel();
    void triggerRechargeAlert();
}

class SmartLight extends SmartDevice {
    public SmartLight(String deviceId, String deviceName) {
        super(deviceId, deviceName);
    }

    public void runDiagnostic() {
        System.out.println(deviceName + " light ok");
    }
}

class SmartCamera extends SmartDevice implements BatteryOperated {
    private final int batteryLevel;

    public SmartCamera(String deviceId, String deviceName, int batteryLevel) {
        super(deviceId, deviceName);
        this.batteryLevel = batteryLevel;
    }

    public void runDiagnostic() {
        System.out.println(deviceName + " camera ok");
    }

    public int getBatteryLevel() {
        return batteryLevel;
    }

    public void triggerRechargeAlert() {
        System.out.println(deviceName + " camera recharge alert");
    }
}

class SmartLock extends SmartDevice implements BatteryOperated {
    private final int batteryLevel;

    public SmartLock(String deviceId, String deviceName, int batteryLevel) {
        super(deviceId, deviceName);
        this.batteryLevel = batteryLevel;
    }

    public void runDiagnostic() {
        System.out.println(deviceName + " lock ok");
    }

    public int getBatteryLevel() {
        return batteryLevel;
    }

    public void triggerRechargeAlert() {
        System.out.println(deviceName + " lock recharge alert");
    }
}

class HomeHub {
    public void executeNightlyRoutine(SmartDevice[] devices) {
        for (SmartDevice d : devices) {
            d.runDiagnostic();

            if (d instanceof BatteryOperated) {
                BatteryOperated b = (BatteryOperated) d;
                if (b.getBatteryLevel() < 20) {
                    b.triggerRechargeAlert();
                }
            }
        }
    }
}

public class Main {
    public static void main(String[] args) {
        SmartDevice[] devices = {
                new SmartLight("L1", "Hall"),
                new SmartCamera("C1", "Front", 15),
                new SmartLock("K1", "Main", 50)
        };

        new HomeHub().executeNightlyRoutine(devices);
    }
}