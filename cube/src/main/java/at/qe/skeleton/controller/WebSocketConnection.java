package at.qe.skeleton.controller;

import at.qe.skeleton.model.Cube;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.concurrent.*;

public class WebSocketConnection {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketConnection.class);
    private final LogicController logicController = new LogicController(this);
    private final String URL = "192.168.0.242";
    //private final String URL = "localhost";
    private final int PORT = 8080;
    private final ConcurrentLinkedQueue blockingQueue = new ConcurrentLinkedQueue();
    // private final BlockingQueue<String> blockingQueue = new ArrayBlockingQueue<>(1);
    private final WebSocketStompClient client;
    private final StompSession session;
    private final ObjectMapper mapper = new ObjectMapper();
    //each Pi gets own Name
    private final String piName = "MyPi";
    //used to only accept / process messages for me / my cube.
    private Cube cube;

    public WebSocketConnection() throws ExecutionException, InterruptedException, TimeoutException {
        ArrayList list = new ArrayList();
        list.add(new WebSocketTransport(new StandardWebSocketClient()));
        this.client = new WebSocketStompClient(new SockJsClient(list));
        this.client.setMessageConverter(new StringMessageConverter());
        session = client.connect(String.format("ws://%s:%d/websocket", URL, PORT), new StompSessionHandlerAdapter() {
        }).get(1, TimeUnit.SECONDS);
        //subscribeToChannel("register");
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
                        logger.info("Got This in my channel: " + (String) payload);
                        try {
                            logicController.handler((String) payload);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (JsonProcessingException e) {
                            e.printStackTrace();
                        }
                        //logicController.respondingToBackendRequest((String) payload, piName, blockingQueue);
                    }
                }
        );
    }

    public String handleBackendResponse(String request) throws InterruptedException, JsonProcessingException {
        //String response = blockingQueue.poll(2, TimeUnit.SECONDS);
        String response = (String) blockingQueue.poll();

        while (request.equals(response)) {
            response = (String) blockingQueue.poll();
        }
        logger.debug("response " + response);
        JsonNode jsonResult = mapper.readTree(response);
        WSResponseType wsType = WSResponseType.valueOf(jsonResult.get("type").asText());
        JsonNode data = jsonResult.get("data");
        if (wsType == WSResponseType.OK) {
            return response;
        } else {
            if (wsType == WSResponseType.VERSION || data.get("piName").asText() == piName || data.get("id").asInt() == cube.getRoomId()) {
                logicController.logic(this, wsType, data);
            }
        }
        return response;
    }

    public String sendRequestForVersion(String input) throws InterruptedException, JsonProcessingException {
        WebsocketResponse request = new WebsocketResponse(input, WSResponseType.VERSION);
        session.send("/version", request.toString());
        String response = handleBackendResponse(request.toString());
        return response;
    }

    public String sendPiConnected() throws InterruptedException, JsonProcessingException {
        WebsocketResponse request = new WebsocketResponse(cube, WSResponseType.PI_CONNECTED);
        sendWebsocketRequest(request);
        String response = handleBackendResponse(request.toString());
        return response;
    }

    public String sendFoundAndConnected(Cube cube) throws InterruptedException, JsonProcessingException {
        WebsocketResponse request = new WebsocketResponse(cube, WSResponseType.FOUND_AND_CONNECTED);
        sendWebsocketRequest(request);
        String response = handleBackendResponse(request.toString());
        return response;
    }

    public String sendNotFound(int roomId) throws InterruptedException, JsonProcessingException {
        WebsocketResponse request = new WebsocketResponse(roomId, WSResponseType.NOT_FOUND);
        sendWebsocketRequest(request);
        String response = handleBackendResponse(request.toString());
        return response;
    }

    public String sendNotConnected(int roomId) throws InterruptedException, JsonProcessingException {
        WebsocketResponse request = new WebsocketResponse(roomId, WSResponseType.NOT_CONNECTED);
        sendWebsocketRequest(request);
        String response = handleBackendResponse(request.toString());
        return response;
    }

    public String sendFacetNotification(Cube cube) throws InterruptedException, JsonProcessingException {
        WebsocketResponse request = new WebsocketResponse(cube, WSResponseType.FACET_NOTIFICATION);
        sendWebsocketRequest(request);
        String response = handleBackendResponse(request.toString());
        return response;
    }

    public String sendBatteryNotification(Cube cube) throws InterruptedException, JsonProcessingException {
        WebsocketResponse request = new WebsocketResponse(cube, WSResponseType.BATTERY_NOTIFICATION);
        sendWebsocketRequest(request);
        String response = handleBackendResponse(request.toString());
        return response;
    }

    public String sendCubeError(Cube cube) throws InterruptedException, JsonProcessingException {
        WebsocketResponse request = new WebsocketResponse(cube, WSResponseType.CUBE_ERROR);
        sendWebsocketRequest(request);
        String response = handleBackendResponse(request.toString());
        return response;
    }

    public String sendCubeDisconnected(Cube cube) throws InterruptedException, JsonProcessingException {
        WebsocketResponse request = new WebsocketResponse(cube, WSResponseType.CUBE_DISCONNECTED);
        sendWebsocketRequest(request);
        String response = handleBackendResponse(request.toString());
        return response;
    }

    public String sendPiDisconnected(Cube cube) throws InterruptedException, JsonProcessingException {
        WebsocketResponse request = new WebsocketResponse(cube, WSResponseType.PI_DISCONNECTING);
        sendWebsocketRequest(request);
        String response = handleBackendResponse(request.toString());
        return response;
    }

    public void sendAck() {
        WebsocketResponse message = new WebsocketResponse("ACK", WSResponseType.OK);
        logger.info("Sending this request: " + message);
        session.send("/cube", message.toString());
    }

    public void sendCubeAck(Cube cube) {
        this.cube = cube;
        WebsocketResponse message = new WebsocketResponse(cube, WSResponseType.OK);
        sendWebsocketRequest(message);
    }

    private void sendWebsocketRequest(WebsocketResponse request) {
        logger.debug("Sending this request: " + request);
        session.send("/cube", request.toString());
    }

    public void emptyQueue() {
        while (!blockingQueue.isEmpty()) {
            blockingQueue.poll();
        }
    }

    public void sendRegistration() {
        WebsocketResponse registerMessage = new WebsocketResponse(piName, WSResponseType.PI_CONNECTED);
        session.send("/cube", registerMessage.toString());
        /*
        try {
            handleBackendResponse(registerMessage.toString());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }*/
    }

    public void close(WSResponseType version) {
        if (session.isConnected()) session.disconnect();
        if (client.isRunning()) client.start();
    }

    public void close() {
        WebsocketResponse wsMessage = new WebsocketResponse(piName, WSResponseType.PI_DISCONNECTING);
        session.send("/cube", wsMessage.toString());

        try {
            handleBackendResponse(wsMessage.toString());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            logger.error("A read on the channel was requested, but nothing was sent to the channel");
        }
        if (session.isConnected()) session.disconnect();
        if (client.isRunning()) client.start();
    }

    public void setCube(Cube cube) {
        this.cube = cube;
    }

    public String getPiName(){
        return piName;
    }

    @PreDestroy
    public void destroy() {
        close();
    }
}
