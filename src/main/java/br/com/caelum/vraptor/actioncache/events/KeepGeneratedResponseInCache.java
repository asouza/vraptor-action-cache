package br.com.caelum.vraptor.actioncache.events;

import java.util.concurrent.Callable;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import br.com.caelum.vraptor.actioncache.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.caelum.vraptor.http.MutableResponse;

@RequestScoped
public class KeepGeneratedResponseInCache {

	private MutableResponse response;
	private ActionCache actionCache;
	private RequestHeaders headers;
	private Event<CachedMethodExecuted> event;
	private static final Logger logger = LoggerFactory.getLogger(KeepGeneratedResponseInCache.class);

	@Deprecated
	public KeepGeneratedResponseInCache() {
	}

	@Inject
	public KeepGeneratedResponseInCache(MutableResponse response, ActionCache actionCache, RequestHeaders headers,
										Event<CachedMethodExecuted> event) {
		super();
		this.response = response;
		this.actionCache = actionCache;
		this.headers = headers;
		this.event = event;
	}

	public void execute(@Observes @CachedAction CachedMethodExecuted cachedMethodExecuted) {
		final Cached cached = cachedMethodExecuted.getCached();
		final CharArrayWriterResponse charResponse = ProxyTargetInstance.get(response);
		CacheKey key = new CacheKey(cached, headers);
		if (actionCache.exists(key)) {
			event.select(new WriteResponseBinding()).fire(cachedMethodExecuted);
		}
		actionCache.fetch(key, new Callable<ActionCacheEntry>() {

			@Override
			public ActionCacheEntry call() throws Exception {
				String result = charResponse.getOutput();
				logger.debug("Caching response of controller method with key {}",cached.key());
				return new ActionCacheEntry(result, charResponse.getHeaders());
			}
		});
	}

}
