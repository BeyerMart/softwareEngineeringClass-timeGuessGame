package at.qe.skeleton.bleclient;

import at.qe.skeleton.controller.WebSocketConnection;
import at.qe.skeleton.model.Cube;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tinyb.BluetoothNotification;

public class FacetValueNotification implements BluetoothNotification<byte[]> {

    private static final Logger logger = (Logger) LoggerFactory.getLogger(FacetValueNotification.class);
    private WebSocketConnection connection;
    private Cube cube;
    private CubeCalibration cubeCalibration;

    public FacetValueNotification(WebSocketConnection connection, Cube cube, CubeCalibration cubeCalibration) {
        this.connection = connection;
        this.cube = cube;
        this.cubeCalibration = cubeCalibration;
    }

    public void run(byte[] facetRaw) {
        int facet = facetRaw[0];
        logger.info("ValueNotification new raw (uncalibrated) Facet: " + facet);
        cube.setFacet(cubeCalibration.mapFromInternalToExternalFacet(facet));
        try {
            connection.sendFacetNotification(cube);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }

}
