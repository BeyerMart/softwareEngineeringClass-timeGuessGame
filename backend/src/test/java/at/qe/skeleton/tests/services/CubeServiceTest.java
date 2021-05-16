package at.qe.skeleton.tests.services;

import at.qe.skeleton.model.Activity;
import at.qe.skeleton.services.CubeService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.modelmapper.internal.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest

public class CubeServiceTest {

	@Autowired
	private CubeService cubeService;

	/**
	 * Test unregistering a non existing Pi
	 * There might be a race condition on the connectedPis Set. However this was tried to limit be using a synchronizedSet.
	 */
	@Test
	public void testUnregisterPiInEmptySet() {
		Assert.isTrue(!cubeService.removePi("MyPi"));
	}

	/**
	 * Test register a Pi
	 */
	@Test
	public void testRegisterPi() {
		cubeService.addPiName("MyPi");
		Assert.isTrue(!cubeService.getAllPis().isEmpty());
	}

	/**
	 * Test remove / deregister a Pi
	 */
	@Test
	public void testRemovePi() {
		cubeService.addPiName("MyPi");
		Assert.isTrue(!cubeService.removePi("NotMyPi"));
		Assert.isTrue(cubeService.removePi("MyPi"));
	}

	@Test
	public void testFacetToPoints() {
		for (int i = 0; i < 12; i += 3) {
			Assertions.assertEquals(1, cubeService.getPointsFromFacet(i));
		}
		for (int i = 1; i < 12; i += 3) {
			Assertions.assertEquals(2, cubeService.getPointsFromFacet(i));
		}
		for (int i = 2; i < 12; i += 3) {
			Assertions.assertEquals(3, cubeService.getPointsFromFacet(i));
		}
	}

	@Test
	public void testFacetToTime() {
		for (int i = 0; i < 9; i++) {
			Assertions.assertEquals(1, cubeService.getTimeFromFacet(i));
		}
		for (int i = 9; i < 12; i++) {
			Assertions.assertEquals(2, cubeService.getTimeFromFacet(i));
		}
	}

	@Test
	public void testFacetToActivity() {
		for (int i = 0; i < 12; i++) {
			if (i < 3) {
				Assertions.assertEquals(Activity.PANTOMIME, cubeService.getActivityFromFacet(i));
			} else if (i < 6) {
				Assertions.assertEquals(Activity.SPEAK, cubeService.getActivityFromFacet(i));
			} else if (i < 9) {
				Assertions.assertEquals(Activity.RHYME, cubeService.getActivityFromFacet(i));
			} else if (i < 12) {
				Assertions.assertEquals(Activity.DRAW, cubeService.getActivityFromFacet(i));
			}
		}
	}

}
