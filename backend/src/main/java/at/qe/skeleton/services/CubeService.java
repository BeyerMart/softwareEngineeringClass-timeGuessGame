package at.qe.skeleton.services;

import at.qe.skeleton.controller.CubeController;
import at.qe.skeleton.model.Activity;
import at.qe.skeleton.model.Room;
import at.qe.skeleton.payload.response.websocket.WSResponseType;
import at.qe.skeleton.payload.response.websocket.WebsocketResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class CubeService {

	private static final Logger logger = LoggerFactory.getLogger(CubeService.class);
	private final int TIMEOUT_THRESHOLD = 6;
	//piName and sessionId
	private Map<String, String> connectedPis = new ConcurrentHashMap<String, String>();
	//sessionId and timestamp
	private Map<String, String> lastUpdates = new ConcurrentHashMap<String, String>();
	@Autowired
	private CubeController cubeController;

	@Autowired
	private RoomService roomService;

	public boolean addPiName(String piName, String sessionId) {
		//PiName as key, so there can be maximum one device connected as a piName.
		//This blocks attacks where one tries to send false cube updates.
		if (connectedPis.containsKey(piName)) {
			return false;
		}
		connectedPis.put(piName, sessionId);
		lastUpdates.put(sessionId, Instant.now().toString());
		return true;
	}

	public boolean removePi(String piName) {
		String sessionId = connectedPis.get(piName);
		if (sessionId != null) {
			lastUpdates.remove(sessionId);
		}
		return (connectedPis.remove(piName) != null);
	}


	public List<String> getAllPis() {
		return new ArrayList<>(connectedPis.keySet());
	}

	public Map getConnectedPis() {
		return Collections.unmodifiableMap(connectedPis);
	}

	//Static Mapping from External Cube Facet (The one that gets transmitted) to actual labeled facets
	//P1-P2-P3-S1-S2-S3-R1-R2-R3-Z1-Z2-Z3
	//00-01-02-03-04-05-06-07-08-09-10-11
	public int getTimeFromFacet(int facet) {
		return (facet >= 9 ? 2 : 1);
	}

	public int getPointsFromFacet(int facet) {
		return (facet % 3) + 1;
	}

	public Activity getActivityFromFacet(int facet) {
		int type = facet / 3;
		switch (type) {
			case 0:
				return Activity.PANTOMIME;
			case 1:
				return Activity.SPEAK;
			case 2:
				return Activity.RHYME;
			case 3:
				return Activity.DRAW;
			default:
				//As fallback, if some unexpected values happen to be in facet.
				return Activity.SPEAK;
		}
	}

	//Update for cube facet is in RoomService
	public void startFacetNotification(int roomId) {
		Room room = roomService.getRoomById(roomId).get();
		cubeController.cubeStartFacetNotification(room);
	}

	public void stopFacetNotification(int roomId) {
		Room room = roomService.getRoomById(roomId).get();
		cubeController.cubeStopFacetNotification(room);
	}

	public void startBatteryNotification(int roomId) {
		Room room = roomService.getRoomById(roomId).get();
		cubeController.cubeStartBatteryNotification(room);
	}

	public void stopBatteryNotification(int roomId) {
		Room room = roomService.getRoomById(roomId).get();
		cubeController.cubeStopBatteryNotification(room);
	}

	public void updateTimeouts(String sessionId, String lastHeardOf) {
		lastUpdates.put(sessionId, lastHeardOf);
	}

	public void startCheckingForTimeouts(String sessionId) {
		TimeoutChecker timeoutChecker = new TimeoutChecker(this, sessionId);
		timeoutChecker.start();
	}

	public Map getLastUpdates() {
		return Collections.unmodifiableMap(lastUpdates);
	}

	class TimeoutChecker extends Thread {

		private final String sessionId;
		private final CubeService cubeService;

		TimeoutChecker(CubeService cubeService, String sessionId) {
			this.sessionId = sessionId;
			this.cubeService = cubeService;
		}

		public void run() {
			boolean piIsActive = true;
			while (piIsActive) {
				long now = Instant.now().getEpochSecond();
				long timeOfLastUpdate = Instant.parse((String) cubeService.getLastUpdates().get(sessionId)).getEpochSecond();
				if ((now - timeOfLastUpdate) > TIMEOUT_THRESHOLD * 2) {
					final String piNameToRemove = connectedPis.keySet().stream().dropWhile(piName -> (piName.equals(sessionId))).collect(Collectors.toList()).get(0);

					removePi(piNameToRemove);
					roomService.getAllRooms().forEach((aLong, room) -> {
						if (room.getCube() != null) {
							if (room.getCube().getPiName().equals(piNameToRemove)) {
								roomService.backendDeleteRoom(aLong);
							}
						}
					});
					logger.info("Pi " + piNameToRemove + " was disconnected due to timeout.");
					piIsActive = false;
				}
				try {
					Thread.sleep(TIMEOUT_THRESHOLD * 1000);
					cubeController.cubeSendConnectionTest(new WebsocketResponse(null, WSResponseType.CONNECTION_TEST_TO_PI));
				} catch (InterruptedException ignored) {
				}
			}

		}
	}
}
