package at.qe.skeleton.bleclient;

import at.qe.skeleton.controller.WSResponseType;
import at.qe.skeleton.controller.WebSocketConnection;
import at.qe.skeleton.controller.WebsocketResponse;
import at.qe.skeleton.model.Cube;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public class WebsocketConnectionTest {

    private WebSocketConnection webSocketConnection;
    private final Cube cube = new Cube();
    private final ObjectMapper mapper = new ObjectMapper();


    public void testFillAndClearWebSocket() throws ExecutionException, InterruptedException, TimeoutException {
        webSocketConnection = new WebSocketConnection();
        webSocketConnection.subscribeToChannel("cube");
        webSocketConnection.close();

    }

    @Test
    public void testWebsocketVersion() throws ExecutionException, InterruptedException, TimeoutException, JsonProcessingException {
        webSocketConnection = new WebSocketConnection();
        webSocketConnection.subscribeToChannel("version");
        WebsocketResponse answer = webSocketConnection.sendStringAndGetVersionResponse("HowAreYou?");
        JsonNode jsonResult = mapper.readTree(answer.toString());
        WSResponseType wsType = WSResponseType.valueOf(jsonResult.get("type").asText());
        Assert.assertEquals(WSResponseType.VERSION, wsType);
        webSocketConnection.close();
    }

    @Test
    public void testWebsocketCube() throws InterruptedException, ExecutionException, TimeoutException, JsonProcessingException {
        webSocketConnection = new WebSocketConnection();
        webSocketConnection.subscribeToChannel("cube");
        //webSocketConnection.emptyQueue();
        cube.setName("DummyCube");
        WebsocketResponse response = webSocketConnection.sendCubeAndGetResponse(cube);
        JsonNode jsonResult = mapper.readTree(response.toString());
        WSResponseType wsType = WSResponseType.valueOf(jsonResult.get("type").asText());
        Assert.assertEquals(WSResponseType.PI_CONNECTED, wsType);
        webSocketConnection.close();
    }

}
