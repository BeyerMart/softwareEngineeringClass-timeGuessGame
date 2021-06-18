# Setup

Prerequisite
* wifi connection
* Java = v1.8.0 (make sure JAVA_HOME is set)
* to compile make sure you also have the JDK installed. This isn't the default yet with java 1.8.0
* maven
* tinyb
* a running backend application with no port rules blocking connection

For easy setUp instructions follow the README.md of the [skeleton-bleclient](https://git.uibk.ac.at/csat2410/skeleton-bleclient).

The 'tinyb.jar' built on your system needs to be in this directory: /cube/lib/.

There is a 'tinyb.jar' already included, but in case of errors, make sure to use your own.

# Building
To build the cube-project go into the directory  `/cube/` and install the maven project:

`mvn clean install`

If you want to open the project in an IDE like IntelliJ, you might need to include this module. (Choose the cube/pom.xml and right click -> add module)

# Executing
The program is a spring boot application. So simply run:

`mvn spring-boot:run`

# Calibrating the cube:
The program guides you through the calibration. However, it is neccesarry to know what facet equals to what Label.

This picture shows the label of each facet but from the outside.

It is recommended to use the stickers for easy labeling. Alternatively, you can write the labels onto the cubes facet.
![CubeCalibrationFromOutSide](https://i.imgur.com/EdGduGB.png)
* At first the connection to the cube is checked. On our cube this fails about 70% of the time. A fix for this is to reset the battery. As the program shows you.

* Once a connection is established, the calibration starts.
* If the calibration (+ plus connection configuration) was done once before, the configuration is stored and loaded automatically. To redo the configuration, type 'redo'
* For the calibration, you are asked to place the cube in such a manner, that the corresponding facet shows up. (It does not matter, if you always place the cube with the corresponding facet to the table, but keep in mind to always use facet up or facet down)
* With the cube placed for the current calibration step, simply type 'y' into the console of the application.
* If you want to skip the calibration and don't care about correct facets, type 'skip'.

* You are now asked for a 'PiName' this is the unique identifier of your minicomputer (and thus cube). The default is simply 'piName'.
* Now please enter the Ip-Address of your backend
* In the next step you are asked for the port number of your backend application
* You are now connected to the backend. Use the frontend to interact with the cube. It should be visible at the room creation.