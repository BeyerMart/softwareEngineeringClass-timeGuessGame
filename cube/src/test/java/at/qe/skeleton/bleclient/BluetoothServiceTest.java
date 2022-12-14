package at.qe.skeleton.bleclient;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import tinyb.BluetoothDevice;
import tinyb.BluetoothManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.mockito.Mockito.when;

public class BluetoothServiceTest {


    @Test
    public void testConnectToTimeFlipWithBestSignal() throws Exception {
        List<BluetoothDevice> mockDevices = utilMockDevices();
        BluetoothDevice bestDevice = BluetoothService.connectToTimeFlipWithBestSignal(new HashSet<>(mockDevices));
        Assert.assertEquals(mockDevices.get(0), bestDevice);
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
