package org.springboot.storelocator.service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.springboot.storelocator.constants.StoreLocatorConstants;
import org.springboot.storelocator.model.Store;
import org.springboot.storelocator.model.Stores;
import org.springboot.storelocator.util.StoreQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Rajesh S Nair
 *
 */

@Service
@EnableCaching
public class StoreServiceImpl implements StoreService {

	private static final String CLASSNAME = StoreServiceImpl.class.getName();
	private static final Logger LOGGER = Logger.getLogger(CLASSNAME);
	
	@Value("classpath:/data/store-data.json")
	Resource resourceData;
	
	@Autowired
	StoreQueryBuilder storeQueryBuilder;
	

	/**
	 * 
	 * Method to save a store or multiple stores to json data file
	 * 
	 */
	@Override
	
	public boolean saveStores(Stores newStores) throws IOException {
		
		String methodName = "saveStore";
		LOGGER.entering(CLASSNAME, methodName);
		List<Store> storesList;
		Stores currentStores;

		ObjectMapper mapper = new ObjectMapper();	
		

		currentStores = mapper.readValue(resourceData.getInputStream(), Stores.class);
		
		
		storesList = currentStores.getStores();

		storesList.addAll(newStores.getStores());

		currentStores.setStores(storesList);
		
		mapper.writeValue(new File(resourceData.getURL().getPath()), currentStores);

		LOGGER.exiting(CLASSNAME, methodName);
		return true;

	}

	/**
	 *Method to find a store by storeId
	 * 
	 */
	@Override
	public Store findStoreById(int storeId) throws IOException {
		String methodName = "findStoreById";
		LOGGER.entering(CLASSNAME, methodName);
		Stores stores;
		ObjectMapper mapper = new ObjectMapper();
		stores = mapper.readValue(resourceData.getInputStream(), Stores.class);

		Optional<Store> store = stores.getStores().stream().filter(st -> st.getStoreId().equals(storeId)).findFirst();

		if (store.isPresent()) {
			LOGGER.exiting(CLASSNAME, methodName);
			return store.get();
		} else {
			LOGGER.exiting(CLASSNAME, methodName);
			return null;
		}
	}

	/**
	 * 
	 * Method to delete a store using storeId
	 * 
	 */
	@Override
	
	public void deleteStore(Store store) throws IOException {
		String methodName = "deleteStore";
		LOGGER.entering(CLASSNAME, methodName);
		Stores stores;
		ObjectMapper mapper = new ObjectMapper();
		stores = mapper.readValue(resourceData.getInputStream(), Stores.class);
		
		stores.getStores().removeIf(st -> st.getStoreId().equals(store.getStoreId()));
		mapper.writeValue(new File(resourceData.getURL().getPath()), stores);
		LOGGER.exiting(CLASSNAME, methodName);

	}

	/**
	 * Method to update store details for a store based on storeId
	 * 
	 */
	@Override	
	public void updateStore(Store store) throws IOException {
		String methodName = "updateStore";
		LOGGER.entering(CLASSNAME, methodName);
		Stores stores;
		ObjectMapper mapper = new ObjectMapper();
		
		stores = mapper.readValue(resourceData.getFile(), Stores.class);
		stores.setStores(stores.getStores().stream().map(st -> st.getStoreId().equals(store.getStoreId()) ? store : st)
				.collect(Collectors.toList()));

		mapper.writeValue(new File(resourceData.getURL().getPath()), stores);
		LOGGER.exiting(CLASSNAME, methodName);
	}

	/**
	 * Method to get stores based on the queryparams provided
	 * if no query params provided all stores in json data file is returned
	 * 
	 */
	@Override
	@CacheEvict(value="store-cache", key = "#queryParams",
	condition = "#isCacheable == null || !#isCacheable", beforeInvocation = true)
@Cacheable(value="store-cache", key = "#queryParams", 
	condition = "#isCacheable != null && #isCacheable")
	public Stores getStores(MultiValueMap<String, String> queryParams,boolean isCacheable) throws IOException {

		String methodName = "getStores";
		LOGGER.entering(CLASSNAME, methodName);
		
		Stores stores;
		ObjectMapper mapper = new ObjectMapper();
	
		stores = mapper.readValue(resourceData.getFile(), Stores.class);
		
		
		

		if (queryParams.containsKey(StoreLocatorConstants.FILTER_CITY)) {
			String city = queryParams.getFirst(StoreLocatorConstants.FILTER_CITY);
			stores.setStores(stores.getStores().stream().filter(st -> st.getCity().equalsIgnoreCase(city))
					.collect(Collectors.toList()));

		} else if (queryParams.containsKey(StoreLocatorConstants.FILTER_COUNTRY)) {
			String country = queryParams.getFirst(StoreLocatorConstants.FILTER_COUNTRY);
			stores.setStores(stores.getStores().stream().filter(st -> st.getCountry().equalsIgnoreCase(country))
					.collect(Collectors.toList()));
		}

		else if ((queryParams.containsKey(StoreLocatorConstants.FILTER_LATITUDE)
				&& queryParams.containsKey(StoreLocatorConstants.FILTER_LONGITUDE))
				|| (queryParams.containsKey(StoreLocatorConstants.FILTER_LOCATION)))
				 {

			stores = storeQueryBuilder.buildQuery(queryParams, stores);

		}

		else if (queryParams.containsKey(StoreLocatorConstants.FILTER_CURRENT)) {
			String current = queryParams.getFirst(StoreLocatorConstants.FILTER_CURRENT);
			if (null != current && current.equalsIgnoreCase("true")) {

				currentOpenStores(stores);

			}

		}

		
		LOGGER.exiting(CLASSNAME, methodName);
		return stores;
	}

	/**
	 * Method to get list of currently open stores
	 * 
	 * @param stores
	 */
	private void currentOpenStores(Stores stores) {
		LocalDate localDate = LocalDate.now();
		java.time.DayOfWeek dayOfWeek = localDate.getDayOfWeek();

		String currentDay = dayOfWeek.toString().substring(0, 3);

		stores.setStores(stores.getStores().stream().filter(st -> {
			for (String open : st.getStlocattr().getOpeninghours().split("~~")) {
				if (!open.contains("CLOSED") && open.contains(currentDay)
						&&
						LocalTime.now().isBefore(LocalTime.parse(open.substring(10, 15)))
						&& LocalTime.now().isAfter(LocalTime.parse(open.substring(4, 9))))				
						
						

							return true;
					
			}
			return false;

		}).collect(Collectors.toList()));
	}

}