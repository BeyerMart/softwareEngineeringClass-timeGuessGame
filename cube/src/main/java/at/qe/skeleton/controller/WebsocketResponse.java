package at.qe.skeleton.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;

public class WebsocketResponse {
    ObjectMapper mapper = new ObjectMapper();
    private final String response;
    private final Logger logger = LoggerFactory.getLogger(WebsocketResponse.class);

    //TODO OverLoad Constructor (make possible to give timestamp)
    public WebsocketResponse(Object input, WSResponseType type) {
        if (type == null) throw new IllegalArgumentException();
        String response = "";
        try {
            response = mapper.writeValueAsString(input);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        this.response = "{\"type\":\"" + type + "\",\"data\":" + response + ",\"timestamp\":\"" + Instant.now().toString() + "\"}";
        logger.debug("Generated Response/Request is: " + this.response);
    }

    @Override
    public String toString() {
        return response;
    }
}
