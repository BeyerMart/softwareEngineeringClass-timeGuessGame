package at.qe.skeleton.bleclient;

import at.qe.skeleton.controller.WSResponseType;
import at.qe.skeleton.controller.WebSocketConnection;
import at.qe.skeleton.model.Cube;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public class WebsocketConnectionTest {

    private final Cube cube = new Cube();
    private final ObjectMapper mapper = new ObjectMapper();
    private WebSocketConnection webSocketConnection;

    @Test
    public void testOpenAndCloseWebSocket() throws ExecutionException, InterruptedException, TimeoutException {
        webSocketConnection = new WebSocketConnection();
        webSocketConnection.subscribeToChannel("cube");
        webSocketConnection.sendRegistration();
        //webSocketConnection.subscribeToChannel("register");
        webSocketConnection.close();
    }

    @Test
    public void testWebsocketVersion() throws ExecutionException, InterruptedException, TimeoutException, JsonProcessingException {
        webSocketConnection = new WebSocketConnection();
        webSocketConnection.subscribeToChannel("version");
        String answer = webSocketConnection.sendRequestForVersion("HowAreYou?");
        JsonNode jsonResult = mapper.readTree(answer);
        WSResponseType wsType = WSResponseType.valueOf(jsonResult.get("type").asText());
        Assert.assertEquals(WSResponseType.VERSION, wsType);
        webSocketConnection.close(WSResponseType.VERSION); //version is i a different channel, thus an information to close cannot be used.
    }

    @Test
    public void testWebsocketPiCubeConnected() throws InterruptedException, ExecutionException, TimeoutException, JsonProcessingException {
        webSocketConnection = new WebSocketConnection();
        webSocketConnection.subscribeToChannel("cube");
        webSocketConnection.sendRegistration();

        //webSocketConnection.emptyQueue();
        cube.setPiName("DummyCube");
        cube.setRoomId(-1);
        webSocketConnection.sendFoundAndConnected(cube);
        //JsonNode jsonResult = mapper.readTree(response.toString());
        //WSResponseType wsType = WSResponseType.valueOf(jsonResult.get("type").asText());
        //Assert.assertEquals(WSResponseType.PI_CONNECTED, wsType);
        webSocketConnection.close();
    }


}
