package org.springboot.storelocator.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class LocationUtilTest {
	
	@Test
	public void fetchCoordinatesByLocationTest(){
		String loc = "Bakkerstraat 15, 6811 EG Arnhem, Netherlands";
		String gmapKey = "AIzaSyDvlTsR8nTb_ohYiowVitNdtrNAG-9thtM";
		long lng=6;
		long lat=52;
		
		GeoApiContext context = new GeoApiContext.Builder().apiKey(gmapKey)
					.build();
			GeocodingResult[] results = null;
			
			LatLng coordinates = null;
		
				try {
					results = GeocodingApi.geocode(context, loc).await();
				} catch (ApiException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				coordinates = results[0].geometry.location;
				assertNotNull(coordinates);
				
				assertEquals( lat, Math.round(coordinates.lat));
				assertEquals(lng, Math.round(coordinates.lng));

	}

}
