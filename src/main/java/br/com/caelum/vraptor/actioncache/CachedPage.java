package br.com.caelum.vraptor.actioncache;

import java.io.IOException;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import br.com.caelum.vraptor.core.MethodInfo;
import br.com.caelum.vraptor.http.MutableRequest;
import br.com.caelum.vraptor.http.MutableResponse;
import br.com.caelum.vraptor.proxy.Proxifier;
import br.com.caelum.vraptor.view.DefaultPageResult;
import br.com.caelum.vraptor.view.PageResult;
import br.com.caelum.vraptor.view.PathResolver;

@RequestScoped
public class CachedPage implements PageResult{
	
	
	private DefaultPageResult pageResult;
	private CharArrayWriterResponse responseBody;		
	private MethodInfo methodInfo;
	private ActionCache actionCache;
	private MutableResponse response;
	

	@Deprecated
	public CachedPage() {
	}
	
	@Inject
	public CachedPage(MutableRequest req, MutableResponse res, MethodInfo methodInfo,
			PathResolver resolver, Proxifier proxifier,ActionCache actionCache) {
		this.response = res;
		this.methodInfo = methodInfo;
		this.actionCache = actionCache;
		responseBody = new CharArrayWriterResponse(res);
		this.pageResult = new DefaultPageResult(req,responseBody, methodInfo, resolver, proxifier);
	}	
	

	public void defaultView() {
		Cached cached = methodInfo.getControllerMethod().getMethod().getAnnotation(Cached.class);
		String body = actionCache.fetch(cached.key());
		if(body==null){
			pageResult.defaultView();
			body  = responseBody.getOutput();
			actionCache.write(cached.key(),body);	
		}
		try {
			response.getWriter().print(body);
		} catch (IOException e) {
			//TODO descobrir o que fazer aqui
			e.printStackTrace();
		}
		
	}

	@Override
	public void forwardTo(String arg0) {
		
	}

	@Override
	public void include() {
		
	}

	@Override
	public <T> T of(Class<T> klass) {
		return pageResult.of(klass);
	}

	@Override
	public void redirectTo(String url) {
		this.pageResult.redirectTo(url);
	}

}
