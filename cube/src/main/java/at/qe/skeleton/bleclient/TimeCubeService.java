package at.qe.skeleton.bleclient;

import org.sputnikdev.bluetooth.manager.BluetoothFatalException;
import tinyb.*;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class TimeCubeService {

    private final BluetoothDevice timecube;
    private final BluetoothGattService informationService;
    private final BluetoothGattService batteryService;
    private final BluetoothGattService timeFlipService;
    private final BluetoothGattCharacteristic facetCharacteristic;
    private final BluetoothGattCharacteristic batteryCharacteristic;

    // TODO: use logging instead of System.out/System.err
    /**
     * Entry point for program to search for Bluetooth devices and communicate with them
     */
    public TimeCubeService(){
        Set<BluetoothDevice> foundDevices = BluetoothService.findTimeFlips();
        BluetoothDevice device = BluetoothService.connectToTimeFlipWithBestSignal(foundDevices);
        timecube = device;
        informationService = device.find("0000180a-0000-1000-8000-00805f9b34fb");
        batteryService = device.find("0000180f-0000-1000-8000-00805f9b34fb");
        timeFlipService = device.find("f1196f50-71a4-11e6-bdf4-0800200c9a66");
        if (timeFlipService == null) {
            System.err.println("This device does not have the timeflip service we are looking for.");
            device.disconnect();
            //System.exit(-1);
        } else {
            System.out.println("Found (timeflip) service " + timeFlipService.getUUID());
        }
        //setPassword(timeFlipService);
        facetCharacteristic = timeFlipService.find("f1196f52-71a4-11e6-bdf4-0800200c9a66");
        batteryCharacteristic = batteryService.find("00002a19-0000-1000-8000-00805f9b34fb");
    }

    public TimeCubeService(BluetoothDevice timecube, BluetoothGattService informationService, BluetoothGattService batteryService, BluetoothGattService timeFlipService, BluetoothGattCharacteristic facetCharacteristic, BluetoothGattCharacteristic batteryCharacteristic){
        this.timecube = timecube;
        this.informationService = informationService;
        this.batteryService = batteryService;
        this.timeFlipService = timeFlipService;
        this.facetCharacteristic = facetCharacteristic;
        this.batteryCharacteristic  = batteryCharacteristic;
    }

    /**
     * This program should connect to TimeFlip devices and read the facet characteristic exposed by the devices
     * over Bluetooth Low Energy.
     *
     * @param args the program arguments
     * @throws InterruptedException if finding devices gets int        initializeServicesAndCharacteristics(device);
    errupted
     * @see <a href="https://github.com/DI-GROUP/TimeFlip.Docs/blob/master/Hardware/BLE_device_commutication_protocol_v3.0_en.md" target="_top">BLE device communication protocol v3.0</a>
     */
    public void main(String[] args) {
        //SpringBootApplication.run(Main.class, args);



        //int batteryLevel = getBatteryLevel();

        facetCharacteristic.enableValueNotifications(new ValueNotification());
        facetCharacteristic.disableValueNotifications();
        //printAllServicesAndCharacteristics(device);

        //get current facet
        //int facet = getCurrentFacet();
        //batteryCharacteristic.enableValueNotifications(new ValueNotification());

        //device.disconnect();
        //System.exit(-1);
    }

    //     * @throws InterruptedException if finding devices gets interrupted




    public boolean setPassword() {
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

    public int getCurrentFacet() {
        BluetoothGattCharacteristic facetCharacteristic = timeFlipService.find("f1196f52-71a4-11e6-bdf4-0800200c9a66");
        byte[] b = facetCharacteristic.readValue();
        int facet = b[0];
        facet--; //This is necessary, as the facets are labeled from 2 to 13.
        return facet;
    }

    public void printAllServicesAndCharacteristics(BluetoothDevice timeflip) {
        List<BluetoothGattService> services = timeflip.getServices();
        for (BluetoothGattService service : services) {
            List<BluetoothGattCharacteristic> bluetoothGattCharacteristics = service.getCharacteristics();
            for (BluetoothGattCharacteristic characteristic : bluetoothGattCharacteristics) {
                System.out.println("Service: " + service.getUUID() + " characteristic: " + characteristic.getUUID());
            }
        }
    }

    public int getBatteryLevel() {
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





}
