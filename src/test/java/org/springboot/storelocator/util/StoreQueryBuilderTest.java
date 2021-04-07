package org.springboot.storelocator.util;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springboot.storelocator.model.Store;
import org.springboot.storelocator.model.StoreGenerator;
import org.springboot.storelocator.model.Stores;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class StoreQueryBuilderTest {
	
	@Autowired
	StoreQueryBuilder storeQuery;
	
	@Test
	public void getRadiusStoresTest() {
	
		MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
		queryParams.set("Radius", "1");
		queryParams.set("uom", "KMS");
		List<Store> storesList = new ArrayList<Store>();
		Stores stores = new Stores();
		storesList.add(StoreGenerator.generate(120, "Store ABC", "AZ", "USA"));
		storesList.add(StoreGenerator.generate(130, "Store XYZ", "NA", "IT"));
		storesList.add(StoreGenerator.generate(140, "Store TEMP", "CA", "SA"));
		stores.setStores(storesList);
		double indexLatitude=52.35974;
		double indexLongitude=40.87914;
		
		List<Store> actualStoreList = storeQuery.getRadiusStores(queryParams, stores, indexLatitude, indexLongitude);
	
		assertEquals(storesList.get(0).getStoreId(), actualStoreList.get(0).getStoreId());
		
	}

}
