package br.com.caelum.vraptor.actioncache;

import javax.annotation.Priority;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Alternative;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.interceptor.Interceptor;

import com.google.common.cache.Cache;

import br.com.caelum.vraptor.core.MethodInfo;
import br.com.caelum.vraptor.http.MutableResponse;
import br.com.caelum.vraptor.ioc.cdi.CDIRequestInfoFactory;

@RequestScoped
@Alternative
@Priority(Interceptor.Priority.LIBRARY_AFTER)
public class CachedHttpResponseFactory{

	@Inject
	private CDIRequestInfoFactory cdiRequestInfoFactory;
	@Inject
	private MethodInfo methodInfo;

	@Produces @javax.enterprise.context.RequestScoped
	public MutableResponse getInstance(){
		MutableResponse response = cdiRequestInfoFactory.producesRequestInfo().getResponse();
		if(!methodInfo.getControllerMethod().containsAnnotation(Cached.class)){
			return response;
		}
		return new CharArrayWriterResponse(response);
	}
}
