package br.com.caelum.vraptor.actioncache;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.Vector;

import org.mockito.Matchers;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.google.common.collect.ImmutableMap;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import br.com.caelum.vraptor.http.MutableRequest;

public class MutableRequestMocker {

	public static MutableRequest create(){
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
