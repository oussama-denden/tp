package com.nordnet.topaze.businessprocess.swap.util.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Utils class used to retrieve the Spring context.
 * 
 * @author mazemzemi
 * 
 */
public class ApplicationContextHolder implements ApplicationContextAware {

	/**
	 * Spring context.
	 */
	private static ApplicationContext context = null;

	/**
	 * Set the context.
	 * 
	 * @param ctx
	 *            : the current context.
	 */
	@Override
	public void setApplicationContext(ApplicationContext ctx) {
		context = ctx;

	}

	/**
	 * Get the current context.
	 * 
	 * @return the context.
	 */
	public static ApplicationContext getContext() {
		return context;
	}

	/**
	 * Return a bean stored in the Spring context.
	 * 
	 * @param <T>
	 *            the type of bean to retrieve.
	 * @param beanName
	 *            the name of the bean to retrieve.
	 * 
	 * @return the found bean, <code>null</code> else.
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getBean(String beanName) {

		return (T) context.getBean(beanName);
	}
}
