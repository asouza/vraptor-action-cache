package br.com.caelum.vraptor.actioncache;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.Vector;

import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import br.com.caelum.vraptor.http.MutableRequest;

import com.google.common.collect.ImmutableMap;

import static org.junit.Assert.assertTrue;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CacheKeyTest {

	@Test
	public void shouldKeepOnlyAcceptsHeaders() throws Exception {
		final MutableRequest mutableRequest = request();

		RequestHeaders requestHeaders = new RequestHeaders(mutableRequest);
		Cached cached = this.getClass().getDeclaredMethod("cachedHeaders").getAnnotation(Cached.class);
		CacheKey cacheKey = new CacheKey(cached,
				requestHeaders);
		assertTrue(cacheKey.getHeaders().containsKey("Accept") && cacheKey.getHeaders().containsKey("Accept-Language")
				&& !cacheKey.getHeaders().containsKey("Connection"));
	}
	
	@Test
	public void shouldKeepOnlyKeyForNoHeaderOption() throws Exception{
		final MutableRequest mutableRequest = request();
		
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

	private MutableRequest request() {
		final MutableRequest mutableRequest = mock(MutableRequest.class);
		final Enumeration<String> headersNames = new Vector<String>(Arrays.asList("Accept", "Accept-Language",
				"Connection", "Cache-Control")).elements();
		final ImmutableMap<String, String> headers = ImmutableMap.<String, String> of("Accept", "text/html",
				"Accept-Language", "pt-br", "Cache-Control", "max-age=0", "Connection", "keep-alive");
		when(mutableRequest.getHeaderNames()).thenReturn(headersNames);
		when(mutableRequest.getHeader(Matchers.anyString())).thenAnswer(new Answer<String>() {

			@Override
			public String answer(final InvocationOnMock invocation) throws Throwable {
				final String key = (String) invocation.getArguments()[0];
				return headers.get(key);
			}
		});
		return mutableRequest;
	}

}
