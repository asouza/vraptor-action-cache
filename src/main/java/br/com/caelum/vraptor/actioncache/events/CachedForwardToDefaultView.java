package br.com.caelum.vraptor.actioncache.events;

import java.util.concurrent.Callable;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.jboss.weld.interceptor.util.proxy.TargetInstanceProxy;

import br.com.caelum.vraptor.actioncache.ActionCache;
import br.com.caelum.vraptor.actioncache.Cached;
import br.com.caelum.vraptor.actioncache.CachedAction;
import br.com.caelum.vraptor.actioncache.CharArrayWriterResponse;
import br.com.caelum.vraptor.core.MethodInfo;
import br.com.caelum.vraptor.events.MethodExecuted;
import br.com.caelum.vraptor.http.MutableResponse;

@RequestScoped
public class CachedForwardToDefaultView {

	private MutableResponse response;
	private MethodInfo methodInfo;
	private ActionCache actionCache;

	@Deprecated
	public CachedForwardToDefaultView() {
	}

	@Inject
	public CachedForwardToDefaultView(MutableResponse response, MethodInfo methodInfo, ActionCache actionCache) {
		super();
		this.response = response;
		this.methodInfo = methodInfo;
		this.actionCache = actionCache;
	}

	public void forward(@Observes @CachedAction MethodExecuted event) {
		Cached cached = methodInfo.getControllerMethod().getMethod().getAnnotation(Cached.class);
		actionCache.fetch(cached.key(), new Callable<String>() {

			@Override
			public String call() throws Exception {
				// TODO Auto-generated method stub
				TargetInstanceProxy proxy = (TargetInstanceProxy)response;
				return ((CharArrayWriterResponse)proxy.getTargetInstance()).getOutput();
			}
		});
	}

}
