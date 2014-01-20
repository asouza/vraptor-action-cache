package br.com.caelum.vraptor.actioncache.events;

import java.io.IOException;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import org.jboss.weld.interceptor.util.proxy.TargetInstanceProxy;

import br.com.caelum.vraptor.actioncache.ActionCache;
import br.com.caelum.vraptor.actioncache.ActionCacheEntry;
import br.com.caelum.vraptor.actioncache.Cached;
import br.com.caelum.vraptor.actioncache.CachedMethodExecuted;
import br.com.caelum.vraptor.actioncache.CharArrayWriterResponse;
import br.com.caelum.vraptor.actioncache.WriteResponse;
import br.com.caelum.vraptor.http.MutableResponse;
import br.com.caelum.vraptor.view.ResultException;

@RequestScoped
public class WriteCachedResponse {

	private MutableResponse response;
	private ActionCache actionCache;

	@Deprecated
	public WriteCachedResponse() {
	}

	@Inject
	public WriteCachedResponse(MutableResponse response,ActionCache actionCache) {
		super();
		this.response = response;
		this.actionCache = actionCache;
	}

	public void execute(@Observes @WriteResponse CachedMethodExecuted executed) {
		try {
			Cached cached = executed.getCached();
			TargetInstanceProxy proxy = (TargetInstanceProxy)response;
			CharArrayWriterResponse charArrayResponse = (CharArrayWriterResponse) proxy.getTargetInstance();
			ActionCacheEntry entry = actionCache.fetch(cached.key());
			HttpServletResponse originalResponse = charArrayResponse.delegate();
			entry.copyHeadersTo(originalResponse);
			originalResponse.getWriter().print(entry.getResult());
		} catch (IOException e) {
			throw new ResultException(e);
		}
	}
}
