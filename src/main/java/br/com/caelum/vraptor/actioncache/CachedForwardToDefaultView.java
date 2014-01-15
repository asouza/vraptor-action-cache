package br.com.caelum.vraptor.actioncache;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.Specializes;
import javax.inject.Inject;

import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.events.MethodExecuted;
import br.com.caelum.vraptor.observer.ForwardToDefaultView;

@Specializes
public class CachedForwardToDefaultView extends ForwardToDefaultView {

	private Result result;

	@SuppressWarnings("deprecation")
	public CachedForwardToDefaultView() {
	}

	@Inject
	public CachedForwardToDefaultView(Result result) {
		super(result);
		this.result = result;
	}

	@Override
	public void forward(@Observes MethodExecuted event) {		
		if (!event.getControllerMethod().containsAnnotation(Cached.class)) {
			super.forward(event);
			return;
		}
		result.use(CachedPage.class).defaultView();

	}
}
