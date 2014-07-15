package br.com.caelum.vraptor.actioncache;

import java.util.concurrent.Callable;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import br.com.caelum.vraptor.cache.CacheStore;

@ApplicationScoped
public class ActionCache {

	private CacheStore<CacheKey, ActionCacheEntry> cache;

	@Inject	
	public ActionCache(@EhCacheVersion CacheStore<CacheKey, ActionCacheEntry> cache) {
		super();
		this.cache = cache;
	}

	@Deprecated
	public ActionCache() {
	}

	public ActionCacheEntry write(CacheKey key, ActionCacheEntry value) {
		return cache.write(key, value);
	}

	public ActionCacheEntry fetch(CacheKey key) {
		return cache.fetch(key);
	}

	public ActionCacheEntry fetch(CacheKey key, Callable<ActionCacheEntry> valueProvider) {
		return cache.fetch(key, valueProvider);
	}

	
	
	
	
	

}
