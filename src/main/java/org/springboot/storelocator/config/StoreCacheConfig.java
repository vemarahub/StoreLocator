package org.springboot.storelocator.config;


import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



import net.sf.ehcache.config.CacheConfiguration;

@Configuration
@EnableCaching
public class StoreCacheConfig extends CachingConfigurerSupport {

	@Bean
	public StoreFilter storeFilter() {
		return new StoreFilter();
	}

	@Bean
	public net.sf.ehcache.CacheManager ehCacheManager() {
		CacheConfiguration locationCache = new CacheConfiguration();
		locationCache.setName("location-cache");
		locationCache.setMemoryStoreEvictionPolicy("LRU");
		locationCache.setMaxEntriesLocalHeap(1000);
		locationCache.setTimeToLiveSeconds(20);

		CacheConfiguration storeCache = new CacheConfiguration();
		storeCache.setName("store-cache");
		storeCache.setMemoryStoreEvictionPolicy("LRU");
		storeCache.setMaxEntriesLocalHeap(1000);
		storeCache.setTimeToLiveSeconds(20);

		net.sf.ehcache.config.Configuration config = new net.sf.ehcache.config.Configuration();
		config.addCache(locationCache);
		config.addCache(storeCache);
		return net.sf.ehcache.CacheManager.newInstance(config);
	}

	@Bean
	@Override
	public CacheManager cacheManager() {
		return new EhCacheCacheManager(ehCacheManager());
	}
}