package at.qe.skeleton.bleclient;

import at.qe.skeleton.controller.WebSocketConnection;
import at.qe.skeleton.model.Cube;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.LoggerFactory;
import tinyb.BluetoothNotification;

import java.util.logging.Logger;

public class FacetValueNotification implements BluetoothNotification<byte[]> {

    private static final Logger logger = (Logger) LoggerFactory.getLogger(FacetValueNotification.class);
    private WebSocketConnection connection;
    private Cube cube;

    public FacetValueNotification(WebSocketConnection connection, Cube cube) {
        this.connection = connection;
        this.cube = cube;
    }

    public void run(byte[] facetRaw) {
        int facet = facetRaw[0];
        logger.info("ValueNotification new Facet: " + facet);
        cube.setFacet(facet);
        try {
            connection.sendFacetNotification(cube);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }

}
