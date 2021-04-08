package org.springboot.storelocator.service;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class StoreServiceTest {
	
	
	
	@Autowired
	StoreService storeService;
	
	private MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
	
	
	
	@Test
	public void getAllStoresTest() throws IOException
	{
		

		storeService.getStores(queryParams, false);
		
	}

	
	  @Test
	  public void findStoreByIdTest() throws IOException {
	  
	 int storeId = 777;
	 storeService.findStoreById(storeId);
	  }
	  
	  @Test
	  public void findStoreByCityTest() throws IOException {
		  
		  queryParams.set("city", "Amsterdam");
			 storeService.getStores(queryParams, false);
			  }
	  
	  @Test
	  public void findStoreByCountryTest() throws IOException {
		  
		  queryParams.set("country", "NL");
			 storeService.getStores(queryParams, false);
			  }
	  
	  @Test
	  public void findStoreByCurrentTest() throws IOException {
		  
		  queryParams.set("current", "true");
			 storeService.getStores(queryParams, false);
			  }
	  
	  @Test
	  public void findStoreByRadiusTest() throws IOException {
		  
		  queryParams.set("location", "Amsterdam,Netherlands");
			 storeService.getStores(queryParams, false);
			  }
	 

}
