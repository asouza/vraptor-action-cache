package br.com.caelum.vraptor.actioncache;

import org.junit.Test;

import br.com.caelum.vraptor.http.MutableRequest;

import static org.junit.Assert.assertTrue;

public class CacheKeyTest {

	@Test
	public void shouldKeepOnlyAcceptsHeaders() throws Exception {
		final MutableRequest mutableRequest = MutableRequestMocker.create();

		RequestHeaders requestHeaders = new RequestHeaders(mutableRequest);
		Cached cached = this.getClass().getDeclaredMethod("cachedHeaders").getAnnotation(Cached.class);
		CacheKey cacheKey = new CacheKey(cached,
				requestHeaders);
		assertTrue(cacheKey.getHeaders().containsKey("Accept") && cacheKey.getHeaders().containsKey("Accept-Language")
				&& !cacheKey.getHeaders().containsKey("Connection"));
	}
	
	@Test
	public void shouldKeepOnlyKeyForNoHeaderOption() throws Exception{
		final MutableRequest mutableRequest = MutableRequestMocker.create();
		
		RequestHeaders requestHeaders = new RequestHeaders(mutableRequest);
		Cached cached = this.getClass().getDeclaredMethod("noCachedHeaders").getAnnotation(Cached.class);
		CacheKey cacheKey = new CacheKey(cached,
				requestHeaders);
		assertTrue(cacheKey.getHeaders().keySet().isEmpty());		
	}
	
	@Cached(key = "testkey")
	private void cachedHeaders(){}
	
	@Cached(key = "testkey",headers=false)
	private void noCachedHeaders(){}


}
