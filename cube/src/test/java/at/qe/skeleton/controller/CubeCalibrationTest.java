package at.qe.skeleton.controller;

import at.qe.skeleton.bleclient.CubeCalibration;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;

public class CubeCalibrationTest {
    private CubeCalibration cubeCalibration;

    private HashMap generateFakeCalibration(){
        HashMap facetMapping = new HashMap();
        for (int i = 0; i < 12; i++) {
            //offset by one
            facetMapping.put(i, i + 1);
        }
        return facetMapping;
    }

    @Test
    public void testCubeCalibration(){
        cubeCalibration = new CubeCalibration();
        cubeCalibration.setInternalFacetToExternalFacetMapping(generateFakeCalibration());
        int generatedFacet = 3;
        Assert.assertEquals(generatedFacet-1, cubeCalibration.mapFromInternalToExternalFacet(generatedFacet));
    }
}
