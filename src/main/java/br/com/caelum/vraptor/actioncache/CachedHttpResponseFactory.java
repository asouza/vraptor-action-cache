package br.com.caelum.vraptor.actioncache;

import javax.annotation.Priority;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Alternative;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.interceptor.Interceptor;

import br.com.caelum.vraptor.core.MethodInfo;
import br.com.caelum.vraptor.http.MutableResponse;
import br.com.caelum.vraptor.ioc.cdi.CDIRequestFactories;

@RequestScoped
@Alternative
@Priority(Interceptor.Priority.LIBRARY_AFTER)
public class CachedHttpResponseFactory{

	@Inject
	private CDIRequestFactories requestFactories;
	@Inject
	private MethodInfo methodInfo;

	@Produces @javax.enterprise.context.RequestScoped
	public MutableResponse getInstance(){
		MutableResponse response = requestFactories.getResponse();
		if(!methodInfo.getControllerMethod().containsAnnotation(Cached.class)){
			return response;
		}
		return new CharArrayWriterResponse(response);
	}
}
