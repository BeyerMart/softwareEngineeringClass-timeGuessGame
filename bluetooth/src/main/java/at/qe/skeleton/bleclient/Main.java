package at.qe.skeleton.bleclient;

import tinyb.*;

import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

// TODO: use logging instead of System.out/System.err

/**
 * Entry point for program to search for Bluetooth devices and communicate with them
 */
public final class Main {
    static boolean running = true;
    static BluetoothGattService informationService;
    static BluetoothGattService batteryService;
    static BluetoothGattService timeFlipService;

    private Main() {
    }

    /**
     * This program should connect to TimeFlip devices and read the facet characteristic exposed by the devices
     * over Bluetooth Low Energy.
     *
     * @param args the program arguments
     * @throws InterruptedException if finding devices gets interrupted
     * @see <a href="https://github.com/DI-GROUP/TimeFlip.Docs/blob/master/Hardware/BLE_device_commutication_protocol_v3.0_en.md" target="_top">BLE device communication protocol v3.0</a>
     */
    public static void main(String[] args) throws InterruptedException {
        Set<BluetoothDevice> foundDevices = findTimeFlips();
        //find all services
        //BluetoothDevice timeflip = foundDevices.stream().findFirst().get();

        for (BluetoothDevice device : foundDevices) {
            //  System.out.println("Found " + findDeviceName + " device with address " + device.getAddress() + " and RSSI " + device.getRSSI());
            device.enableConnectedNotifications(new ConnectedNotification());
            if (device.connect()) {
                System.out.println("Connection established");
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
                });
                // TODO: read from device

                printAllServicesAndCharacteristics(device);
                informationService = device.find("0000180a-0000-1000-8000-00805f9b34fb");
                batteryService = device.find("0000180f-0000-1000-8000-00805f9b34fb");
                timeFlipService = device.find("f1196f50-71a4-11e6-bdf4-0800200c9a66");
                if (timeFlipService == null) {
                    System.err.println("This device does not have the timeflip service we are looking for.");
                    device.disconnect();
                    System.exit(-1);
                }
                setPassword(timeFlipService);

                BluetoothGattCharacteristic facetCharacteristic = timeFlipService.find("f1196f52-71a4-11e6-bdf4-0800200c9a66");
                facetCharacteristic.enableValueNotifications(new ValueNotification());
                BluetoothGattCharacteristic batteryCharacteristic = batteryService.find("00002a19-0000-1000-8000-00805f9b34fb");
                List<BluetoothGattDescriptor> batteryDescriptors = batteryCharacteristic.getDescriptors();
                for (BluetoothGattDescriptor batteryDescriptor : batteryDescriptors) {
                    System.out.println("BatteryDescriptors  UUID: " + batteryDescriptor.getUUID() + " Value: " + batteryDescriptor.readValue());
                }
                System.out.println("Battery value: ");
                byte[] batteryRaw = batteryCharacteristic.readValue();
                System.out.print("battery raw = {");
                for (byte b : batteryRaw) {
                    String hex = String.format("%02x", b);
                    int value = Integer.parseInt(hex, 16);
                    System.out.print(String.format("%02x", b));
                    System.out.print("Battery value in percentage: " + value + "%.");
                }
                System.out.print("}\n");


                System.out.println("Found (timeflip) service " + timeFlipService.getUUID());
                timeFlipService.getCharacteristics().forEach(c -> System.out.println(c.getUUID()));
                //BluetoothGattService batteryService = device.find("180F");

                //BluetoothGattService tempService = getService(sensor, "f000aa00-0451-4000-b000-000000000000");


                //get current facet
                for (int i = 0; i < 1000; i++) {
                    getCurrentFacet(timeFlipService);
                    Thread.sleep(100);
                }
                //batteryCharacteristic.enableValueNotifications(new ValueNotification());

                device.disconnect();
                System.exit(-1);
            } else {
                System.out.println("Connection not established - trying next one");
            }
        }
    }

    //     * @throws InterruptedException if finding devices gets interrupted
    public static Set<BluetoothDevice> findTimeFlips() throws InterruptedException {
        BluetoothManager manager = BluetoothManager.getBluetoothManager();

        final String findDeviceName = "TimeFlip";

        final boolean discoveryStarted = manager.startDiscovery();
        System.out.println("The discovery started: " + (discoveryStarted ? "true" : "false"));

        FindDevicesManager findDevicesManager = new FindDevicesManager(findDeviceName);
        final boolean findDevicesSuccess = findDevicesManager.findDevices(manager);

        try {
            manager.stopDiscovery();
        } catch (BluetoothException e) {
            System.err.println("Discovery could not be stopped.");
        }

        System.out.println("All found devices:");
        manager.getDevices().forEach(d -> System.out.println(d.getAddress() + " - " + d.getName() + " (" + d.getRSSI() + ")"));

        if (!findDevicesSuccess) {
            System.err.println("No " + findDeviceName + " devices found during discovery.");
            System.exit(-1);
        }
        return findDevicesManager.getFoundDevices();
        //Set<BluetoothDevice> foundDevices = findDevicesManager.getFoundDevices();
        //System.out.println("Found " + foundDevices.size() + " " + findDeviceName + " device(s).");
        //return foundDevices;
    }

    private static boolean setPassword(BluetoothGattService timeFlipService) {
        BluetoothGattCharacteristic passwordCharacteristic = timeFlipService.find("f1196f57-71a4-11e6-bdf4-0800200c9a66");
        byte[] password = {0x30, 0x30, 0x30, 0x30, 0x30, 0x30};
        if (passwordCharacteristic.writeValue(password)) {
            System.out.println("New password was written.");
            return true;
        } else {
            System.out.println("Error writing password.");
            return false;
        }
    }

    private static void getCurrentFacet(BluetoothGattService timeFlipService) {
        BluetoothGattCharacteristic facetCharacteristic = timeFlipService.find("f1196f52-71a4-11e6-bdf4-0800200c9a66");
        byte[] b = facetCharacteristic.readValue();
        int facet = b[0];
        facet--; //This is necessary, as the facets are labeled from 2 to 13.
        System.out.println(facet);

    }

    private static void printAllServicesAndCharacteristics(BluetoothDevice timeflip) {
        List<BluetoothGattService> services = timeflip.getServices();
        for (BluetoothGattService service : services) {
            List<BluetoothGattCharacteristic> bluetoothGattCharacteristics = service.getCharacteristics();
            for (BluetoothGattCharacteristic characteristic : bluetoothGattCharacteristics) {
                System.out.println("Service: " + service.getUUID() + " characteristic: " + characteristic.getUUID());
            }
        }
    }
}