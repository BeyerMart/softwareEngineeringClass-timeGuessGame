package at.qe.skeleton.bleclient;

import at.qe.skeleton.controller.WebSocketConnection;
import at.qe.skeleton.model.Cube;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.LoggerFactory;
import tinyb.BluetoothNotification;

import java.util.logging.Logger;

public class BatteryValueNotification implements BluetoothNotification<byte[]> {
    private static final Logger logger = (Logger) LoggerFactory.getLogger(BatteryValueNotification.class);
    private WebSocketConnection connection;
    private Cube cube;

    public BatteryValueNotification(WebSocketConnection connection, Cube cube) {
        this.connection = connection;
        this.cube = cube;
    }

    public void run(byte[] batteryRaw) {

        String hex = String.format("%02x", batteryRaw[0]);
        int batteryLevel = Integer.parseInt(hex, 16);
        logger.info("ValueNotification new Batterylevel: " + batteryLevel);
        cube.setBatteryLevel(batteryLevel);
        try {
            connection.sendBatteryNotification(cube);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }
}
