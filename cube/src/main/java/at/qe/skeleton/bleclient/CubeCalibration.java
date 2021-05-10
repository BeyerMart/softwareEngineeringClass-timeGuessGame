package at.qe.skeleton.bleclient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Console;
import java.util.HashMap;
import java.util.Locale;
import java.util.Scanner;

public class CubeCalibration {
    private static final Logger logger = LoggerFactory.getLogger(CubeCalibration.class);
    private HashMap<Integer, Integer> internalFacetToExternalFacetMapping;
    private boolean connectedWithCube;
    private Scanner scanner;
    private char[] listOfActivitys = new char[4];
    private TimeCubeService timeCubeService;
    private String piName = "";
    private String URL = "192.168.0.242";
    private int PORT = 8080;


    public CubeCalibration() {
        internalFacetToExternalFacetMapping = new HashMap<Integer, Integer>();
        connectedWithCube = false;
        scanner = new Scanner(System.in);
        listOfActivitys[0] = 'P';
        listOfActivitys[1] = 'S';
        listOfActivitys[2] = 'R';
        listOfActivitys[3] = 'Z';
        //for (int i = 0; i < 12; i++) {
        //    internalFacetToExternalFacetMapping.put(i, null);
        //}
    }


    public boolean startCalibration() {
        try {
            timeCubeService = new TimeCubeService();
            timeCubeService.setPassword();
            connectedWithCube = true;
            logger.info("Cube with " + timeCubeService.getBatteryLevel() + "% battery connected.");
            return true;
        } catch (RuntimeException r) {
            logger.error("Cube could not be found. Reset the battery and retry");
        }
        System.out.println("An error occurred, while starting up the connection with the cube. Please reset the battery of the cube and then press 'y' to try again.");
        System.out.println("To shut down, type any other key.");
        String input = "";
        input = scanner.next().toLowerCase();
        //input = console.readLine().toLowerCase();
        if (input.equals("y") || input.equals("yes")) {
            return startCalibration();
        } else {
            logger.error("No valid response. shutting down.");
            return false;
        }
    }

    public void calibrate() {
        String input = "";
        System.out.println("Please follow the manual / quickstart guide for a visual help on calibrating");
        System.out.println("The original mapping views the cube from the inside, as arbitrary stickers can be on the cube, you have to make sure to correctly invert it.");
        System.out.println("If you do not want to calibrate the cube and use simply values from 1 to 12 for the internal mapping, type 'skip', otherwise just type anything else.");
        input = scanner.next().toLowerCase();
        if (input.equals("skip")){
            for (int i = 0; i < 12; i++) {
                internalFacetToExternalFacetMapping.put(i+1, i+1);
            }
            return;
        }
        else{
            for (int i = 0; i < 4; i++) {
                System.out.println("Current progress " + (double) i / (double) 4 + " %.");
                char activity = listOfActivitys[i];
                for (int j = 1; j < 4; j++) {
                    int availablePoints = j;
                    System.out.println("Please turn the cube to the side " + activity + availablePoints + " and confirm with 'y'");
                    input = scanner.next().toLowerCase();
                    if (input.equals("y") || input.equals("yes")) {
                        //Mapping is like:
                        //P1,P2,P3,-S1,S2,S3,-...
                        // 0, 1, 2,  3, 4, 5, ...
                        int key = 3*i+j;
                        internalFacetToExternalFacetMapping.put(key, timeCubeService.getCurrentFacet());
                    } else {
                        logger.error("No valid response.");
                    }
                }
            }
        }

        System.out.println("Cube successfully configured.");
        internalFacetToExternalFacetMapping.values().stream().forEach(s -> System.out.println(s));
    }

    public void setPiName(){
        System.out.println("Do you want to set a custom PiName, or just go with the default 'piName'. \nNote: Each pi in the game needs a unique name. \n('c'/'custom') or ('d'/'default')");
        String input = scanner.next().toLowerCase();
        if (input.equals("c") || input.equals("custom")){
            System.out.println("Enter now your PiName:");
            piName = scanner.next();
            System.out.println("Ok, your piName is " + piName);
        } else if (input.equals("d") || input.equals("default")){
            piName = "piName";
        }
    }

    public void setURL(){
        System.out.println("Do you want to set an URL, for the Backend, or just go with 'localhost'? \n('u'/'URL') or ('l'/'localhost')");
        String input = scanner.next().toLowerCase();
        if (input.equals("u") || input.equals("URL")){
            System.out.println("Enter now the URL of the Host:");
            URL = scanner.next();
            System.out.println("Ok, your URL is " + URL);
        } else if (input.equals("l") || input.equals("localhost")){
            piName = "localhost";
        }
    }

    public void setPORT(){
        System.out.println("The default PORT number is '8080'. \nDo you want to set a custom backen PORT? If so, just enter it now. \n(new PortNumber) or ('d'/'default')");
        String input = scanner.next().toLowerCase();
        if (input.equals("d") || input.equals("default")){
            PORT = 8080;
        } else {
            PORT = Integer.parseInt(input);
        }
        System.out.println("Ok, your PORT number is " + PORT);
    }

    public TimeCubeService getTimeCubeService(){
        return timeCubeService;
    }

    public HashMap getInternalFacetToExternalFacetMapping(){
        return internalFacetToExternalFacetMapping;
    }

    public String getPiName(){
        return piName;
    }

    public String getURL() {
        return URL;
    }

    public int getPORT() {
        return PORT;
    }
}
