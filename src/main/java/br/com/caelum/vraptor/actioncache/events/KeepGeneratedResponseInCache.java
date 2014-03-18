package br.com.caelum.vraptor.actioncache.events;

import java.util.concurrent.Callable;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	private static final Logger logger = LoggerFactory.getLogger(KeepGeneratedResponseInCache.class);

	@Deprecated
	public KeepGeneratedResponseInCache() {
	}

	@Inject
	public KeepGeneratedResponseInCache(MutableResponse response, ActionCache actionCache, RequestHeaders headers) {
		super();
		this.response = response;
		this.actionCache = actionCache;
		this.headers = headers;
	}

	public void execute(@Observes @CachedAction CachedMethodExecuted event) {
		final Cached cached = event.getCached();
		final CharArrayWriterResponse charResponse = ProxyTargetInstance.get(response);
		actionCache.fetch(new CacheKey(cached, headers), new Callable<ActionCacheEntry>() {

			@Override
			public ActionCacheEntry call() throws Exception {
				String result = charResponse.getOutput();
				logger.debug("Caching response of controller method with key {}",cached.key());
				return new ActionCacheEntry(result, charResponse.getHeaders());
			}
		});
	}

}
