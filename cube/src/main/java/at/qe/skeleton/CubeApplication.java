package at.qe.skeleton;

import at.qe.skeleton.bleclient.CubeCalibration;
import at.qe.skeleton.controller.WebSocketConnection;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@SpringBootApplication
public class CubeApplication {
    private static final int WEBSOCKET_CONNECTION_TRIES = 4;
    private static CubeCalibration cubeCalibration;

    public static void main(String[] args) throws ExecutionException, InterruptedException, TimeoutException, IOException {
        SpringApplication.run(CubeApplication.class, args);

        cubeCalibration = new CubeCalibration();
        if (!cubeCalibration.startCalibration()) {
            return;
        }

        cubeCalibration.calibrate();

        WebSocketConnection webSocketConnection = null;
        int counter = 0;
        while (webSocketConnection == null) {
            try {
                webSocketConnection = new WebSocketConnection(cubeCalibration);
            } catch (Exception e) {
                e.printStackTrace();
                counter++;
                if (counter > WEBSOCKET_CONNECTION_TRIES) {
                    System.out.println("Shutting down. No Connection to Websocket possible.");
                    System.exit(1);
                }
                System.out.println("A Timeout Error occurred. Trying again to connect...");

            }
        }

        webSocketConnection.subscribeToChannel("cube");
        webSocketConnection.sendRegistration();
    }

    @PreDestroy
    public void destroy() {
        cubeCalibration.getTimeCubeService().destroy();
    }
}
