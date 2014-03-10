package br.com.caelum.vraptor.actioncache;

import org.jboss.weld.interceptor.util.proxy.TargetInstanceProxy;

public class ProxyTargetInstance {

	@SuppressWarnings("unchecked")
	public static <T> T get(Object object){
		TargetInstanceProxy<T> proxy = (TargetInstanceProxy<T>)object;
		return proxy.getTargetInstance();
	}
}
