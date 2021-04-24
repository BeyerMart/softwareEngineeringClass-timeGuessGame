package at.qe.skeleton.payload.response.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.Instant;

public class WebsocketResponse {
    ObjectMapper mapper = new ObjectMapper();
    private final String response;

    public WebsocketResponse(Object input, WSResponseType type) {
        if (type == null) throw new IllegalArgumentException();
        String response = "";
        try {
            response = mapper.writeValueAsString(input);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        this.response = "{\"type\":\"" + type + "\",\"data\":" + response + ",\"timestamp\":\"" + Instant.now().toString() + "\"}";
    }

    @Override
    public String toString() {
        return response;
    }
}
