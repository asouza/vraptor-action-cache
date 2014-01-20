package br.com.caelum.vraptor.actioncache;

import java.util.concurrent.Callable;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import br.com.caelum.vraptor.cache.CacheStore;

@ApplicationScoped
public class ActionCache {

	@Inject
	private CacheStore<String, ActionCacheEntry> cache;

	@Inject
	public ActionCache(CacheStore<String, ActionCacheEntry> cache) {
		super();
		this.cache = cache;
	}

	@Deprecated
	public ActionCache() {
	}

	public ActionCacheEntry write(String key, ActionCacheEntry value) {
		return cache.write(key, value);
	}

	public ActionCacheEntry fetch(String key) {
		return cache.fetch(key);
	}

	public ActionCacheEntry fetch(String key, Callable<ActionCacheEntry> writeFunction) {
		return cache.fetch(key, writeFunction);
	}
	
	
	
	
	

}
