package at.qe.skeleton.services;

import at.qe.skeleton.controller.CubeController;
import at.qe.skeleton.model.Activity;
import at.qe.skeleton.model.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CubeService {

	private Set<String> connectedPis = Collections.synchronizedSet(new HashSet<>());
	@Autowired
	private CubeController cubeController;

	@Autowired
	private RoomService roomService;

	public void addPiName(String piName) {
		connectedPis.add(piName);
	}

	public boolean removePi(String piName) {
		return connectedPis.remove(piName);
	}

	public List<String> getAllPis() {
		return new ArrayList<String>(connectedPis);
	}

	public int getTimeFromFacet(int facet) {
		int option = facet % 12;
		switch (option) {
			case 4:
			case 5:
			case 9:
				return 2;
			default:
				return 1;
		}
	}

	public int getPointsFromFacet(int facet) {
		int option = facet % 12;
		switch (option) {
			case 0:
				return 3;
			case 1:
				return 2;
			case 2:
				return 3;
			case 3:
				return 2;
			case 4:
				return 2;
			case 5:
				return 1;
			case 6:
				return 1;
			case 7:
				return 1;
			case 8:
				return 2;
			case 9:
				return 3;
			case 10:
				return 3;
			case 11:
				return 1;
			default:
				return 0;
		}
	}

	public Activity getActivityFromFacet(int facet) {
		int option = facet % 12;
		switch (option) {
			case 0:
				return Activity.mime;
			case 1:
				return Activity.speak;
			case 2:
				return Activity.rhyme;
			case 3:
				return Activity.rhyme;
			case 4:
				return Activity.draw;
			case 5:
				return Activity.draw;
			case 6:
				return Activity.speak;
			case 7:
				return Activity.mime;
			case 8:
				return Activity.mime;
			case 9:
				return Activity.draw;
			case 10:
				return Activity.speak;
			case 11:
				return Activity.rhyme;
			default:
				return Activity.speak;
		}
	}

	//TODO needs timer / thread wait / bool to stop the timer in GameplayController
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
}
