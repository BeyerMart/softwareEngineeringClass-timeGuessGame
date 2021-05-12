package at.qe.skeleton.controller;

import at.qe.skeleton.bleclient.BatteryValueNotification;
import at.qe.skeleton.bleclient.CubeCalibration;
import at.qe.skeleton.bleclient.FacetValueNotification;
import at.qe.skeleton.bleclient.TimeCubeService;
import at.qe.skeleton.model.Cube;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sputnikdev.bluetooth.manager.BluetoothFatalException;
import tinyb.BluetoothException;

import java.util.Queue;

public class LogicController {

    private static final Logger logger = LoggerFactory.getLogger(LogicController.class);
    private final ObjectMapper mapper = new ObjectMapper();

    private Cube cube;
    private WebSocketConnection connection;
    private CubeCalibration cubeCalibration;

    public LogicController(WebSocketConnection connection, CubeCalibration cubeCalibration) {
        this.connection = connection;
        this.cubeCalibration = cubeCalibration;
    }


    public Cube getCube() {
        return cube;
    }

    public void handler(String payload) throws InterruptedException, JsonProcessingException {
        JsonNode jsonResult = null;
        try {
            jsonResult = mapper.readTree(payload);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        WSResponseType wsType = WSResponseType.valueOf(jsonResult.get("type").asText());
        JsonNode data = jsonResult.get("data");
        if (wsType == WSResponseType.OK || wsType == WSResponseType.PI_CONNECTED || wsType == WSResponseType.PI_DISCONNECTING){
            return;
        }
        int room_id = data.get("room_id").asInt();
        if (data.asText().equals(connection.getPiName())) {
            Cube cube = new Cube();
            cube.setRoomId(room_id);
            switch (wsType) {
                case START_BATTERY_NOTIFICATION:
                    cubeCalibration.getTimeCubeService().getBatteryCharacteristic().enableValueNotifications(new BatteryValueNotification(connection, cube));
                    break;
                case STOP_BATTERY_NOTIFICATION:
                    cubeCalibration.getTimeCubeService().getBatteryCharacteristic().disableValueNotifications();
                    break;
                case START_FACET_NOTIFICATION:
                    cubeCalibration.getTimeCubeService().getFacetCharacteristic().enableValueNotifications(new FacetValueNotification(connection, cube, cubeCalibration));
                    break;
                case STOP_FACET_NOTIFICATION:
                    cubeCalibration.getTimeCubeService().getFacetCharacteristic().disableValueNotifications();
                    break;
                case FACET_REQUEST:
                    int facet = cubeCalibration.getTimeCubeService().getCurrentFacet();
                    int batteryLevel = cubeCalibration.getTimeCubeService().getBatteryLevel();
                    cube.setFacet(cubeCalibration.mapFromInternalToExternalFacet(facet));
                    cube.setBatteryLevel(batteryLevel);
                    connection.sendFacetNotification(cube);
            }
        }
    }
}
