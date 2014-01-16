package br.com.caelum.vraptor.actioncache.events;

import java.io.IOException;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.jboss.weld.interceptor.util.proxy.TargetInstanceProxy;

import com.sun.corba.se.spi.orbutil.fsm.Action;

import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.actioncache.ActionCache;
import br.com.caelum.vraptor.actioncache.Cached;
import br.com.caelum.vraptor.actioncache.CachedMethodExecuted;
import br.com.caelum.vraptor.actioncache.CharArrayWriterResponse;
import br.com.caelum.vraptor.actioncache.WriteResponse;
import br.com.caelum.vraptor.core.MethodInfo;
import br.com.caelum.vraptor.events.MethodExecuted;
import br.com.caelum.vraptor.http.MutableResponse;
import br.com.caelum.vraptor.view.ResultException;

@RequestScoped
public class WriteCachedResponse {

	private MutableResponse response;
	private ActionCache actionCache;
	private MethodInfo methodInfo;

	@Deprecated
	public WriteCachedResponse() {
	}

	@Inject
	public WriteCachedResponse(MutableResponse response,ActionCache actionCache,MethodInfo methodInfo) {
		super();
		this.response = response;
		this.actionCache = actionCache;
		this.methodInfo = methodInfo;
	}

	public void execute(@Observes @WriteResponse CachedMethodExecuted executed) {
		try {
			Cached cached = methodInfo.getControllerMethod().getMethod().getAnnotation(Cached.class);
			TargetInstanceProxy proxy = (TargetInstanceProxy)response;
			CharArrayWriterResponse charArrayResponse = (CharArrayWriterResponse) proxy.getTargetInstance();
			charArrayResponse.delegate().getWriter().print(actionCache.fetch(cached.key()));
		} catch (IOException e) {
			throw new ResultException(e);
		}
	}
}
