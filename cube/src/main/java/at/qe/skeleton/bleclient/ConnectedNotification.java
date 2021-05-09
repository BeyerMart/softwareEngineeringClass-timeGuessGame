package at.qe.skeleton.bleclient;

import org.slf4j.LoggerFactory;
import tinyb.BluetoothNotification;

import java.util.logging.Logger;

public class ConnectedNotification implements BluetoothNotification<Boolean> {

	private static final Logger logger = (Logger) LoggerFactory.getLogger(ConnectedNotification.class);

	public void run(Boolean connected) {
		logger.info("ConnectedNotification");
	}

}