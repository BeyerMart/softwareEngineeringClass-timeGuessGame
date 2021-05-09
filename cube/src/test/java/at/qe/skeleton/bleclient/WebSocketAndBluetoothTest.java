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

public class WebSocketAndBluetoothTest {

    private final Cube cube = new Cube();
    private final ObjectMapper mapper = new ObjectMapper();
    private WebSocketConnection webSocketConnection;

    @Test
    public void askForFacetAndSendCubeToBackendTest() throws ExecutionException, InterruptedException, TimeoutException, JsonProcessingException {
        webSocketConnection = new WebSocketConnection();
        webSocketConnection.subscribeToChannel("cube");
        try{
            TimeCubeService timeCubeService = new TimeCubeService();
            timeCubeService.setPassword();
            //cube is created after START_SEARCHING
            cube.setPiName("TestCube");
            cube.setRoomId(0); // Only used for testing
            cube.setFacet(timeCubeService.getCurrentFacet());

            String response = webSocketConnection.sendFoundAndConnected(cube);
            JsonNode jsonResult = mapper.readTree(response);
            WSResponseType wsType = WSResponseType.valueOf(jsonResult.get("type").asText());
            Assert.assertEquals(WSResponseType.OK, wsType);
        }
        catch (RuntimeException r){
            String response = webSocketConnection.sendNotFound(0);
            JsonNode jsonResult = mapper.readTree(response);
            WSResponseType wsType = WSResponseType.valueOf(jsonResult.get("type").asText());
            Assert.assertEquals(WSResponseType.OK, wsType);
        }

        webSocketConnection.close();
    }
}
