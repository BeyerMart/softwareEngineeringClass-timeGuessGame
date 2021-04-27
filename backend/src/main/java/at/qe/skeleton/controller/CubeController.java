package at.qe.skeleton.controller;

import at.qe.skeleton.model.Cube;
import at.qe.skeleton.payload.response.ErrorResponse;
import at.qe.skeleton.payload.response.websocket.WSResponseType;
import at.qe.skeleton.payload.response.websocket.WebsocketResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class CubeController {

	private static final Logger logger = LoggerFactory.getLogger(CubeController.class);


	@SendTo("/cube")
	@MessageMapping("/cube")
	public String cubeCommunicationGetVersion(String requestMessage) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode node = mapper.createObjectNode();

		logger.info("Got this message: " + requestMessage);
		JsonNode input = mapper.readTree(requestMessage);
		WebsocketResponse response;
		WSResponseType wsType = WSResponseType.valueOf(input.get("type").asText());
		switch (wsType) {
			case VERSION:

			case PI_CONNECTED:
				Cube cube = mapper.readValue(input.get("data").toString(), Cube.class);
				logger.info("Got this cube: " + cube.getName());

				node.put("gameId", "0");
				//Create json object
				response = new WebsocketResponse(node, WSResponseType.PI_CONNECTED);
				break;
			default:
				return new ErrorResponse("Service not available", 404).toString();
		}
		logger.info("Writing into channel /cube" + response);
		return response.toString();
	}
}
