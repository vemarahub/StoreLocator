package org.springboot.storelocator;

import org.springboot.storelocator.model.Location;
import org.springboot.storelocator.model.Stlocattr;
import org.springboot.storelocator.model.Store;

public class StoreGenerator {
    public static Store generate() {
        Store testStore = new Store();

        testStore.setStoreId(100);
        testStore.setStoreName("Store Marvel");
        testStore.setCity("New York");
        testStore.setCountry("USA");
        testStore.setCountrycode("US");
        testStore.setPhone("+1 2346453245");
        testStore.setZipcode("9877");
        testStore.setAddress1("32,26 avenue,NY");
        Location location = new Location();
        location.setLat("40.22");
        location.setLng("60.34");
        Stlocattr stlocattr = new Stlocattr();
        stlocattr.setLanguageId(100);
        stlocattr.setOpeninghours(
                "MON 09:00-21:00~~TUE 09:00-21:00~~WED 09:00-21:00~~THU 09:00-21:00~~FRI 09:00-21:00~~SAT 09:00-21:00~~SUN 09:00-21:00");
        testStore.setLocation(location);
        testStore.setStlocattr(stlocattr);
        return testStore;
    }


    public static Store generate(int id,String name, String city, String country) {
        Store store = new Store();
        store.setStoreId(id);
        store.setStoreName(name);
        store.setCity(city);
        store.setCountry(country);
        store.setCountrycode("US");
        store.setPhone("+1 2346453245");
        store.setZipcode("9877");
        store.setAddress1("32,26 avenue,NY");
        Location location = new Location();
        location.setLat("40.22");
        location.setLng("60.34");
        Stlocattr stlocattr = new Stlocattr();
        stlocattr.setLanguageId(100);
        stlocattr.setOpeninghours(
                "MON 09:00-21:00~~TUE 09:00-21:00~~WED 09:00-21:00~~THU 09:00-21:00~~FRI 09:00-21:00~~SAT 09:00-21:00~~SUN 09:00-21:00");
        store.setLocation(location);
        store.setStlocattr(stlocattr);
        
        return store;
    }
}