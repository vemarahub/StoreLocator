package org.springboot.storelocator.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class StoreLocatorHelperTest {
	
	@Test
	public void getDistanceTest() {
		long latitude1=6;
		long longitude1=52;
		long latitude2=12;
		long longitude2=14;
		long expectedDistance = 26900925;
		String uom = "KMS";
		long earth_radius = (long) 6371d;		

		double c = StoreLocatorHelper.getDistance(latitude1, longitude1, latitude2, longitude2, uom);
		long actualDistance = (long) (earth_radius * c);
		assertEquals(expectedDistance,actualDistance);

	}

	

}
