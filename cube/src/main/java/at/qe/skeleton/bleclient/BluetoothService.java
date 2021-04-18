package at.qe.skeleton.bleclient;

import org.sputnikdev.bluetooth.manager.BluetoothFatalException;
import tinyb.BluetoothDevice;
import tinyb.BluetoothException;
import tinyb.BluetoothManager;

import java.util.Comparator;
import java.util.Set;

public class BluetoothService {

    public static Set<BluetoothDevice> findTimeFlips() {
        BluetoothManager manager = BluetoothManager.getBluetoothManager();

        final String findDeviceName = "TimeFlip";

        final boolean discoveryStarted = manager.startDiscovery();
        System.out.println("The discovery started: " + (discoveryStarted ? "true" : "false"));

        FindDevicesManager findDevicesManager = new FindDevicesManager(findDeviceName);
        boolean findDevicesSuccess;
        try {
            findDevicesSuccess = findDevicesManager.findDevices(manager);
        } catch (InterruptedException e) {
            findDevicesSuccess = false;
            e.printStackTrace();
        }

        try {
            manager.stopDiscovery();
        } catch (BluetoothException e) {
            System.err.println("Discovery could not be stopped.");
        }

        System.out.println("All found devices:");
        manager.getDevices().forEach(d -> System.out.println(d.getAddress() + " - " + d.getName() + " (" + d.getRSSI() + ")"));

        if (!findDevicesSuccess) {
            System.err.println("No " + findDeviceName + " devices found during discovery.");
            //System.exit(-1);
        }
        return findDevicesManager.getFoundDevices();
    }

    public static BluetoothDevice connectToTimeFlipWithBestSignal(Set<BluetoothDevice> timeflipSet) {
        System.out.println(timeflipSet.stream().findFirst().get().getName());
        BluetoothDevice device = timeflipSet.stream().max(Comparator.comparing(BluetoothDevice::getRSSI)).get();
        device.enableConnectedNotifications(new ConnectedNotification());
        if (device.connect()) {
            /*
            Lock lock = new ReentrantLock();
            Condition cv = lock.newCondition();

            Runtime.getRuntime().addShutdownHook(new Thread() {
                public void run() {
                    running = false;
                    lock.lock();
                    try {
                        cv.signalAll();
                    } finally {
                        lock.unlock();
                    }
                }
            }); */
            return device;
        } else {
            System.out.println("Connection with best timeflip (based on signal) not established.");
            throw new BluetoothFatalException("No Connection");
        }
    }
}
