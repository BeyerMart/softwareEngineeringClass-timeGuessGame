package at.qe.skeleton.bleclient;

import at.qe.skeleton.controller.LogicController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sputnikdev.bluetooth.manager.BluetoothFatalException;
import tinyb.BluetoothDevice;
import tinyb.BluetoothException;
import tinyb.BluetoothManager;

import java.util.Comparator;
import java.util.Optional;
import java.util.Set;

public class BluetoothService {
    private static final Logger logger = LoggerFactory.getLogger(BluetoothService.class);

    public static Set<BluetoothDevice> findTimeFlips() throws RuntimeException{
        BluetoothManager manager = BluetoothManager.getBluetoothManager();

        final String findDeviceName = "TimeFlip";

        final boolean discoveryStarted = manager.startDiscovery();
        logger.info("The discovery started: " + (discoveryStarted ? "true" : "false"));

        FindDevicesManager findDevicesManager = new FindDevicesManager(findDeviceName);
        logger.info("FoundDeviceManager");
        boolean findDevicesSuccess;
        try {
            findDevicesSuccess = findDevicesManager.findDevices(manager);
        } catch (InterruptedException e) {
            logger.error("Could not find device.");
            findDevicesSuccess = false;
            e.printStackTrace();
        }

        try {
            manager.stopDiscovery();
        } catch (BluetoothException e) {
            logger.error("Discovery could not be stopped.");
        }

        logger.info("All found devices:");
        manager.getDevices().forEach(d -> logger.info(d.getAddress() + " - " + d.getName() + " (" + d.getRSSI() + ")"));

        if (!findDevicesSuccess) {
            throw new RuntimeException("No " + findDeviceName + " devices found during discovery.");
            //System.exit(-1);
        }
        return findDevicesManager.getFoundDevices();
    }

    public static BluetoothDevice connectToTimeFlipWithBestSignal(Set<BluetoothDevice> timeflipSet) throws BluetoothFatalException, BluetoothException{
        Optional<BluetoothDevice> optionalBluetoothDevice = timeflipSet.stream().max(Comparator.comparing(BluetoothDevice::getRSSI));
        BluetoothDevice device = optionalBluetoothDevice.get();
        //device.enableConnectedNotifications(new ConnectedNotification());
        boolean connected = device.connect();
        if (connected) {
            return device;
        } else {
            logger.error("Connection with best timeflip (based on signal) not established.");
            throw new BluetoothFatalException("No Connection");
        }
    }
}
