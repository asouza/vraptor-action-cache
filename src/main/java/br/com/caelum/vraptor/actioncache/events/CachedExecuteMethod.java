package br.com.caelum.vraptor.actioncache.events;

import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Specializes;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import br.com.caelum.vraptor.actioncache.ActionCache;
import br.com.caelum.vraptor.actioncache.ActionCacheEntry;
import br.com.caelum.vraptor.actioncache.CacheKey;
import br.com.caelum.vraptor.actioncache.Cached;
import br.com.caelum.vraptor.actioncache.CachedActionBinding;
import br.com.caelum.vraptor.actioncache.CachedMethodExecuted;
import br.com.caelum.vraptor.actioncache.RequestHeaders;
import br.com.caelum.vraptor.actioncache.WriteResponseBinding;
import br.com.caelum.vraptor.core.MethodInfo;
import br.com.caelum.vraptor.events.InterceptorsExecuted;
import br.com.caelum.vraptor.events.MethodExecuted;
import br.com.caelum.vraptor.events.MethodReady;
import br.com.caelum.vraptor.observer.ExecuteMethod;
import br.com.caelum.vraptor.validator.Validator;

@Specializes
public class CachedExecuteMethod extends ExecuteMethod {

	private ActionCache actionCache;
	private Event<CachedMethodExecuted> cachedMethodExecutedEvent;
	private RequestHeaders requestHeaders;

	/**
	 * @deprecated CDI eyes only
	 */
	protected CachedExecuteMethod() {

	}

	@Inject
	public CachedExecuteMethod(MethodInfo methodInfo, Validator validator, Event<MethodExecuted> methodExecutedEvent,
			Event<MethodReady> readyToExecuteMethod, ActionCache actionCache, HttpServletResponse response,
			Event<CachedMethodExecuted> cachedMethodExecutedEvent, RequestHeaders requestHeaders) {
		super(methodInfo, validator, methodExecutedEvent, readyToExecuteMethod);
		this.actionCache = actionCache;
		this.cachedMethodExecutedEvent = cachedMethodExecutedEvent;
		this.requestHeaders = requestHeaders;
	}

	@Override
	public void execute(@Observes InterceptorsExecuted stack) {
		CachedMethodExecuted cachedMethodExecuted = new CachedMethodExecuted(stack.getControllerMethod());
		Cached cached = cachedMethodExecuted.getCached();
		if (cached == null) {
			super.execute(stack);
			return;
		}
		ActionCacheEntry body = actionCache.fetch(new CacheKey(cached, requestHeaders));
		if (body == null) {
			super.execute(stack);
		}
		cachedMethodExecutedEvent.select(new CachedActionBinding()).fire(cachedMethodExecuted);
		cachedMethodExecutedEvent.select(new WriteResponseBinding()).fire(cachedMethodExecuted);

	}
}
