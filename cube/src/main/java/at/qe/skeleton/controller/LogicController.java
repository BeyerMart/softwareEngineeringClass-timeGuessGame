package at.qe.skeleton.controller;

import at.qe.skeleton.bleclient.BatteryValueNotification;
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

    public LogicController(WebSocketConnection connection) {
        this.connection = connection;
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

    public void respondingToBackendRequest(String payload, String piName, Queue blockingQueue) {
        try {
            logger.info("responding...BackendRequest");
            JsonNode jsonResult = mapper.readTree(payload);
            WSResponseType wsType = WSResponseType.valueOf(jsonResult.get("type").asText());
            JsonNode data = jsonResult.get("data");
            switch (wsType) {
                case START_SEARCHING:
                    if (data.get("piName").asText().equals(piName)) {
                        blockingQueue.poll();
                        //blockingQueue.poll(2, TimeUnit.SECONDS);
                        respondToStartSearching(data);
                    }
                    break;
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

        } catch (JsonProcessingException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void respondToStartSearching(JsonNode data) throws InterruptedException, JsonProcessingException {
        int roomId = data.get("roomId").asInt();
        String piName = data.get("piName").asText();
        try {
            timeCubeService = new TimeCubeService();
            timeCubeService.setPassword();
            int facet = timeCubeService.getCurrentFacet();
            int batterylevel = timeCubeService.getBatteryLevel();
            cube = new Cube(batterylevel, roomId);
            cube.setFacet(facet);
            cube.setPiName(piName);
            connection.setCube(cube);
            connection.sendFoundAndConnected(cube);
        } catch (BluetoothFatalException e) {
            connection.sendNotConnected(roomId);
        } catch (BluetoothException e) {
            connection.sendNotFound(roomId);
        } catch (RuntimeException r) {
            logger.error("CubeError RunTimeException Cube could not be found / reset the battery and retry");
            Cube errorCube = new Cube();
            errorCube.setRoomId(roomId);
            errorCube.setPiName(piName);
            connection.sendCubeError(errorCube);
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
                case START_SEARCHING:
                    //blockingQueue.poll(2, TimeUnit.SECONDS);
                    respondToStartSearching(data);
                    break;
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
