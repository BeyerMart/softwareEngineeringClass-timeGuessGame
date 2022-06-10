package at.qe.skeleton.controller;

import at.qe.skeleton.bleclient.CubeCalibration;
import at.qe.skeleton.model.Cube;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import javax.annotation.PreDestroy;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class WebSocketConnection {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketConnection.class);
    private final LogicController logicController;
    private final String URL;
    private final int PORT;
    private final ConcurrentLinkedQueue blockingQueue = new ConcurrentLinkedQueue();
    private final WebSocketStompClient client;
    private final StompSession session;
    private final ObjectMapper mapper = new ObjectMapper();
    //each Pi gets own Name
    private final String piName;
    private CubeCalibration cubeCalibration;
    //used to only accept / process messages for me / my cube.

    public WebSocketConnection(CubeCalibration cubeCalibration) throws ExecutionException, InterruptedException, TimeoutException {
        logger.info("Connecting to Backend...");
        this.cubeCalibration = cubeCalibration;

        piName = cubeCalibration.getPiName();
        URL = cubeCalibration.getUrl();
        PORT = cubeCalibration.getPort();
        ArrayList list = new ArrayList();
        list.add(new WebSocketTransport(new StandardWebSocketClient()));
        this.client = new WebSocketStompClient(new SockJsClient(list));
        this.client.setMessageConverter(new StringMessageConverter());
        session = client.connect(String.format("ws://%s:%d/websocket", URL, PORT), new StompSessionHandlerAdapter() {
        }).get(3, TimeUnit.SECONDS);
        this.logicController = new LogicController(this, cubeCalibration);
    }

    public void subscribeToChannel(String channel) {

        session.subscribe("/" + channel, new StompFrameHandler() {
                    @Override
                    public Type getPayloadType(StompHeaders stompHeaders) {
                        return String.class;
                    }

                    @Override
                    public void handleFrame(StompHeaders stompHeaders, Object payload) {
                        blockingQueue.add((String) payload);
                        logger.info("Got this in my channel: " + (String) payload);
                        try {
                            logicController.handler((String) payload);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (JsonProcessingException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
    }

    private ObjectNode preProcessMessage(Cube cube) throws JsonProcessingException {
        String sessionId = session.getSessionId();
        ObjectNode message = mapper.createObjectNode();
        message.put("sessionId", sessionId);
        message.putPOJO("cube", cube);
        return message;
    }

    public void sendFacetNotification(Cube cube) throws InterruptedException, JsonProcessingException {
        ObjectNode message = preProcessMessage(cube);
        WebsocketResponse request = new WebsocketResponse(message, WSResponseType.FACET_NOTIFICATION);
        sendWebsocketRequest(request);
    }

    public void sendBatteryNotification(Cube cube) throws InterruptedException, JsonProcessingException {
        ObjectNode message = preProcessMessage(cube);
        WebsocketResponse request = new WebsocketResponse(message, WSResponseType.BATTERY_NOTIFICATION);
        sendWebsocketRequest(request);
    }


    private void sendWebsocketRequest(WebsocketResponse request) {
        logger.debug("Sending this request: " + request);
        session.send("/cube", request.toString());
    }


    public void sendRegistration() {
        String sessionId = session.getSessionId();
        ObjectNode message = mapper.createObjectNode();
        message.put("sessionId", sessionId);
        message.put("piName", piName);
        WebsocketResponse registerMessage = new WebsocketResponse(message, WSResponseType.PI_CONNECTED);
        session.send("/cube", registerMessage.toString());
    }

    public void close() {
        WebsocketResponse wsMessage = new WebsocketResponse(piName, WSResponseType.PI_DISCONNECTING);
        session.send("/cube", wsMessage.toString());

        if (session.isConnected()) session.disconnect();
        if (client.isRunning()) client.start();
    }

    @PreDestroy
    public void destroy() {
        close();
    }

    public StompSession getSession() {
        return session;
    }

    public LogicController getLogicController() {
        return logicController;
    }

}
