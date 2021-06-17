package at.qe.skeleton.controller;

import at.qe.skeleton.bleclient.BatteryValueNotification;
import at.qe.skeleton.bleclient.CubeCalibration;
import at.qe.skeleton.bleclient.FacetValueNotification;
import at.qe.skeleton.model.Cube;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;

public class LogicController {

    private static final Logger logger = LoggerFactory.getLogger(LogicController.class);
    private static final int BACKEND_TIMEOUT_THRESHOLD = 4;
    private final ObjectMapper mapper = new ObjectMapper();
    private final int CONNECTION_TIMEOUT = 6; //2 seconds
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
            case NOT_FOUND:
            case CONNECTION_TEST_TO_BACKEND:
                return;
            case NOT_CONNECTED:
                logger.error("Cube could not be connected. There might be a pi with the same name connected to the backend.");

            case CONNECTION_TEST_TO_PI:
                //new TimeoutChecker().start();
                if (dateOfCubeSend == null) {
                    dateOfCubeSend = Instant.parse(jsonResult.get("timestamp").asText());
                    startCheckingForTimeout();
                }

                sendTestConnection();
                return;

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

    public void sendTestConnection() {
        String sessionId = connection.getSession().getSessionId();
        ObjectNode message = mapper.createObjectNode();
        message.put("sessionId", sessionId);
        message.put("piName", cubeCalibration.getPiName());
        WebsocketResponse sendingMessage = new WebsocketResponse(message, WSResponseType.CONNECTION_TEST_TO_BACKEND);
        try {
            connection.getSession().send("/cube", sendingMessage.toString());
            dateOfCubeSend = Instant.parse(sendingMessage.mapper.readTree(sendingMessage.toString()).get("timestamp").asText());
        } catch (Exception e) {
            logger.error("Shutting down due to ConnectionException at the backend.");
            System.exit(1);
        }
    }

    public void startCheckingForTimeout() {
        TimeoutChecker timeoutChecker = new TimeoutChecker();
        timeoutChecker.start();
    }

    class TimeoutChecker extends Thread {
        public void run() {
            while (true) {
                long now = Instant.now().getEpochSecond();
                logger.debug(now + " now");
                logger.debug(dateOfCubeSend.getEpochSecond() + " DateOfCubeSend");

                if ((now - dateOfCubeSend.getEpochSecond()) > BACKEND_TIMEOUT_THRESHOLD * 2) {
                    logger.error("Shutting down due to ConnectionTimeout between backend and this pi.");
                    System.exit(1);
                }
                try {
                    Thread.sleep(BACKEND_TIMEOUT_THRESHOLD * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
