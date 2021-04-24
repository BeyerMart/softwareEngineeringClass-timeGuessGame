package at.qe.skeleton.bleclient;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import tinyb.BluetoothDevice;
import tinyb.BluetoothGattCharacteristic;
import tinyb.BluetoothGattService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

public class TimeFlipBluetoothTest {

    TimeCubeService timeCubeService;
    BluetoothDevice timeCube;
    BluetoothGattService informationService;
    BluetoothGattService batteryService;
    BluetoothGattService timeFlipService;
    BluetoothGattCharacteristic facetCharacteristic;
    BluetoothGattCharacteristic batteryCharacteristic;
    BluetoothService bluetoothService;
    //To test this class one needs to follow the readme.md and make sure your tinyb.jar is in the cube/lib folder.

    @Before
    public void init() {
        timeCube = Mockito.mock(BluetoothDevice.class);
        informationService = Mockito.mock(BluetoothGattService.class);
        batteryService = Mockito.mock(BluetoothGattService.class);
        timeFlipService = Mockito.mock(BluetoothGattService.class);
        facetCharacteristic = Mockito.mock(BluetoothGattCharacteristic.class);
        batteryCharacteristic = Mockito.mock(BluetoothGattCharacteristic.class);
        bluetoothService = Mockito.mock(BluetoothService.class);
        //List<BluetoothDevice> mockDevices = utilMockDevices();
        //Set<BluetoothDevice> mockDevicesSet = new HashSet<BluetoothDevice>(mockDevices);
        //when(bluetoothService.findTimeFlips()).thenReturn(mockDevicesSet);
        //when(bluetoothService.connectToTimeFlipWithBestSignal(mockDevicesSet)).thenReturn(mockDevices.get(0));
        //when(timeCube.find("0000180a-0000-1000-8000-00805f9b34fb")).thenReturn(informationService);
        //when(timeCube.find("0000180f-0000-1000-8000-00805f9b34fb")).thenReturn(batteryService);
        //when(timeCube.find("f1196f50-71a4-11e6-bdf4-0800200c9a66")).thenReturn(timeFlipService);
        //when(timeFlipService.find("f1196f52-71a4-11e6-bdf4-0800200c9a66")).thenReturn(facetCharacteristic);
        //when(batteryService.find("00002a19-0000-1000-8000-00805f9b34fb")).thenReturn(batteryCharacteristic);
        //when(timeCubeService.setPassword(timeFlipService)).thenReturn(true);
    }

    @Test
    public void testGetBatteryLevel() {
        byte[] value = new byte[1];
        value[0] = 69;
        when(batteryCharacteristic.readValue()).thenReturn(value);
        timeCubeService = new TimeCubeService(timeCube, informationService, batteryService, timeFlipService, facetCharacteristic, batteryCharacteristic);

        Assert.assertEquals(69, timeCubeService.getBatteryLevel());
    }


    private List<BluetoothDevice> utilMockDevices() {
        List<BluetoothDevice> mockDevices = new ArrayList<>();

        BluetoothDevice betterTimeFlip = Mockito.mock(BluetoothDevice.class);
        when(betterTimeFlip.getName()).thenReturn("timeflip");
        when(betterTimeFlip.getRSSI()).thenReturn((short) -20);
        when(betterTimeFlip.connect()).thenReturn(true);
        mockDevices.add(betterTimeFlip);

        BluetoothDevice worseTimeFlip = Mockito.mock(BluetoothDevice.class);
        when(worseTimeFlip.getName()).thenReturn("timeflip2");
        when(worseTimeFlip.getRSSI()).thenReturn((short) -44);
        mockDevices.add(worseTimeFlip);

        return mockDevices;
    }


}
