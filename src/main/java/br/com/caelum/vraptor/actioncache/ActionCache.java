package br.com.caelum.vraptor.actioncache;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import br.com.caelum.vraptor.cache.CacheStore;

@ApplicationScoped
public class ActionCache {

	@Inject
	private CacheStore<String, String> cache;

	@Inject
	public ActionCache(CacheStore<String, String> cache) {
		super();
		this.cache = cache;
	}

	@Deprecated
	public ActionCache() {
	}

	public String write(String key, String value) {
		return cache.write(key, value);
	}

	public String fetch(String key) {
		return cache.fetch(key);
	}
	
	
	

}
