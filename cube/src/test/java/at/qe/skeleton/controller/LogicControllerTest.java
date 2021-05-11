package at.qe.skeleton.controller;

import at.qe.skeleton.bleclient.CubeCalibration;
import at.qe.skeleton.bleclient.TimeCubeService;
import at.qe.skeleton.model.Cube;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class LogicControllerTest {
    private CubeCalibration cubeCalibration;
    private WebSocketConnection webSocketConnection;
    private TimeCubeService timeCubeService;

    @Before
    public void setup() throws InterruptedException, JsonProcessingException {
        cubeCalibration = new CubeCalibration();
        webSocketConnection = Mockito.mock(WebSocketConnection.class);
        when(webSocketConnection.sendFacetNotification(any() )).thenReturn(String.valueOf(true));
        timeCubeService = Mockito.mock(TimeCubeService.class);
        when(timeCubeService.getCurrentFacet()).thenReturn(0);
        when(timeCubeService.getBatteryLevel()).thenReturn(69);
        LogicController logicController = new LogicController(webSocketConnection, cubeCalibration);

    }
    @Test
    public void facetRequestTest() throws InterruptedException, JsonProcessingException {
        LogicController logicController = new LogicController(webSocketConnection, cubeCalibration);
        String payload = "{\"type\":\"FACET_REQUEST\",\"data\":{\"room_id\":0,\"pi_name\":null,\"host_id\":1,\"players\":{\"1\":{\"user_id\":1,\"virtualUsers\":{},\"amount\":1}},\"teams\":{},\"game_id\":-1,\"room_name\":\"Room 1\",\"topic_id\":-1,\"cube\":null,\"connectedWithPiAndCube\":false,\"amountOfPlayers\":1},\"timestamp\":\"2021-05-11T15:54:44.618077600Z\"}";
        logicController.handler(payload);
    }
}
