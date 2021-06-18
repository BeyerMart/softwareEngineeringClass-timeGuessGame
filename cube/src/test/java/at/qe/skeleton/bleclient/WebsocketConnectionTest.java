package at.qe.skeleton.bleclient;

import at.qe.skeleton.controller.WSResponseType;
import at.qe.skeleton.controller.WebSocketConnection;
import at.qe.skeleton.model.Cube;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public class WebsocketConnectionTest {

    private final Cube cube = new Cube();
    private final ObjectMapper mapper = new ObjectMapper();
    private WebSocketConnection webSocketConnection;
    //For Tests on other machines enter valid backend ip addresses.
    private CubeCalibration cubeCalibration = new CubeCalibration("piName", "192.168.0.242", 8080);


    /*@Test
    public void testOpenAndCloseWebSocket() throws ExecutionException, InterruptedException, TimeoutException {
        webSocketConnection = new WebSocketConnection(cubeCalibration);
        webSocketConnection.subscribeToChannel("cube");
        webSocketConnection.sendRegistration();
        Thread.sleep(1000);
        webSocketConnection.close();
    }*/
}
