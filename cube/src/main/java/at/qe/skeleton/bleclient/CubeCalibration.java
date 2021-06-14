package at.qe.skeleton.bleclient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Scanner;

public class CubeCalibration {
    private static final Logger logger = LoggerFactory.getLogger(CubeCalibration.class);
    private final char[] listOfActivities = new char[4];
    private HashMap<Integer, Integer> internalFacetToExternalFacetMapping;
    private boolean connectedWithCube;
    private boolean skippedCalibration = true;
    private Scanner scanner;
    private TimeCubeService timeCubeService;
    private String piName = "";
    private String URL = "192.168.0.242";
    private int PORT = 8080;


    public CubeCalibration() {
        internalFacetToExternalFacetMapping = new HashMap<Integer, Integer>();
        connectedWithCube = false;
        scanner = new Scanner(System.in);
        listOfActivities[0] = 'P';
        listOfActivities[1] = 'S';
        listOfActivities[2] = 'R';
        listOfActivities[3] = 'Z';
        //for (int i = 0; i < 12; i++) {
        //    internalFacetToExternalFacetMapping.put(i, null);
        //}
    }

    //Constructor used for testing
    public CubeCalibration(String piName, String url, int port) {
        this.piName = piName;
        this.URL = url;
        this.PORT = port;
    }


    public boolean startCalibration() {
        try {
            logger.info("searching cube...");
            timeCubeService = new TimeCubeService();
            timeCubeService.setPassword();
            connectedWithCube = true;
            logger.info("If an error occurs with 'assertion 'G_IS_DBUS_INTERFACE', this can be ignored");
            logger.info("Cube with " + timeCubeService.getBatteryLevel() + "% battery connected.");
            return true;
        } catch (RuntimeException r) {
            logger.error("Cube could not be found. Reset the battery and retry");
            logger.error("Exception: " + r);
        }
        System.out.println("An error occurred, while starting up the connection with the cube. Please reset the battery of the cube and then press 'y' to try again.");
        System.out.println("To shut down, type 'Ctrl' + 'C'.");
        String input = "";
        input = scanner.next().toLowerCase();
        //input = console.readLine().toLowerCase();
        if (input.equals("y") || input.equals("yes")) {
            return startCalibration();
        } else {
            logger.error("No valid response. shutting down.");
            System.exit(1);
            return false;
        }
    }

    public void calibrate() {
        String input = "";
        System.out.println("Please follow the manual / quickstart guide for a visual help on calibrating.");
        System.out.println("The original mapping views the cube from the inside, as arbitrary stickers can be on the cube, you have to make sure to correctly invert it.");
        System.out.println("If you do not want to calibrate the cube and use simply values from 1 to 12 for the internal mapping, type 'skip', otherwise just type anything else.");
        input = scanner.next().toLowerCase();
        if (input.equals("skip")) {
            skippedCalibration = true;
            for (int i = 0; i < 12; i++) {
                internalFacetToExternalFacetMapping.put(i + 1, i + 1);
            }
            return;
        } else {
            skippedCalibration = false;
            for (int i = 0; i < 4; i++) {
                System.out.println("Current progress " + (double) i / (double) 4 * 100 + "%.");
                char activity = listOfActivities[i];
                for (int j = 1; j < 4; j++) {
                    int availablePoints = j;
                    System.out.println("Please turn the cube to the side " + activity + availablePoints + " and confirm with 'y'");
                    input = scanner.next().toLowerCase();
                    if (input.equals("y") || input.equals("yes")) {
                        //Mapping is like:
                        //P1,P2,P3,-S1,S2,S3,-...
                        // 0, 1, 2,  3, 4, 5, ...
                        int key = 3 * i + j;
                        int currentFacet = timeCubeService.getCurrentFacet();
                        while (internalFacetToExternalFacetMapping.values().contains(currentFacet)){
                            System.out.println("This facet is already present in the mapping.\nPlease turn the cube to the side " + activity + availablePoints + " and confirm with 'y'");
                            input = scanner.next().toLowerCase();
                            if (input.equals("y") || input.equals("yes")) {
                                currentFacet = timeCubeService.getCurrentFacet();
                            }
                        }
                        internalFacetToExternalFacetMapping.put(key, currentFacet);
                    } else {
                        logger.error("No valid response.");
                    }
                }
            }
        }

        System.out.println("Cube successfully configured.");
        internalFacetToExternalFacetMapping.values().stream().forEach(s -> System.out.println(s));
    }

    public void setPiName() {
        System.out.println("Do you want to set a custom PiName, or just go with the default 'piName'. \nNote: Each pi in the game needs a unique name. \n('c'/'custom') or ('d'/'default')");
        String input = scanner.next().toLowerCase();
        if (input.equals("c") || input.equals("custom")) {
            System.out.println("Enter now your PiName without Spaces:");
            piName = scanner.next();
            System.out.println("Ok, your piName is '" + piName + "'");
            scanner.reset();
        } else if (input.equals("d") || input.equals("default")) {
            piName = "piName";
        }
    }

    public void setPiName(String piName){
        this.piName = piName;
    }

    public void setURL() {
        System.out.println("Do you want to set an URL, for the Backend, or just go with 'localhost'? \n('u'/'URL') or ('l'/'localhost')");
        String input = scanner.next().toLowerCase();
        scanner.reset();
        if (input.equals("u") || input.equals("URL")) {
            System.out.println("Enter now the URL of the Host:");
            URL = scanner.next();
            System.out.println("Ok, your URL is " + URL);
        } else if (input.equals("l") || input.equals("localhost")) {
            piName = "localhost";
        }
    }

    public void setPORT() {
        System.out.println("The default PORT number is '8080'. \nDo you want to set a custom backen PORT? If so, just enter it now. \n(new PortNumber) or ('d'/'default')");
        String input = scanner.next().toLowerCase();
        scanner.reset();
        if (input.equals("d") || input.equals("default")) {
            PORT = 8080;
        } else {
            PORT = Integer.parseInt(input);
        }
        System.out.println("Ok, your PORT number is " + PORT);
    }

    public TimeCubeService getTimeCubeService() {
        return timeCubeService;
    }

    public void setTimeCubeService(TimeCubeService timeCubeService) {
        this.timeCubeService = timeCubeService;
    }

    public HashMap getInternalFacetToExternalFacetMapping() {
        return internalFacetToExternalFacetMapping;
    }

    public String getPiName() {
        return piName;
    }

    public String getURL() {
        return URL;
    }

    public int getPORT() {
        return PORT;
    }

    public int getFacetFromTimeCubeService() {
            int internalFacet = timeCubeService.getCurrentFacet();
            int externalFacet;
            if (skippedCalibration) {
                //ensure the value is between 0 and 11
                externalFacet = internalFacet % 12;
            } else {
                //Usage of the calibration
                externalFacet = mapFromInternalToExternalFacet(internalFacet);
            }
            return externalFacet;
    }

    public int mapFromInternalToExternalFacet(int internal){
        return internalFacetToExternalFacetMapping.entrySet().stream().filter(integerIntegerEntry -> integerIntegerEntry.getValue().equals(internal)).findAny().get().getKey();
    }

    public void setInternalFacetToExternalFacetMapping(HashMap<Integer, Integer> hashMap){
        internalFacetToExternalFacetMapping = hashMap;
    }
}
