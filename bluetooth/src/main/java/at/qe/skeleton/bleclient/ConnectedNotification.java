package at.qe.skeleton.bleclient;

import tinyb.BluetoothNotification;

public class ConnectedNotification implements BluetoothNotification<Boolean> {

	public void run(Boolean connected) {
		System.out.println("ConnectedNotification");
	}

}