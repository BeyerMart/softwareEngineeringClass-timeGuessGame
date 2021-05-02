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

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.concurrent.*;

public class WebSocketConnection {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketConnection.class);
    private final String URL = "192.168.0.241";
    //private final String URL = "localhost";
    private final int PORT = 8080;
    private final BlockingQueue<String> blockingQueue = new ArrayBlockingQueue<>(1);
    private WebSocketStompClient client;
    private StompSession session;
    private ObjectMapper mapper = new ObjectMapper();

    public WebSocketConnection() throws ExecutionException, InterruptedException, TimeoutException {
        ArrayList list = new ArrayList();
        list.add(new WebSocketTransport(new StandardWebSocketClient()));
        this.client = new WebSocketStompClient(new SockJsClient(list));
        this.client.setMessageConverter(new StringMessageConverter());
        session = client.connect(String.format("ws://%s:%d/websocket", URL, PORT), new StompSessionHandlerAdapter() {
        }).get(1, TimeUnit.SECONDS);
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
                    }
                }
        );
    }

    public WebsocketResponse sendStringAndGetVersionResponse(String input) throws InterruptedException, JsonProcessingException {
        WebsocketResponse request = new WebsocketResponse(input, WSResponseType.VERSION);
        session.send("/version", request.toString());

        String result = blockingQueue.poll(2, TimeUnit.SECONDS);
        while (request.toString().equals(result)) {
            result = blockingQueue.poll(2, TimeUnit.SECONDS);
        }
        logger.info("Got Result from Backend: " + result);
        JsonNode jsonResult = mapper.readTree(result);
        WSResponseType wsType = WSResponseType.valueOf(jsonResult.get("type").asText());
        switch (wsType) {
            case VERSION:
                return new WebsocketResponse(jsonResult.get("data"), WSResponseType.VERSION);
            default:
                return null;
        }

    }

    public WebsocketResponse sendCubeAndGetResponse(Cube cube) throws InterruptedException, JsonProcessingException {
        WebsocketResponse request = new WebsocketResponse(cube, WSResponseType.PI_CONNECTED);
        logger.info("Sending this request: " + request);
        session.send("/cube", request.toString());

        String response = blockingQueue.poll(2, TimeUnit.SECONDS);
        while (request.toString().equals(response)) {
            response = blockingQueue.poll(2, TimeUnit.SECONDS);
        }
        logger.info("Got Result from Backend: " + response);
        JsonNode jsonResult = mapper.readTree(response);
        WSResponseType wsType = WSResponseType.valueOf(jsonResult.get("type").asText());

        return new WebsocketResponse(jsonResult.get("data").asText(), wsType);
    }

    public void emptyQueue() {
        while (!blockingQueue.isEmpty()) {
            blockingQueue.poll();
        }
    }

    public void close() {
        if (session.isConnected()) session.disconnect();
        if (client.isRunning()) client.start();
    }
}
