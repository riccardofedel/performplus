package com.finconsgroup.performplus.service.business.utils;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.finconsgroup.performplus.service.dto.EntityDTO;

public class VariazioneHelper {
	private static final AuditHelper AH = new AuditHelper();

	public static Map<String, String> attributi(final Object dto) {
		Map<String, Method> gets = gets(dto.getClass());
		Map<String, String> attributi = new HashMap<>();
		for (String key : gets.keySet()) {
			Method read = gets.get(key);
			try {
				Object value = read.invoke(dto, (Object[]) null);
				value = valoreRicorsivo(value, key);
				if (value != null) {
					attributi.put(key, value.toString());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return attributi;
	}

	private static Object valoreRicorsivo(Object value, String key) throws Exception {
		if (key == null || value == null || key.trim().length() == 0 || !key.contains("."))
			return value;
		key = key.trim();
		int k = key.indexOf('.');
		String key1 = key.substring(k + 1);
		k = key.indexOf('.');
		String name=key1;
		if(k>0) {
			name=key1.substring(0, k);
		}
		name="get"+StringUtils.capitalize(name);
		Map<String, Method> gets = gets(value.getClass());
		Method read = gets.get(name);
		return valoreRicorsivo(read.invoke(value, (Object[]) null),key1);
	}

	public static String oggetto(final Object dto) {
		if (dto == null)
			return null;
		return AH.getAudit(dto.getClass());
	}

	public static String identificativo(final Object dto) {
		if (dto instanceof EntityDTO ele) {
			return Long.toString(ele.getId());
		}
		return dto.toString();
	}

	public static Map<String, Method> gets(final Class<?> cls) {
		final HashMap<String, Method> map = new HashMap<>();
		final List<Method> methods = AH.getAuditReadMethods(cls);
		if (methods != null && !methods.isEmpty()) {
			for (Method m : methods) {
				map.put(AH.getAudit(m), m);
			}
		}
		return map;
	}

	public static boolean isAudit(Object dto) {
		if (dto == null)
			return false;
		return AH.isAudit(dto.getClass());
	}

//	public static void main(String[] args) {
//		ObiettivoGestionaleDTO np = new ObiettivoGestionaleDTO(0l, 2021);
//		np.setTipoObiettivoGestionale(TipoObiettivoGestionale.SEQUENZIALE);
//		ObiettivoGestionaleDTO predecessore = new ObiettivoGestionaleDTO(0l, 2020);
//		predecessore.setId(22l);
//		np.setPredecessore(predecessore);
//		np.setInizio(java.sql.Date.valueOf("2012-03-01"));
//		np.setScadenza(java.sql.Date.valueOf("2012-03-23"));
//		np.setDescrizione("prova");
//		Map<String, String> map = attributi(np);
//		for (String key : map.keySet()) {
//			System.out.println(key + ":" + map.get(key));
//		}
//	}
}