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

public class WebSocketAndBluetoothTest {

    private final Cube cube = new Cube();
    private final ObjectMapper mapper = new ObjectMapper();
    private WebSocketConnection webSocketConnection;

    @Test
    public void askForFacetAndSendCubeToBackendTest() throws ExecutionException, InterruptedException, TimeoutException, JsonProcessingException {
        TimeCubeService timeCubeService = new TimeCubeService();
        timeCubeService.setPassword();
        cube.setName("FacetTest");
        cube.setFacet(timeCubeService.getCurrentFacet());
        webSocketConnection = new WebSocketConnection();
        webSocketConnection.subscribeToChannel("cube");
        WebsocketResponse response = webSocketConnection.sendCubeAndGetResponse(cube);
        JsonNode jsonResult = mapper.readTree(response.toString());
        WSResponseType wsType = WSResponseType.valueOf(jsonResult.get("type").asText());
        Assert.assertEquals(WSResponseType.PI_CONNECTED, wsType);
        webSocketConnection.close();
    }
}
