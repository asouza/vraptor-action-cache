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
import br.com.caelum.vraptor.actioncache.CachedMethodExecuted;
import br.com.caelum.vraptor.actioncache.RequestHeaders;
import br.com.caelum.vraptor.core.MethodInfo;
import br.com.caelum.vraptor.events.InterceptorsExecuted;
import br.com.caelum.vraptor.events.MethodExecuted;
import br.com.caelum.vraptor.events.MethodReady;
import br.com.caelum.vraptor.observer.ExecuteMethod;
import br.com.caelum.vraptor.validator.Messages;
import br.com.caelum.vraptor.validator.Validator;

@Specializes
public class CachedExecuteMethod extends ExecuteMethod {

	private ActionCache actionCache;
	private RequestHeaders requestHeaders;

	@Inject
	public CachedExecuteMethod(MethodInfo methodInfo, Messages messages, Event<MethodExecuted> methodExecutedEvent,
			Event<MethodReady> readyToExecuteMethod, ActionCache actionCache, HttpServletResponse response,
			RequestHeaders requestHeaders) {
		super(methodInfo, messages, methodExecutedEvent, readyToExecuteMethod);
		this.actionCache = actionCache;
		this.requestHeaders = requestHeaders;
	}

	@Override
	public void execute(@Observes final InterceptorsExecuted stack) {
		ExecuteIfNoCache executeIfNoCache = new ExecuteIfNoCache(stack.getControllerMethod(),actionCache,requestHeaders);
		executeIfNoCache.execute(new Runnable() {			
			@Override
			public void run() {
				CachedExecuteMethod.super.execute(stack);				
			}
		});
			

	}

}
