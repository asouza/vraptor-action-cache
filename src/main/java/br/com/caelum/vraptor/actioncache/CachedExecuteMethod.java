package br.com.caelum.vraptor.actioncache;

import java.io.IOException;

import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Specializes;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

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
	private HttpServletResponse response;
	private Event<MethodExecuted> methodExecutedEvent;
	private MethodInfo methodInfo;

	@Inject
	public CachedExecuteMethod(MethodInfo methodInfo, Validator validator, MethodExecutor methodExecutor,
			Event<MethodExecuted> methodExecutedEvent, Event<ReadyToExecuteMethod> readyToExecuteMethod,
			ActionCache actionCache,HttpServletResponse response) {
		super(methodInfo, validator, methodExecutor, methodExecutedEvent, readyToExecuteMethod);
		this.methodInfo = methodInfo;
		this.methodExecutedEvent = methodExecutedEvent;
		this.actionCache = actionCache;
		this.response = response;
	}

	@Override
	public void execute(@Observes EndOfInterceptorStack stack) {
		String body = actionCache.fetch(stack.getControllerMethod().getMethod().getAnnotation(Cached.class).key());
		if (body == null) {
			super.execute(stack);
			return;
		}
		methodExecutedEvent.fire(new MethodExecuted(stack.getControllerMethod(), methodInfo));
	}
}
