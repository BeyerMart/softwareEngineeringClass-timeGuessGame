package at.qe.skeleton.controller;

import at.qe.skeleton.model.Cube;
import at.qe.skeleton.model.Room;
import at.qe.skeleton.payload.response.ErrorResponse;
import at.qe.skeleton.payload.response.SuccessResponse;
import at.qe.skeleton.payload.response.websocket.WSResponseType;
import at.qe.skeleton.payload.response.websocket.WebsocketResponse;
import at.qe.skeleton.services.CubeService;
import at.qe.skeleton.services.RoomService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class CubeController {

	private static final Logger logger = LoggerFactory.getLogger(CubeController.class);
	@Autowired
	SimpMessagingTemplate template;
	private ObjectMapper mapper = new ObjectMapper();
	@Autowired
	private CubeService cubeService;
	@Autowired
	private RoomService roomService;
	@Autowired
	private RoomController roomController;

	//Communication to Pi
	@SendTo("/cube")
	@MessageMapping("/cube")
	public String cubeCommunicationGetVersion(String requestMessage) throws JsonProcessingException {
		Cube cube;
		WebsocketResponse response;
		logger.info("Got this message: " + requestMessage);
		JsonNode input = mapper.readTree(requestMessage);
		JsonNode data = input.get("data");
		WSResponseType wsType = WSResponseType.valueOf(input.get("type").asText());
		switch (wsType) {
			case VERSION:
				logger.error("Ask the Version at /version");
				return "";
			//response =  new WebsocketResponse(null, WSResponseType.OK);
			//break;
			case PI_CONNECTED:
				JsonNode registration = mapper.readTree(requestMessage);
				logger.info("Pi " + registration.get("data") + " has registered");
				cubeService.addPiName(registration.get("data").asText());
				response = new WebsocketResponse(registration.get("data"), WSResponseType.OK);
				break;

			case PI_DISCONNECTING:
				JsonNode deregistration = mapper.readTree(requestMessage);
				logger.info("Pi " + deregistration.get("data") + " closed connection");
				cubeService.removePi(deregistration.get("data").asText());
				response = new WebsocketResponse(deregistration.get("data"), WSResponseType.OK);
				break;

			case FOUND_AND_CONNECTED:
				cube = mapper.readValue(input.get("data").toString(), Cube.class);
				Optional<Room> roomOptional = roomService.getRoomById(cube.getRoomId());
				if (roomOptional.isPresent()) {
					//updating Room
					roomController.cubeConnected(roomOptional.get(), cube);
					response = new WebsocketResponse(roomOptional.get(), WSResponseType.OK);
				} else {
					response = new WebsocketResponse(cube, WSResponseType.CUBE_ERROR);
				}

				break;
			case NOT_FOUND:
			case NOT_CONNECTED:
				//roomService.getRoomById(cube.getRoomId());
				//TODO Trigger FrontEnd "CubeNotFound, reset Battery"
				int roomId = data.asInt();
				Optional<Room> roomOptional1 = roomService.getRoomById(roomId);
				if (roomOptional1.isPresent()) {
					roomController.cubeNotConnected(roomOptional1.get());
					response = new WebsocketResponse(roomOptional1.get(), WSResponseType.OK);
				} else {
					logger.error("Room with id " + data.asText() + " is at pi but not in the Backend.");
					response = new WebsocketResponse(data.asText(), WSResponseType.ROOM_DELETED);
				}
				break;

			case FACET_NOTIFICATION:
				cube = mapper.convertValue(data, Cube.class);
				int facet = cube.getFacet();//TODO Where to send this Value?

				Optional<Room> roomOptional3 = roomService.getRoomById(cube.getRoomId());
				if (roomOptional3.isPresent()) {
					roomService.updateCube(cube.getRoomId(), cube);
					response = new WebsocketResponse(roomOptional3.get(), WSResponseType.OK);
				} else {
					logger.error("Room with id " + data.asText() + " is at pi but not in the Backend.");
					response = new WebsocketResponse(data.asText(), WSResponseType.ROOM_DELETED);
				}
				break;
			case BATTERY_NOTIFICATION:
				cube = mapper.convertValue(data, Cube.class);
				int batteryLevel = cube.getBatteryLevel();//TODO Where to send this Value?

				Optional<Room> roomOptional4 = roomService.getRoomById(cube.getRoomId());
				if (roomOptional4.isPresent()) {
					roomService.updateCube(cube.getRoomId(), cube);
					response = new WebsocketResponse(roomOptional4.get(), WSResponseType.OK);
				} else {
					logger.error("Room with id " + data.asText() + " is at pi but not in the Backend.");
					response = new WebsocketResponse(data.asText(), WSResponseType.ROOM_DELETED);
				}
				break;

			case CUBE_ERROR:
				cube = mapper.convertValue(data, Cube.class);
				//TODO call frontEnd an say unrecognized Cube Error (RunTimeException)

				Optional<Room> roomOptional5 = roomService.getRoomById(cube.getRoomId());
				if (roomOptional5.isPresent()) {
					response = new WebsocketResponse(roomOptional5.get(), WSResponseType.OK);
				} else {
					logger.error("Room with id " + data.asText() + " is at pi but not in the Backend.");
					response = new WebsocketResponse(data.asText(), WSResponseType.ROOM_DELETED);
				}
				break;

			case CUBE_DISCONNECTED:
				logger.info("Cube disconnected");
				return "";
			case OK:
				return "";
			default:
				return new ErrorResponse("Service not available", 404).toString();
		}
		logger.info("Writing into channel /cube" + response);
		return response.toString();
	}

	public void cubeStartSearching(Room room) {
		WebsocketResponse request = new WebsocketResponse(room, WSResponseType.START_SEARCHING);
		template.convertAndSend("/cube", request.toString());

	}

	public void cubeStartFacetNotification(Room room) {
		WebsocketResponse request = new WebsocketResponse(room, WSResponseType.START_FACET_NOTIFICATION);
		template.convertAndSend("/cube", request.toString());
	}

	public void cubeStopFacetNotification(Room room) {
		WebsocketResponse request = new WebsocketResponse(room, WSResponseType.STOP_FACET_NOTIFICATION);
		template.convertAndSend("/cube", request.toString());
	}

	public void cubeStartBatteryNotification(Room room)  {
		WebsocketResponse request = new WebsocketResponse(room, WSResponseType.START_BATTERY_NOTIFICATION);
		template.convertAndSend("/cube", request.toString());
	}

	public void cubeStopBatteryNotification(Room room)  {
		WebsocketResponse request = new WebsocketResponse(room, WSResponseType.STOP_BATTERY_NOTIFICATION);
		template.convertAndSend("/cube", request.toString());
	}

	public void cubeGetCurrentFacet(Room room){
		WebsocketResponse request = new WebsocketResponse(room, WSResponseType.FACET_REQUEST);
		template.convertAndSend("/cube", request.toString());
	}


	@GetMapping("/cubes")
	public ResponseEntity<?> getAllPis() {
		List<String> pis = cubeService.getAllPis();
		return ResponseEntity.ok(new SuccessResponse(pis).toString());
	}

	public void piConnected(String piName) {
		template.convertAndSend("/pis", new WebsocketResponse(piName, WSResponseType.PI_CONNECTED).toString());
	}

	public void piDisconnected(String piName) {
		template.convertAndSend("/pis", new WebsocketResponse(piName, WSResponseType.PI_DISCONNECTING).toString());
	}


}
