package br.com.caelum.vraptor.actioncache.events;

import java.io.IOException;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.caelum.vraptor.actioncache.ActionCache;
import br.com.caelum.vraptor.actioncache.ActionCacheEntry;
import br.com.caelum.vraptor.actioncache.CacheKey;
import br.com.caelum.vraptor.actioncache.Cached;
import br.com.caelum.vraptor.actioncache.CachedMethodExecuted;
import br.com.caelum.vraptor.actioncache.CharArrayWriterResponse;
import br.com.caelum.vraptor.actioncache.ProxyTargetInstance;
import br.com.caelum.vraptor.actioncache.RequestHeaders;
import br.com.caelum.vraptor.actioncache.WriteResponse;
import br.com.caelum.vraptor.http.MutableResponse;
import br.com.caelum.vraptor.view.ResultException;

@RequestScoped
public class WriteCachedResponse {

	private MutableResponse response;
	private ActionCache actionCache;
	private RequestHeaders requestHeaders;
	private static final Logger logger = LoggerFactory.getLogger(WriteCachedResponse.class);

	@Deprecated
	public WriteCachedResponse() {
	}

	@Inject
	public WriteCachedResponse(MutableResponse response,ActionCache actionCache,RequestHeaders requestHeaders) {
		super();
		this.response = response;
		this.actionCache = actionCache;
		this.requestHeaders = requestHeaders;
	}

	public void execute(@Observes @WriteResponse CachedMethodExecuted executed) {
		try {
			Cached cached = executed.getCached();
			CharArrayWriterResponse charArrayResponse = ProxyTargetInstance.get(response);
			ActionCacheEntry entry = actionCache.fetch(new CacheKey(cached,requestHeaders));
			HttpServletResponse originalResponse = charArrayResponse.delegate();
			entry.copyHeadersTo(originalResponse);
			logger.debug("Using cached response for {}", cached.key());
			originalResponse.getWriter().write(entry.getResult());
			originalResponse.flushBuffer();
		} catch (IOException e) {
			throw new ResultException(e);
		}
	}
}
