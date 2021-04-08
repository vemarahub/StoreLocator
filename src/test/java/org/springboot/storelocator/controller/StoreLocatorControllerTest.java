package org.springboot.storelocator.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springboot.storelocator.model.Store;
import org.springboot.storelocator.model.StoreGenerator;
import org.springboot.storelocator.model.Stores;
import org.springboot.storelocator.service.ApplicationTestConfig;
import org.springboot.storelocator.service.StoreServiceImpl;
import org.springboot.storelocator.util.StoreQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ApplicationTestConfig.class)
@WebAppConfiguration
class StoreLocatorControllerTest {

	@InjectMocks
	StoreLocatorController storeLocatorController;

	private MockMvc mockMvc;

	@Mock
	StoreServiceImpl storeService;

	@Autowired
	StoreQueryBuilder storeQueryBuilder;

	Stores stores = new Stores();

	Stores storesTemp;

	private MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();


	@BeforeEach
	 void initStores() {
		this.mockMvc = MockMvcBuilders.standaloneSetup(storeLocatorController).build();
		List<Store> storeList = new ArrayList<Store>();	
		Store testStore = StoreGenerator.generate();
		storeList.add(testStore);
		storeList.add(StoreGenerator.generate(100,"store Marvel", "Amsterdam", "NL"));
		storeList.add(StoreGenerator.generate(200,"store DC", "New York", "USA"));
		storeList.add(StoreGenerator.generate(300,"store Unicorn", "London", "GB"));
		
		
		stores.setStores(storeList);
		
	}

	@Test
	 void testGetAllStores() throws Exception {

		
		when(this.storeService.getStores(queryParams,false)).thenReturn(stores);

		ResponseEntity<Object> response = storeLocatorController.getStores(queryParams,false);
		Stores storesData = (Stores) response.getBody();
		assertNotNull(storesData);
		assertEquals(stores, storesData);
		assertEquals(HttpStatus.OK, response.getStatusCode());


	}

	@Test
	 void testGetStoresByCity() throws Exception {

		queryParams.set("city", "Amsterdam");

		when(this.storeService.getStores(queryParams,false)).thenReturn(stores);

		ResponseEntity<Object> response = storeLocatorController.getStores(queryParams,false);
		Stores storesData = (Stores) response.getBody();
		assertNotNull(storesData);

		stores.getStores().get(0).setCity("Amsterdam");

		assertEquals(stores.getStores().get(0).getCity(), storesData.getStores().get(0).getCity());
		assertEquals(HttpStatus.OK, response.getStatusCode());

	}

	@Test
	 void testGetStoresByCountry() throws Exception {

		queryParams.set("country", "USA");

		when(this.storeService.getStores(queryParams,false)).thenReturn(stores);

		ResponseEntity<Object> response = storeLocatorController.getStores(queryParams,false);
		Stores storesData = (Stores) response.getBody();
		assertNotNull(storesData);

		
		assertEquals(stores.getStores().get(0).getCountry(), storesData.getStores().get(0).getCountry());
		assertEquals(HttpStatus.OK, response.getStatusCode());

	}

	@Test
	 void testGetStoresByRadius() throws Exception {

		queryParams.set("radius", "100");
		queryParams.set("longitude", "50.44");
		queryParams.set("latitude", "44.23");

		when(this.storeService.getStores(queryParams,false)).thenReturn(stores);

		ResponseEntity<Object> response = storeLocatorController.getStores(queryParams,false);
		Stores storesData = (Stores) response.getBody();
		assertNotNull(storesData);

		stores = storeQueryBuilder.buildQuery(queryParams, stores);

		if (!stores.getStores().isEmpty() && !storesData.getStores().isEmpty())
			assertEquals(stores.getStores().get(0).getStoreId(), storesData.getStores().get(0).getStoreId());
		assertEquals(HttpStatus.OK, response.getStatusCode());

	}

	@Test
	 void testGetStoresCurrent() throws Exception {

		queryParams.set("current", "true");

		when(this.storeService.getStores(queryParams,false)).thenReturn(stores);

		ResponseEntity<Object> response = storeLocatorController.getStores(queryParams,false);
		Stores storesData = (Stores) response.getBody();
		assertNotNull(storesData);
		
		stores.getStores().get(0).setStoreId(100);

		assertEquals(stores.getStores().get(0).getStoreId(), storesData.getStores().get(0).getStoreId());
		assertEquals(HttpStatus.OK, response.getStatusCode());

	}

	@Test
	 void testDeleteStore() throws Exception {
		Store newStore = stores.getStores().get(0);
		ObjectMapper mapper = new ObjectMapper();
		String newStoreAsJSON = mapper.writeValueAsString(newStore);
		given(this.storeService.findStoreById(100)).willReturn(stores.getStores().get(0));
		this.mockMvc.perform(delete("/stores/100").content(newStoreAsJSON).accept(MediaType.APPLICATION_JSON_VALUE)
				.contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk());
	}
	
	@Test
	 void testDeleteStoreNotFound() throws Exception {
		Store newStore = stores.getStores().get(0);
		ObjectMapper mapper = new ObjectMapper();
		String newStoreAsJSON = mapper.writeValueAsString(newStore);
		given(this.storeService.findStoreById(990)).willReturn(stores.getStores().get(0));
		this.mockMvc.perform(delete("/stores/290").content(newStoreAsJSON).accept(MediaType.APPLICATION_JSON_VALUE)
				.contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isNotFound());
	}

	@Test
	 void testUpdateStore() throws Exception {
		Store newStore = stores.getStores().get(0);
		newStore.setStoreName("Tommy");
		ObjectMapper mapper = new ObjectMapper();
		String newStoreAsJSON = mapper.writeValueAsString(newStore);
		given(this.storeService.findStoreById(102)).willReturn(stores.getStores().get(0));
		this.mockMvc.perform(put("/stores/102").content(newStoreAsJSON).accept(MediaType.APPLICATION_JSON_VALUE)
				.contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk());
		
	}
	
	@Test
	 void testUpdateStoreNotFound() throws Exception {
		Store newStore = stores.getStores().get(0);
		newStore.setStoreName("Tommy");
		ObjectMapper mapper = new ObjectMapper();
		String newStoreAsJSON = mapper.writeValueAsString(newStore);
		given(this.storeService.findStoreById(912)).willReturn(stores.getStores().get(0));
		this.mockMvc.perform(put("/stores/182").content(newStoreAsJSON).accept(MediaType.APPLICATION_JSON_VALUE)
				.contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isNotFound());
		
	}

    @Test   
     void testCreateStores() throws Exception {
    	Store newStore = StoreGenerator.generate(9852, "Store New", "Monte Carlo", "France");
    	List<Store> storeList = new ArrayList<Store>();
    	storeList.add(newStore);
    	
    	Stores newStores = new Stores();
    	newStores.setStores(storeList);
    	ObjectMapper mapper = new ObjectMapper();
		String newStoresAsJSON = mapper.writeValueAsString(newStores);	
		this.mockMvc.perform(post("/stores/").content(newStoresAsJSON).accept(MediaType.APPLICATION_JSON_VALUE)
				.contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isCreated());
		
				
    }
    
    @Test   
    void testCreateStoresExists() throws Exception {
   	Store newStore = StoreGenerator.generate(9852, "Store New", "Monte Carlo", "France");
   	List<Store> storeList = new ArrayList<Store>();
   	storeList.add(newStore);
   	
   	Stores newStores = new Stores();
   	newStores.setStores(storeList);
   	ObjectMapper mapper = new ObjectMapper();
		String newStoresAsJSON = mapper.writeValueAsString(newStores);
		given(this.storeService.findStoreById(9852)).willReturn(stores.getStores().get(0));
		this.mockMvc.perform(post("/stores/").content(newStoresAsJSON).accept(MediaType.APPLICATION_JSON_VALUE)
				.contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isBadRequest());
		
				
   }
    
    @Test   
    void testCreateStoresInvalid() throws Exception {
   	Store newStore = StoreGenerator.generate(9852, "Store New", "Monte Carlo", "France");
 
   	ObjectMapper mapper = new ObjectMapper();
		String newStoresAsJSON = mapper.writeValueAsString(newStore);
		given(this.storeService.findStoreById(9852)).willReturn(stores.getStores().get(0));
		this.mockMvc.perform(post("/stores/").content(newStoresAsJSON).accept(MediaType.APPLICATION_JSON_VALUE)
				.contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isInternalServerError());
		
				
   }
}