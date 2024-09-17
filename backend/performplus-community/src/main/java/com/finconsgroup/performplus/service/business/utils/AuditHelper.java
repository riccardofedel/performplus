package com.finconsgroup.performplus.service.business.utils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class AuditHelper implements IAuditHelper {

	public boolean isAudit(Class<?> cls) {
		String name = getAudit(cls);
		return name != null;
	}

	public boolean isAudit(Method m) {
		String name = getAudit(m);
		return name != null;
	}

	public String getAudit(Method m) {
		if (m == null)
			return null;
		Audit annotation = m.getAnnotation(Audit.class);
		if (annotation == null)
			return null;

		return annotation.value() == null ? m.getName() : annotation.value();
	}

	public String getAudit(Class<?> cls) {
		if (cls == null)
			return null;
		Audit annotation = cls.getAnnotation(Audit.class);
		if (annotation == null)
			return null;

		return annotation.value() == null ? cls.getSimpleName() : annotation.value();
	}

	public List<Method> getAuditReadMethods(Class<?> cls) {
		if (!isAudit(cls))
			return null;
		List<Method> gets = new ArrayList<>();
		Method[] methods = cls.getMethods();
		for (Method m : methods) {
			if (m.getReturnType() == null)
				continue;
			if (m.getParameterTypes() != null && m.getParameterTypes().length > 0)
				continue;
			if (!isAudit(m))
				continue;
			gets.add(m);
		}
		return gets;
	}

}