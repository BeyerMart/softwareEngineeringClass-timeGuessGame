package at.qe.skeleton.bleclient;

import tinyb.BluetoothNotification;

public class ValueNotification implements BluetoothNotification<byte[]> {

    public void run(byte[] facetRaw) {
        int facet=facetRaw[0];
        facet--; //This is necessary, as the facets are labeled from 2 to 13.
        System.out.println("ValueNotification new Facet: " + facet);



        //System.out.println(String.format(" Temp: Object = %fC, Ambient = %fC", objectTempCelsius, ambientTempCelsius));


    }

}
