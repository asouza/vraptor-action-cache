package br.com.caelum.vraptor.actioncache.events;

import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Specializes;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import br.com.caelum.vraptor.actioncache.ActionCache;
import br.com.caelum.vraptor.actioncache.Cached;
import br.com.caelum.vraptor.actioncache.CachedActionBinding;
import br.com.caelum.vraptor.actioncache.WriteResponseBinding;
import br.com.caelum.vraptor.core.MethodInfo;
import br.com.caelum.vraptor.events.EndOfInterceptorStack;
import br.com.caelum.vraptor.events.MethodExecuted;
import br.com.caelum.vraptor.events.ReadyToExecuteMethod;
import br.com.caelum.vraptor.observer.ExecuteMethod;
import br.com.caelum.vraptor.reflection.MethodExecutor;
import br.com.caelum.vraptor.validator.Validator;

@Specializes
public class CachedExecuteMethod extends ExecuteMethod {

	private ActionCache actionCache;
	private Event<MethodExecuted> methodExecutedEvent;
	private MethodInfo methodInfo;
	
	
	/**
	 * @deprecated CDI eyes only
	 */
	protected CachedExecuteMethod() {
		super(null, null, null, null, null);
	}	

	@Inject
	public CachedExecuteMethod(MethodInfo methodInfo, Validator validator, MethodExecutor methodExecutor,
			Event<MethodExecuted> methodExecutedEvent, Event<ReadyToExecuteMethod> readyToExecuteMethod,
			ActionCache actionCache,HttpServletResponse response) {
		super(methodInfo, validator, methodExecutor, methodExecutedEvent, readyToExecuteMethod);
		this.methodInfo = methodInfo;
		this.methodExecutedEvent = methodExecutedEvent;
		this.actionCache = actionCache;
	}

	@Override
	public void execute(@Observes EndOfInterceptorStack stack) {
		Cached cached = stack.getControllerMethod().getMethod().getAnnotation(Cached.class);
		if(cached==null){
			super.execute(stack);
			return ;
		}
		String body = actionCache.fetch(cached.key());
		if (body == null) {
			super.execute(stack);
		}
		methodExecutedEvent.select(new CachedActionBinding()).fire(new MethodExecuted(stack.getControllerMethod(), methodInfo));
		methodExecutedEvent.select(new WriteResponseBinding()).fire(new MethodExecuted(stack.getControllerMethod(), methodInfo));
		
	}
}
