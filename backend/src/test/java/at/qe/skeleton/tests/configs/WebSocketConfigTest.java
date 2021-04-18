package at.qe.skeleton.tests.configs;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
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
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class WebSocketConfigTest {
    @LocalServerPort
    private Integer port;

    private WebSocketStompClient client;
    private StompSession session;

    private final BlockingQueue<String> blockingQueue = new ArrayBlockingQueue(1);

    @BeforeAll
    public void setup() {
        this.client = new WebSocketStompClient(new SockJsClient(
                List.of(new WebSocketTransport(new StandardWebSocketClient()))
        ));
        this.client.setMessageConverter(new StringMessageConverter());
    }

    @Test
    @Order(1)
    public void initConnectionTest() {
        assertDoesNotThrow(() -> {
            session = client.connect(String.format("ws://localhost:%d/websocket", port), new StompSessionHandlerAdapter() {
            }).get(1, TimeUnit.SECONDS);
        });
    }

    @Test
    @Order(2)
    public void messageSendReceivedTest() throws InterruptedException, JSONException {
        //Setup subscription
        session.subscribe("/version", new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders stompHeaders) {
                return String.class;
            }

            @Override
            public void handleFrame(StompHeaders stompHeaders, Object payload) {
                blockingQueue.add((String) payload);
            }
        });

        //Send message
        session.send("/version", "");
        String result = blockingQueue.poll(2, TimeUnit.SECONDS);

        //Get message type
        JSONObject node = new JSONObject(result);

        assertEquals("VERSION", node.getString("type"));
    }
}
