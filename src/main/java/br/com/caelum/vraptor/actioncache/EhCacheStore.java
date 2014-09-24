package br.com.caelum.vraptor.actioncache;

import java.util.concurrent.Callable;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;

import com.google.common.base.Supplier;
import com.google.common.base.Throwables;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import br.com.caelum.vraptor.cache.CacheException;
import br.com.caelum.vraptor.cache.CacheStore;

@ApplicationScoped
@EhCacheVersion
public class EhCacheStore implements CacheStore<CacheKey, ActionCacheEntry>{
	
	private CacheManager cacheManager = CacheManager.create();
	private Cache cache;
	
	@PostConstruct
	public void setup(){
		cacheManager.addCache("pages");
		cache = cacheManager.getCache("pages");
	}
	
	@PreDestroy
	public void destroy(){
		cacheManager.shutdown();
	}

	@Override
	public ActionCacheEntry write(CacheKey key, ActionCacheEntry value) {
		Element element = new Element(key,value,false, key.getIdleTime(), key.getDuration());
		cache.put(element);
		return value;
	}

	@Override
	public ActionCacheEntry fetch(CacheKey key) {
		Element element = cache.get(key);
		return  element != null ? (ActionCacheEntry) element.getValue() : null;
	}

	@Override
	public ActionCacheEntry fetch(CacheKey key, Supplier<ActionCacheEntry> valueProvider) {
		if(cache.isKeyInCache(key)){
			return (ActionCacheEntry) cache.get(key).getValue();
		}
		try {
			ActionCacheEntry entry = valueProvider.get();
			return write(key, entry);
		} catch (Exception e) {
			Throwables.propagateIfPossible(e);
			throw new CacheException("Error computing the value", e);
		}
	}

}
