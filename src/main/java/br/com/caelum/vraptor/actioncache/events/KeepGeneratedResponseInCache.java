package br.com.caelum.vraptor.actioncache.events;

import java.util.concurrent.Callable;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import br.com.caelum.vraptor.actioncache.ActionCache;
import br.com.caelum.vraptor.actioncache.ActionCacheEntry;
import br.com.caelum.vraptor.actioncache.CacheKey;
import br.com.caelum.vraptor.actioncache.Cached;
import br.com.caelum.vraptor.actioncache.CachedAction;
import br.com.caelum.vraptor.actioncache.CachedMethodExecuted;
import br.com.caelum.vraptor.actioncache.CharArrayWriterResponse;
import br.com.caelum.vraptor.actioncache.ProxyTargetInstance;
import br.com.caelum.vraptor.actioncache.RequestHeaders;
import br.com.caelum.vraptor.http.MutableResponse;

@RequestScoped
public class KeepGeneratedResponseInCache {

	private MutableResponse response;
	private ActionCache actionCache;
	private RequestHeaders headers;

	@Deprecated
	public KeepGeneratedResponseInCache() {
	}

	@Inject
	public KeepGeneratedResponseInCache(MutableResponse response, ActionCache actionCache,RequestHeaders headers) {
		super();
		this.response = response;
		this.actionCache = actionCache;
		this.headers = headers;
	}

	public void execute(@Observes @CachedAction CachedMethodExecuted event) {
		Cached cached = event.getCached();
		final CharArrayWriterResponse charResponse = ProxyTargetInstance.get(response);
		actionCache.fetch(new CacheKey(cached,headers), new Callable<ActionCacheEntry>() {

			@Override
			public ActionCacheEntry call() throws Exception {
				String result = charResponse.getOutput();				
				return new ActionCacheEntry(result,charResponse.getHeaders());
			}
		});
	}

}
