package br.com.caelum.vraptor.actioncache.events;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import br.com.caelum.vraptor.actioncache.ActionCache;
import br.com.caelum.vraptor.actioncache.ActionCacheEntry;
import br.com.caelum.vraptor.actioncache.CacheKey;
import br.com.caelum.vraptor.actioncache.Cached;
import br.com.caelum.vraptor.actioncache.MutableRequestMocker;
import br.com.caelum.vraptor.actioncache.RequestHeaders;
import br.com.caelum.vraptor.cache.DefaultCacheStore;
import br.com.caelum.vraptor.controller.ControllerMethod;
import br.com.caelum.vraptor.http.MutableRequest;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ExecuteIfNoCacheTest {

	private MutableRequest mutableRequest;
	private RequestHeaders requestHeaders;
	private ControllerMethod controllerMethod;
	private ActionCache actionCache;

	@Before
	public void setup() {
		mutableRequest = MutableRequestMocker.create();
		requestHeaders = new RequestHeaders(mutableRequest);
		controllerMethod = mock(ControllerMethod.class);
		actionCache = new ActionCache(new DefaultCacheStore<CacheKey, ActionCacheEntry>());

	}

	@Test
	public void shouldRunOnlyThereIsNoCache() throws Exception {
		when(controllerMethod.getMethod()).thenReturn(Controller.class.getMethod("notCached"));

		ExecuteIfNoCache executeIfNoCache = new ExecuteIfNoCache(controllerMethod, actionCache, requestHeaders);
		Runnable runnable = mock(Runnable.class);
		executeIfNoCache.execute(runnable);
		verify(runnable, times(1)).run();
		;
	}

	@Test
	public void shouldRunIfThereIsNoEntryInCache() throws Exception {
		when(controllerMethod.getMethod()).thenReturn(Controller.class.getMethod("cached"));

		ExecuteIfNoCache executeIfNoCache = new ExecuteIfNoCache(controllerMethod, actionCache, requestHeaders);
		Runnable runnable = mock(Runnable.class);
		executeIfNoCache.execute(runnable);
		verify(runnable, times(1)).run();
		;
	}

	@Test
	public void shouldNotRunIfThereIsAEntryInCache() throws Exception {
		when(controllerMethod.getMethod()).thenReturn(Controller.class.getMethod("cached"));
		Cached cached = Controller.class.getDeclaredMethod("cached").getAnnotation(Cached.class);
		actionCache.write(new CacheKey(cached, requestHeaders),
				new ActionCacheEntry("<html></html>", requestHeaders.get()));

		ExecuteIfNoCache executeIfNoCache = new ExecuteIfNoCache(controllerMethod, actionCache, requestHeaders);
		Runnable runnable = mock(Runnable.class);
		executeIfNoCache.execute(runnable);
		verify(runnable, never()).run();
	}

	private class Controller {
		@Cached(key = "test")
		public void cached() {

		}

		public void notCached() {

		}
	}
}
