package at.qe.skeleton;

import at.qe.skeleton.bleclient.TimeCubeService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CubeApplication{

    public static void main(String[] args){
        SpringApplication.run(CubeApplication.class, args);

        //TODO remove when working with web APIs
        TimeCubeService timeCubeService = new TimeCubeService();
        timeCubeService.setPassword();
        System.out.println("Battery Level: " + timeCubeService.getBatteryLevel());

    }
}
