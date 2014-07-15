package br.com.caelum.vraptor.actioncache.events;

import javax.enterprise.inject.Vetoed;

import br.com.caelum.vraptor.actioncache.ActionCache;
import br.com.caelum.vraptor.actioncache.ActionCacheEntry;
import br.com.caelum.vraptor.actioncache.CacheKey;
import br.com.caelum.vraptor.actioncache.Cached;
import br.com.caelum.vraptor.actioncache.CachedMethodExecuted;
import br.com.caelum.vraptor.actioncache.RequestHeaders;
import br.com.caelum.vraptor.controller.ControllerMethod;

@Vetoed
public class ExecuteIfNoCache {

	private ControllerMethod controllerMethod;
	private ActionCache actionCache;
	private RequestHeaders requestHeaders;

	public ExecuteIfNoCache(ControllerMethod controllerMethod, ActionCache actionCache,
			RequestHeaders requestHeaders) {
		super();
		this.controllerMethod = controllerMethod;
		this.actionCache = actionCache;
		this.requestHeaders = requestHeaders;
	}

	public <ParamterType> void execute(Runnable runnable) {
		CachedMethodExecuted cachedMethodExecuted = new CachedMethodExecuted(controllerMethod);
		Cached cached = cachedMethodExecuted.getCached();
		if (cached == null) {
			runnable.run();
			return;
		}
		ActionCacheEntry body = actionCache.fetch(new CacheKey(cached, requestHeaders));
		if (body == null) {
			runnable.run();
		}
	}

}
