package br.com.caelum.vraptor.actioncache;

import javax.enterprise.inject.Vetoed;

import br.com.caelum.vraptor.controller.ControllerMethod;

@Vetoed
public class CachedMethodExecuted {


	private ControllerMethod controllerMethod;

	public CachedMethodExecuted(ControllerMethod controllerMethod) {
		this.controllerMethod = controllerMethod;
	}
	
	public Cached getCached(){
		return this.controllerMethod.getMethod().getAnnotation(Cached.class);
	}
}
