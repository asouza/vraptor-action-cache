package br.com.caelum.vraptor.actioncache;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import br.com.caelum.vraptor.http.MutableRequest;

@RequestScoped
public class RequestHeaders {

	private Map<String, String> headers = new HashMap<>();

	/**
	 * @deprecated CDI eyes only
	 */
	public RequestHeaders() {
	}

	@Inject
	public RequestHeaders(MutableRequest mutableRequest) {
		super();
		Enumeration<String> headerNames = mutableRequest.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			String key = headerNames.nextElement();
			headers.put(key, mutableRequest.getHeader(key));
		}
	}

	public Map<String, String> get() {
		return headers;
	}
	
	public static RequestHeaders empty(){
		return new RequestHeaders();
	}

}
