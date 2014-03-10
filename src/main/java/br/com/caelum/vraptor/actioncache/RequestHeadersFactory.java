package br.com.caelum.vraptor.actioncache;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import br.com.caelum.vraptor.http.MutableRequest;

@ApplicationScoped
public class RequestHeadersFactory {
	
	@Inject
	private MutableRequest mutableRequest;

	@RequestScoped
	@Produces
	public RequestHeaders getInstance(){
		return new RequestHeaders(mutableRequest);
	}
}
