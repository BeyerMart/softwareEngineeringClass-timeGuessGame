package at.qe.skeleton.bleclient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;
import java.util.Scanner;

public class CubeCalibration {
    private static final Logger logger = LoggerFactory.getLogger(CubeCalibration.class);
    private final char[] listOfActivities = new char[4];
    private final String PROPERTIES_FILE_NAME = "TimeGuessConfiguration.config";
    private HashMap<Integer, Integer> internalFacetToExternalFacetMapping;
    private boolean skippedCalibration = true;
    private Scanner scanner;
    private TimeCubeService timeCubeService;
    private String piName = "piName";
    private String url = "192.168.0.242";
    private int port = 8080;
    private Properties properties = new Properties();


    public CubeCalibration() {
        internalFacetToExternalFacetMapping = new HashMap<Integer, Integer>();
        scanner = new Scanner(System.in);
        listOfActivities[0] = 'P';
        listOfActivities[1] = 'S';
        listOfActivities[2] = 'R';
        listOfActivities[3] = 'Z';
    }

    //Constructor used for testing
    public CubeCalibration(String piName, String url, int port) {
        this.piName = piName;
        this.url = url;
        this.port = port;
    }


    public boolean startCalibration() {
        try {
            logger.info("Searching cube...");
            timeCubeService = new TimeCubeService();
            timeCubeService.setPassword();
            logger.info("If an error occurs with 'assertion 'G_IS_DBUS_INTERFACE', this can be ignored.");
            logger.info("Cube with " + timeCubeService.getBatteryLevel() + "% battery connected.");
            return true;
        } catch (RuntimeException r) {
            logger.error("Exception during cube connection: " + r);
        }
        System.out.println("An error occurred, while starting up the connection with the cube. Please reset the battery of the cube and then press 'y' to try again.");
        System.out.println("To shut down, type 'Ctrl' + 'C'.");
        String input = "";
        input = scanner.next().toLowerCase();
        if (input.equals("y") || input.equals("yes")) {
            return startCalibration();
        } else {
            logger.error("No valid response. Shutting down.");
            System.exit(1);
            return false;
        }
    }

    public void calibrate() throws IOException {
        if (loadCalibration()) {
            System.out.println("A stored configuration was loaded. If you want to recalibrate the cube and redefine the PiName, Url and Port Number, type 'redo' else 'skip'.");
            String input = scanner.next().toLowerCase();
            if (!input.equals("redo")) {
                return;
            }
        }
        calibrateCube();
        setPiName();
        setUrl();
        setPort();
        storeCalibration();
    }

    public void calibrateCube() {
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
                        while (internalFacetToExternalFacetMapping.values().contains(currentFacet)) {
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
    }

    public void setPiName() {
        System.out.println("Enter now a name for your pi and cube combination without spaces:");
        piName = scanner.next();
        System.out.println("Ok, your piName is '" + piName + "'");
        scanner.reset();
    }

    public void setUrl() {
        System.out.println("Do you want to set an URL, for the Backend, or just go with 'localhost'? \n('u'/'URL') or ('l'/'localhost')");
        String input = scanner.next().toLowerCase();
        scanner.reset();
        if (input.equals("u") || input.equals("URL")) {
            System.out.println("Enter now the URL of the Host:");
            url = scanner.next();
            System.out.println("Ok, your URL is " + url);
        } else if (input.equals("l") || input.equals("localhost")) {
            piName = "localhost";
        }
    }

    public void setPort() {
        System.out.println("The default PORT number is '8080'. \nDo you want to set a custom backen PORT? If so, just enter it now. \n(new PortNumber) or ('d'/'default')");
        String input = scanner.next().toLowerCase();
        scanner.reset();
        if (input.equals("d") || input.equals("default")) {
            port = 8080;
        } else {
            try {
                port = Integer.parseInt(input);
                System.out.println("Ok, your PORT number is " + port);
            } catch (NumberFormatException e) {
                System.err.println("Please enter only a port number. No extra symbols.");
                setPort();
            }
        }
    }

    public boolean loadCalibration() {
        FileInputStream in = null;
        try {
            in = new FileInputStream(PROPERTIES_FILE_NAME);
            properties.load(in);
            port = Integer.parseInt(properties.getProperty("PORT", "8080"));
            url = properties.getProperty("URL", "localhost");
            piName = properties.getProperty("PI_NAME", "piName");
            for (int i = 1; i < 13; i++) {
                internalFacetToExternalFacetMapping.put(i, Integer.parseInt(properties.getProperty("CALIBRATION" + i)));
            }
            in.close();
            return true;

        } catch (IOException e) {
            //No Configuration yet stored.
            return false;
        }
    }

    public void storeCalibration() throws IOException {
        try {

            properties.setProperty("PORT", Integer.toString(this.port));
            properties.setProperty("URL", this.url);
            properties.setProperty("PI_NAME", piName);
            //storing the mapping in a suitable way:
            for (int i = 1; i < 13; i++) {
                properties.setProperty("CALIBRATION" + i, String.valueOf(internalFacetToExternalFacetMapping.get(i)));
            }
            FileOutputStream out = new FileOutputStream(PROPERTIES_FILE_NAME);
            properties.store(out, "Configuration for Timeflip g6t3");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("The CubeCalibration could not be stored.");
        }
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

    public void setInternalFacetToExternalFacetMapping(HashMap<Integer, Integer> hashMap) {
        internalFacetToExternalFacetMapping = hashMap;
    }

    public String getPiName() {
        return piName;
    }

    public void setPiName(String piName) {
        this.piName = piName;
    }

    public String getUrl() {
        return url;
    }

    public int getPort() {
        return port;
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

    public int mapFromInternalToExternalFacet(int internal) {
        return internalFacetToExternalFacetMapping.entrySet().stream().filter(integerIntegerEntry -> integerIntegerEntry.getValue().equals(internal)).findAny().get().getKey();
    }
}
