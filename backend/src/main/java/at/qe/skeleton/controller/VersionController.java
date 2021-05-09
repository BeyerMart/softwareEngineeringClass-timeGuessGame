package at.qe.skeleton.controller;

import at.qe.skeleton.payload.response.SuccessResponse;
import at.qe.skeleton.payload.response.websocket.WSResponseType;
import at.qe.skeleton.payload.response.websocket.WebsocketResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.http.MediaType;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

@RequestMapping(value = "api/version", produces = MediaType.APPLICATION_JSON_VALUE)
public class VersionController {
    @GetMapping
    public String getVersion() {
        //Create json object
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();

        node.put("version", "v1.0");

        return (new SuccessResponse(node)).toString();
    }

    @SendTo("/version")
    @MessageMapping("/version")
    public String getWSVersion() {
        //Create json object
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();

        node.put("version", "v1.0");

        return (new WebsocketResponse(node, WSResponseType.VERSION)).toString();
    }
}
