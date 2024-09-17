package com.finconsgroup.performplus.service.business.utils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class ClassHelper {

    public static Type trovaClass(Class<?> clz) throws Exception {
	Type gs = clz.getGenericSuperclass();
	while (gs != null && !(gs instanceof ParameterizedType)
		&& gs instanceof Class) {
	    Type type = ((Class<?>)gs).getGenericSuperclass();
	    if (type==null || gs.getClass() == type.getClass())
		gs = null;
	    else
		gs=type;
	}
	ParameterizedType pt;
	if (gs != null && gs instanceof ParameterizedType) {
	    pt = (ParameterizedType) gs;
	    Type[] ata = pt.getActualTypeArguments();
	    if (ata != null && ata.length > 0)
		try {
		    return ata[0];
		} catch (Throwable t) {
		}

	}
	return null;
    }
   
}