package br.com.caelum.vraptor.actioncache;

import java.util.Map;
import java.util.Set;

import javax.enterprise.inject.Vetoed;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

@Vetoed
public class CacheKey {

	private String userKey;
	private Map<String, String> headers;
	private Set<String> cacheableHeaders = Sets.newHashSet("accept","accept-language");

	public CacheKey(String userKey, RequestHeaders requestHeaders) {
		this.userKey = userKey;
		this.headers = Maps.filterKeys(requestHeaders.get(),new Predicate<String>() {
			@Override
			public boolean apply(String key) {				
				return cacheableHeaders.contains(key.toLowerCase());
			}
		});
	}
	
	public Map<String, String> getHeaders() {
		return ImmutableMap.copyOf(headers);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((headers == null) ? 0 : headers.hashCode());
		result = prime * result + ((userKey == null) ? 0 : userKey.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CacheKey other = (CacheKey) obj;
		if (headers == null) {
			if (other.headers != null)
				return false;
		} else if (!headers.equals(other.headers))
			return false;
		if (userKey == null) {
			if (other.userKey != null)
				return false;
		} else if (!userKey.equals(other.userKey))
			return false;
		return true;
	}
	
	

}
