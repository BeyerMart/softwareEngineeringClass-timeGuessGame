package at.qe.skeleton;

import at.qe.skeleton.bleclient.CubeCalibration;
import at.qe.skeleton.controller.WebSocketConnection;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@SpringBootApplication
public class CubeApplication{
    private static CubeCalibration cubeCalibration;
    public static void main(String[] args) throws ExecutionException, InterruptedException, TimeoutException {
        SpringApplication.run(CubeApplication.class, args);

        cubeCalibration = new CubeCalibration();
        if(!cubeCalibration.startCalibration()){
            return;
        }


        cubeCalibration.calibrate();
        cubeCalibration.setPiName();
        cubeCalibration.setURL();
        cubeCalibration.setPORT();


        //TODO remove when working with web APIs
        //TimeCubeService timeCubeService = new TimeCubeService();
        //timeCubeService.setPassword();
        //System.out.println("Battery Level: " + timeCubeService.getBatteryLevel());
        WebSocketConnection webSocketConnection = new WebSocketConnection(cubeCalibration);
        webSocketConnection.subscribeToChannel("cube");
        webSocketConnection.sendRegistration();

    }
}
