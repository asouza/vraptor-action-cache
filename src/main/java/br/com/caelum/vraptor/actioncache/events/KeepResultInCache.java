package br.com.caelum.vraptor.actioncache.events;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.actioncache.ActionCache;
import br.com.caelum.vraptor.actioncache.ActionCacheEntry;
import br.com.caelum.vraptor.actioncache.CacheKey;
import br.com.caelum.vraptor.actioncache.Cached;
import br.com.caelum.vraptor.actioncache.CachedActionBinding;
import br.com.caelum.vraptor.actioncache.CachedMethodExecuted;
import br.com.caelum.vraptor.actioncache.RequestHeaders;
import br.com.caelum.vraptor.actioncache.WriteResponseBinding;
import br.com.caelum.vraptor.controller.ControllerMethod;
import br.com.caelum.vraptor.events.ControllerFound;
import br.com.caelum.vraptor.events.RequestSucceded;
import br.com.caelum.vraptor.observer.ForwardToDefaultView;

/**
 * This class depends of internal flow os events in VRaptor. Any change will
 * break the current behavior.
 * 
 * @author Alberto Souza
 * 
 */
@RequestScoped
public class KeepResultInCache extends ForwardToDefaultView {

	private Event<CachedMethodExecuted> cachedMethodExecutedEvent;
	private CachedMethodExecuted cachedMethodExecuted;
	private ActionCache actionCache;
	private RequestHeaders requestHeaders;
	private ControllerMethod controllerMethod;

	@Inject
	public KeepResultInCache(Event<CachedMethodExecuted> cachedMethodExecutedEvent, Result result,
			ControllerMethod controllerMethod,ActionCache actionCache,RequestHeaders requestHeaders) {
		super(result);
		this.cachedMethodExecutedEvent = cachedMethodExecutedEvent;
		this.controllerMethod = controllerMethod;
		this.actionCache = actionCache;
		this.requestHeaders = requestHeaders;
		this.cachedMethodExecuted = new CachedMethodExecuted(controllerMethod);
	}

	/**
	 * @deprecated CDI eyes only
	 */
	protected KeepResultInCache() {
	}

	@Override
	public void forward(@Observes final RequestSucceded event) {
		ExecuteIfNoCache executeIfNoCache = new ExecuteIfNoCache(controllerMethod,
				actionCache, requestHeaders);
		
		executeIfNoCache.execute(new Runnable() {
			@Override
			public void run() {
				KeepResultInCache.super.forward(event);
			}
		});

		cachedMethodExecutedEvent.select(new CachedActionBinding()).fire(cachedMethodExecuted);
		cachedMethodExecutedEvent.select(new WriteResponseBinding()).fire(cachedMethodExecuted);
	}
}
