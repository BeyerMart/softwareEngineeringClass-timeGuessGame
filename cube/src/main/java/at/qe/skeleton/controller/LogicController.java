package at.qe.skeleton.controller;

import at.qe.skeleton.bleclient.BatteryValueNotification;
import at.qe.skeleton.bleclient.CubeCalibration;
import at.qe.skeleton.bleclient.FacetValueNotification;
import at.qe.skeleton.model.Cube;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;

import static at.qe.skeleton.controller.WSResponseType.CONNECTION_TEST;

public class LogicController {

    private static final Logger logger = LoggerFactory.getLogger(LogicController.class);
    private final ObjectMapper mapper = new ObjectMapper();
    private final int CONNECTION_TIMEOUT = 2; //2 seconds
    Instant dateOfCubeSend;
    private Cube cube;
    private WebSocketConnection connection;
    private CubeCalibration cubeCalibration;
    private boolean successfulReplyInTime;

    public LogicController(WebSocketConnection connection, CubeCalibration cubeCalibration) {
        this.connection = connection;
        this.cubeCalibration = cubeCalibration;
    }


    public Cube getCube() {
        return cube;
    }

    public void handler(String payload) throws InterruptedException, JsonProcessingException {
        JsonNode jsonResult = null;
        int room_id;
        try {
            jsonResult = mapper.readTree(payload);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        WSResponseType wsType = WSResponseType.valueOf(jsonResult.get("type").asText());
        JsonNode data = jsonResult.get("data");
        switch (wsType) {
            case OK:
            case PI_CONNECTED:
            case PI_DISCONNECTING:
                return;
            case NOT_CONNECTED:
                logger.error("Cube could not be connected. There might be a pi with the same name connected to the backend.");
            case NOT_FOUND:
                return;
            case CONNECTION_TEST:
                if (Instant.now().getEpochSecond() - dateOfCubeSend.getEpochSecond() > CONNECTION_TIMEOUT) {
                    logger.error("Shutting down due to ConnectionTimeout between backend and this pi.");
                    System.exit(1);
                }
                Thread.sleep(CONNECTION_TIMEOUT * 1000);
                WebsocketResponse websocketResponse = new WebsocketResponse(null, CONNECTION_TEST);
                try {
                    connection.sendWebsocketRequest(websocketResponse);
                    dateOfCubeSend = Instant.parse(websocketResponse.mapper.readTree(websocketResponse.toString()).get("timestamp").asText());
                } catch (Exception e) {
                    //e.printStackTrace();
                    logger.error("Shutting down due to ConnectionTimeout at the backend.");
                    System.exit(1);
                }

            case ROOM_CREATED:
                String piNameFromBackend = data.get("piName").asText();
                if (piNameFromBackend.equals(cubeCalibration.getPiName())) {
                    int batteryLevel = cubeCalibration.getTimeCubeService().getBatteryLevel();
                    room_id = data.get("roomId").asInt();
                    int facet = cubeCalibration.getFacetFromTimeCubeService();
                    cube = new Cube(batteryLevel, room_id, facet);
                    cube.setPiName(piNameFromBackend);
                    logger.info("Cube and pi " + cube.getPiName() + " connected to room with id " + cube.getRoomId());
                    connection.sendFacetNotification(cube);
                }
                return;
        }

        room_id = data.get("id").asInt();
        //if (data.asText().equals(connection.getPiName())) {
        if (room_id == cube.getRoomId()) {
            //cube.setRoomId(room_id);
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
                    break;
                case ROOM_CREATED:

            }
        }
    }

    public void startTestBackendConnection() {
        WebsocketResponse websocketResponse = new WebsocketResponse(null, CONNECTION_TEST);
        connection.sendWebsocketRequest(websocketResponse);
        try {
            dateOfCubeSend = Instant.parse(websocketResponse.mapper.readTree(websocketResponse.toString()).get("timestamp").asText());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
