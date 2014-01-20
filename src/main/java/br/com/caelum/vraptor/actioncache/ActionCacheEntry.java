package br.com.caelum.vraptor.actioncache;

import java.io.Serializable;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.enterprise.inject.Vetoed;
import javax.servlet.http.HttpServletResponse;

@Vetoed
public class ActionCacheEntry implements Serializable{

	private static final long serialVersionUID = -6392861477335598395L;
	private String result;
	private Map<String, String> headers;

	public ActionCacheEntry(String result, Map<String, String> headers) {
		this.result = result;
		this.headers = headers;
	}
	
	public String getResult() {
		return result;
	}
	
	public Map<String, String> getHeaders() {
		return headers;
	}

	public void copyHeadersTo(HttpServletResponse response) {
		Set<Entry<String, String>> usedHeaders = headers.entrySet();
		for (Entry<String, String> header : usedHeaders) {
			response.setHeader(header.getKey(),header.getValue());
		}
	}

}
