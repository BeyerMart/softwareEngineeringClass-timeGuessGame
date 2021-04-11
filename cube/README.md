# Bluetooth Low Energy on a Raspberry Pi

### Requirements
* Successful installation of `tinyb`
* Bluetooth device in proximity
* The file `tinyb.jar` (from the tinyb installation (`tinyb/build/java/tinyb.jar`)) is in the folder `lib`.

## Building

First build the JAR so `bleclient.jar` gets placed in the `target` directory.
Additionally, all Dependencies in `pom.xml` will get placed in `target/dependencies`. This is achieved through the plugins `maven-install-plugin` and `maven-dependency-plugin`.

### Building by hand

     mvn clean package -Dmaven.test.skip=true -Dmaven.javadoc.skip=true

### Building with tests and Javadoc

     mvn clean install

## Executing

Execute the program `bleclient.jar` with `tinyb.jar` and the dependencies in `target/dependencies/*`.
Use the fully qualified name of the to be executed class:

     sudo java -cp target/bleclient.jar:./lib/tinyb.jar:./target/dependencies/* at.qe.skeleton.bleclient.Main       
