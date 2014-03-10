package br.com.caelum.vraptor.actioncache.tag;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;

import javax.enterprise.inject.spi.CDI;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import br.com.caelum.vraptor.actioncache.ActionCache;
import br.com.caelum.vraptor.actioncache.ActionCacheEntry;
import br.com.caelum.vraptor.actioncache.CacheKey;

/**
 * It should keep jsp fragment in cache. 
 * 
 * @author Alberto Souza(@asouza)
 *
 */
public class FragmentCache extends SimpleTagSupport {	

	private int duration;
	private String key;	
	private static ActionCache actionCache = CDI.current().select(ActionCache.class).get();

	@Override
	public void doTag() throws JspException, IOException {
		super.doTag();
		final StringWriter body = new StringWriter();
		CacheKey cacheKey = new CacheKey(key);
		ActionCacheEntry entry = actionCache.fetch(cacheKey);
		if (entry == null) {
			getJspBody().invoke(body);
			actionCache.write(cacheKey, new ActionCacheEntry(body.toString(), new HashMap<String, String>()));
		}
		getJspContext().getOut().println(actionCache.fetch(cacheKey).getResult());
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public void setKey(String key) {
		this.key = key;
	}
}
