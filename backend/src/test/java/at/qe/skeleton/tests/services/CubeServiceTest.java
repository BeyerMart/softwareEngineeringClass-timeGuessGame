package at.qe.skeleton.tests.services;

import at.qe.skeleton.controller.CubeController;
import at.qe.skeleton.model.Activity;
import at.qe.skeleton.payload.response.websocket.WSResponseType;
import at.qe.skeleton.payload.response.websocket.WebsocketResponse;
import at.qe.skeleton.services.CubeService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.modelmapper.internal.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.Instant;

import static org.mockito.Mockito.doNothing;

@SpringBootTest

public class CubeServiceTest {

	@Autowired
	private CubeService cubeService;
	@MockBean
	private CubeController cubeController;

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
		cubeService.addPiName("MyPi", "1234");
		Assert.isTrue(!cubeService.getAllPis().isEmpty());
		Assert.isTrue(!cubeService.getLastUpdates().isEmpty());
		Assert.isTrue(cubeService.removePi("MyPi"));
	}

	/**
	 * Test remove / deregister a Pi
	 */
	@Test
	public void testRemovePi() {
		cubeService.addPiName("MyPi", "1234");
		Assert.isTrue(!cubeService.removePi("NotMyPi"));
		Assert.isTrue(cubeService.removePi("MyPi"));
	}

	/**
	 * Test try to register with different sessionId but same piName -> thus trying to attack the pi.
	 * Test correct sessionId corresponding to piName is stored in map
	 */
	@Test
	public void testRegisterTwiceForSamePiName() {
		Assert.isTrue(cubeService.addPiName("MyPi", "1234"));
		Assert.isTrue(!cubeService.addPiName("MyPi", "2345"));

		Assert.isTrue(cubeService.getConnectedPis().get("MyPi").equals("1234"));
		Assert.isTrue(!cubeService.getConnectedPis().get("MyPi").equals("2345"));

		Assert.isTrue(cubeService.removePi("MyPi"));
	}

	/**
	 * Testing getting the piNames
	 */
	@Test
	public void testGetAllPis() {
		Assert.isTrue(cubeService.addPiName("MyPi", "1234"));
		Assert.isTrue(cubeService.addPiName("NotMyPi", "2345"));
		Assertions.assertEquals(2, cubeService.getAllPis().size());
		Assertions.assertEquals("MyPi", cubeService.getAllPis().get(0));
		Assertions.assertEquals("NotMyPi", cubeService.getAllPis().get(1));
		Assert.isTrue(cubeService.removePi("MyPi"));
		Assert.isTrue(cubeService.removePi("NotMyPi"));

	}

	/**
	 * Testing getting the piNames as unmodifiable map
	 */
	@Test
	public void testGetConnectedPis() {
		Assert.isTrue(cubeService.addPiName("MyPi", "1234"));
		Assert.isTrue(cubeService.addPiName("NotMyPi", "2345"));
		Assertions.assertEquals(2, cubeService.getConnectedPis().size());
		Assertions.assertThrows(UnsupportedOperationException.class, () -> cubeService.getConnectedPis().put("abc", "342342"));
		Assert.isTrue(cubeService.removePi("MyPi"));
		Assert.isTrue(cubeService.removePi("NotMyPi"));
	}

	/**
	 * Test to convert the facet data to the amount of points related to that facet
	 */
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

	/**
	 * Test to convert the facet data to the amount of time (in minutes) related to that facet
	 */
	@Test
	public void testFacetToTime() {
		for (int i = 0; i < 9; i++) {
			Assertions.assertEquals(1, cubeService.getTimeFromFacet(i));
		}
		for (int i = 9; i < 12; i++) {
			Assertions.assertEquals(2, cubeService.getTimeFromFacet(i));
		}
	}

	/**
	 * Test to convert the facet data to the activity related to that facet
	 */
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

	/**
	 * Test to check the updating and getting of the lastUpdates per session.
	 */
	@Test
	public void testUpdateAndGetTimeouts() {
		Instant now = Instant.now();
		cubeService.updateTimeouts("123", now.toString());
		cubeService.updateTimeouts("345", Instant.now().plusSeconds(5).toString());
		Assertions.assertEquals(2, cubeService.getLastUpdates().size());
		Assertions.assertEquals(now.toString(), cubeService.getLastUpdates().get("123"));
	}

	/**
	 * Test to check the TimeoutChecker.class
	 * This takes quite some time, as a timeout needs to occur.
	 * The method, which sends the connection test to the pi is mocked.
	 */
	@Test
	public void testTimeoutChecker() {
		cubeService.addPiName("myTimoutPI", "123");
		cubeService.updateTimeouts("123", Instant.now().toString());
		doNothing().when(cubeController).cubeSendConnectionTest(new WebsocketResponse(null, WSResponseType.CONNECTION_TEST_TO_PI));
		cubeService.startCheckingForTimeouts("123");
		try {
			Thread.sleep((cubeService.getTIMEOUT_THRESHOLD() * 3 + 1) * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Assertions.assertEquals(0, cubeService.getConnectedPis().size());
	}
}
