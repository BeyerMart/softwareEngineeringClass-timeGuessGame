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
		switch (type){
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
}
