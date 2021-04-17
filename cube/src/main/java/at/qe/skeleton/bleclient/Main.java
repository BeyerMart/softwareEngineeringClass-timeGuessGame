package at.qe.skeleton.bleclient;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import tinyb.BluetoothDevice;
import tinyb.BluetoothException;
import tinyb.BluetoothGattCharacteristic;
import tinyb.BluetoothGattService;
import tinyb.BluetoothManager;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

// TODO: use logging instead of System.out/System.err

/**
 * Entry point for program to search for Bluetooth devices and communicate with them
 */
@SpringBootApplication
public class Main {

	private static final boolean running = true;
	private static BluetoothGattService informationService;
	private static BluetoothGattService batteryService;
	private static BluetoothGattService timeFlipService;
	private static BluetoothGattCharacteristic facetCharacteristic;
	private static BluetoothGattCharacteristic batteryCharacteristic;

	/**
	 * This program should connect to TimeFlip devices and read the facet characteristic exposed by the devices
	 * over Bluetooth Low Energy.
	 *
	 * @param args the program arguments
	 * @throws InterruptedException if finding devices gets interrupted
	 * @see <a href="https://github.com/DI-GROUP/TimeFlip.Docs/blob/master/Hardware/BLE_device_commutication_protocol_v3.0_en.md" target="_top">BLE device communication protocol v3.0</a>
	 */
	public static void main(String[] args) throws Exception {
		//SpringBootApplication.run(Main.class, args);
		
		Set<BluetoothDevice> foundDevices = findTimeFlips();
		BluetoothDevice device = connectToTimeFlipWithBestSignal(foundDevices);
		initializeServicesAndCharacteristics(device);

		int batteryLevel = getBatteryLevel(batteryCharacteristic);

		facetCharacteristic.enableValueNotifications(new ValueNotification());
		facetCharacteristic.disableValueNotifications();
		//printAllServicesAndCharacteristics(device);

		//get current facet
		int facet = getCurrentFacet(timeFlipService);
		//batteryCharacteristic.enableValueNotifications(new ValueNotification());

		device.disconnect();
		System.exit(-1);
	}

	public static int getBatteryLevel(BluetoothGattCharacteristic batteryCharacteristic) {
		byte[] batteryRaw = batteryCharacteristic.readValue();
		String hex = String.format("%02x", batteryRaw[0]);
		int value = Integer.parseInt(hex, 16);
		return value;
        /*for (byte b : batteryRaw) {
            String hex = String.format("%02x", b);
            int value = Integer.parseInt(hex, 16);
            System.out.print(String.format("%02x", b));
            System.out.print("Battery value in percentage: " + value + "%.");
        }
        System.out.print("}\n");*/
	}

	public static void initializeServicesAndCharacteristics(BluetoothDevice device) {
		informationService = device.find("0000180a-0000-1000-8000-00805f9b34fb");
		batteryService = device.find("0000180f-0000-1000-8000-00805f9b34fb");
		timeFlipService = device.find("f1196f50-71a4-11e6-bdf4-0800200c9a66");
		if (timeFlipService == null) {
			System.err.println("This device does not have the timeflip service we are looking for.");
			device.disconnect();
			System.exit(-1);
		} else {
			System.out.println("Found (timeflip) service " + timeFlipService.getUUID());
		}
		setPassword(timeFlipService);
		facetCharacteristic = timeFlipService.find("f1196f52-71a4-11e6-bdf4-0800200c9a66");
		batteryCharacteristic = batteryService.find("00002a19-0000-1000-8000-00805f9b34fb");

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
	}

	public static BluetoothDevice connectToTimeFlipWithBestSignal(Set<BluetoothDevice> timeflipSet) throws Exception {
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
			throw new Exception("Could Not connect to device");
		}
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

	public static int getCurrentFacet(BluetoothGattService timeFlipService) {
		BluetoothGattCharacteristic facetCharacteristic = timeFlipService.find("f1196f52-71a4-11e6-bdf4-0800200c9a66");
		byte[] b = facetCharacteristic.readValue();
		int facet = b[0];
		facet--; //This is necessary, as the facets are labeled from 2 to 13.
		return facet;
	}

	public static void printAllServicesAndCharacteristics(BluetoothDevice timeflip) {
		List<BluetoothGattService> services = timeflip.getServices();
		for (BluetoothGattService service : services) {
			List<BluetoothGattCharacteristic> bluetoothGattCharacteristics = service.getCharacteristics();
			for (BluetoothGattCharacteristic characteristic : bluetoothGattCharacteristics) {
				System.out.println("Service: " + service.getUUID() + " characteristic: " + characteristic.getUUID());
			}
		}
	}
}