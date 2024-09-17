package com.finconsgroup.performplus.service.business.utils;
import java.lang.reflect.Method;
import java.util.List;

public interface IAuditHelper {
    public boolean isAudit(Class<?> cls);

    public boolean isAudit(Method m);

    public String getAudit(Method m);

    public String getAudit(Class<?> cls);

    public List<Method> getAuditReadMethods(Class<?> cls);
}