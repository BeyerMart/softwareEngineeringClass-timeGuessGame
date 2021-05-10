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

    private TimeCubeService timeCubeService;
    private Cube cube;
    private WebSocketConnection connection;
    private CubeCalibration cubeCalibration;

    public LogicController(WebSocketConnection connection, CubeCalibration cubeCalibration) {
        this.connection = connection;
        this.cubeCalibration = cubeCalibration;
    }

    public void logic(WebSocketConnection connection, WSResponseType wsType, JsonNode data) throws InterruptedException, JsonProcessingException {

        switch (wsType) {
            case VERSION:
                //return new WebsocketResponse(jsonResult.get("data"), WSResponseType.VERSION);
                logger.info("BackendVersion: " + data.get("version").asText());
                break;


            case OK:
                //connection.sendAck();
                break;
            default:
        }
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
        if (data.get("piName").asText().equals(connection.getPiName())) {
            switch (wsType) {
                case START_BATTERY_NOTIFICATION:
                    timeCubeService.getBatteryCharacteristic().enableValueNotifications(new BatteryValueNotification(connection, cube));
                    break;
                case STOP_BATTERY_NOTIFICATION:
                    timeCubeService.getBatteryCharacteristic().disableValueNotifications();
                    break;
                case START_FACET_NOTIFICATION:
                    timeCubeService.getFacetCharacteristic().enableValueNotifications(new FacetValueNotification(connection, cube));
                    break;
                case STOP_FACET_NOTIFICATION:
                    timeCubeService.getFacetCharacteristic().disableValueNotifications();
                    break;
                case FACET_REQUEST:
                    int facet = timeCubeService.getCurrentFacet();
                    cube.setFacet(facet);
                    connection.sendFacetNotification(cube);
            }
        }
    }
}
