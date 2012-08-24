package org.dummycreator.dummyfactories;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import org.apache.log4j.Logger;
import org.dummycreator.ClassBindings;
import org.dummycreator.DummyFactory;


/**
 * @author Benny Bottema <b.bottema@projectnibble.org> (further developed project)
 */
public class MethodBasedFactory<T> implements DummyFactory<T> {

    private static final Logger logger = Logger.getLogger(MethodBasedFactory.class);

    private final Method method;

    public MethodBasedFactory(Method method) {
	this.method = method;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T createDummy(List<Exception> constructorExceptions, ClassBindings classBindings) {
	Method m = method;
	Class<?>[] parameters = m.getParameterTypes();
	final Object[] params = new Object[parameters.length];
	for (int i = 0; i < params.length; i++) {
	    params[i] = new ClassBasedFactory<Object>((Class<Object>) parameters[i]).createDummy(constructorExceptions, classBindings);
	}
	try {
	    return (T) m.invoke(null, params);
	} catch (InvocationTargetException e) {
	    logger.debug(String.format("failed to invoke Method [%s] to product an object of type [%s]", m.getName(), method.getReturnType()), e);
	} catch (IllegalAccessException e) {
	    logger.debug(String.format("failed to invoke Method [%s] to product an object of type [%s]", m.getName(), method.getReturnType()), e);
	}
	return null;
    }

}