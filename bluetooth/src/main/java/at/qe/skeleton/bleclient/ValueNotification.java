package at.qe.skeleton.bleclient;

import tinyb.BluetoothNotification;

public class ValueNotification implements BluetoothNotification<byte[]> {

	public void run(byte[] facetRaw) {
		int facet = facetRaw[0];
		System.out.println("ValueNotification new Facet: " + facet);
	}

}
