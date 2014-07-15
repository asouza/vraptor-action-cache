package br.com.caelum.vraptor.actioncache;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import br.com.caelum.vraptor.http.MutableRequest;

public class EhCacheStoreTest {
	
	private static EhCacheStore ehCacheStore = new EhCacheStore();
	private MutableRequest mutableRequest;
	private RequestHeaders requestHeaders;
	
	@BeforeClass
	public static void setupClass(){
		ehCacheStore.setup();
	}
	
	@AfterClass
	public static void shutdown(){
		ehCacheStore.destroy();
	}
	
	@Before
	public void setup(){
		mutableRequest = MutableRequestMocker.create();
		requestHeaders = new RequestHeaders(mutableRequest);			
	}

	@Test
	public void shouldRespectDurationTime() throws Exception{
		
		Cached cached = Controller.class.getDeclaredMethod("cachedWithDuration").getAnnotation(Cached.class);
		
		CacheKey key = new CacheKey(cached, requestHeaders);
		ehCacheStore.write(key,
				new ActionCacheEntry("<html></html>", requestHeaders.get()));
		
		assertNotNull(ehCacheStore.fetch(key));
		
		Thread.sleep(11000);
		
		assertNull(ehCacheStore.fetch(key));
		
	}
	
	@Test
	public void shouldRespectIdleTime() throws Exception{
		Cached cached = Controller.class.getDeclaredMethod("cachedWithIdleTime").getAnnotation(Cached.class);
		
		CacheKey key = new CacheKey(cached, requestHeaders);
		ehCacheStore.write(key,
				new ActionCacheEntry("<html></html>", requestHeaders.get()));
		
		assertNotNull(ehCacheStore.fetch(key));
		
		Thread.sleep(11000);
		
		assertNull(ehCacheStore.fetch(key));
		
	}
	
	private class Controller {
		@Cached(key = "test",duration=10)
		public void cachedWithDuration() {

		}
		
		@Cached(key = "test2",idleTime=10)
		public void cachedWithIdleTime() {
			
		}

		public void notCached() {

		}
	}	
}
