package at.qe.skeleton.controller;

import at.qe.skeleton.bleclient.CubeCalibration;
import at.qe.skeleton.bleclient.TimeCubeService;
import at.qe.skeleton.model.Cube;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;


import java.util.HashMap;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class LogicControllerTest {
    private CubeCalibration cubeCalibration;
    private WebSocketConnection webSocketConnection;
    private TimeCubeService timeCubeService;
    private String piName = "testPi";
    private LogicController logicController;

    private HashMap createOneToOneHashmap(){
        HashMap hashMap = new HashMap();
        for (int i = 0; i < 12; i++) {
            hashMap.put(i,i);
        }
        return hashMap;
    }

    private void setup() throws InterruptedException, JsonProcessingException {
        cubeCalibration = new CubeCalibration();
        cubeCalibration.setPiName(piName);
        timeCubeService = Mockito.mock(TimeCubeService.class);
        cubeCalibration.setTimeCubeService(timeCubeService);
        cubeCalibration.setInternalFacetToExternalFacetMapping(createOneToOneHashmap());
        webSocketConnection = Mockito.mock(WebSocketConnection.class);
        //when(webSocketConnection.sendFacetNotification(any() )).thenReturn(String.valueOf(true));
        when(timeCubeService.getCurrentFacet()).thenReturn(0);
        when(timeCubeService.getBatteryLevel()).thenReturn(69);
        logicController = new LogicController(webSocketConnection, cubeCalibration);
    }
    @Test
    public void facetRequestTest() throws InterruptedException, JsonProcessingException {
        setup();
        String responseFromBackendAfterConnectingCubeWithPi = "{\"type\":\"ROOM_CREATED\",\"data\":{\"facet\":0,\"piName\":\""+ piName +"\",\"roomId\":0,\"batteryLevel\":0},\"timestamp\":\"2021-05-13T17:08:38.645041500Z\"}";
        logicController.handler(responseFromBackendAfterConnectingCubeWithPi);
        String payload = "{\"type\":\"FACET_REQUEST\",\"data\":{\"id\":0,\"pi_name\":\""+ piName +"\",\"host_id\":1,\"players\":{\"1\":{\"user_id\":1,\"virtualUsers\":{},\"amount\":1}},\"teams\":{},\"game_id\":-1,\"room_name\":\"Room 1\",\"topic_id\":-1,\"cube\":null,\"connectedWithPiAndCube\":false,\"amountOfPlayers\":1},\"timestamp\":\"2021-05-11T15:54:44.618077600Z\"}";
        logicController.handler(payload);
    }
}
