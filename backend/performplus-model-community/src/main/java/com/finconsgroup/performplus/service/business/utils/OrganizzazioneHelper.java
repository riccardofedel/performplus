package com.finconsgroup.performplus.service.business.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.finconsgroup.performplus.domain.Organizzazione;
import com.finconsgroup.performplus.enumeration.Livello;
import com.finconsgroup.performplus.rest.api.vm.OrganizzazioneSmartVM;
import com.finconsgroup.performplus.service.dto.OrganizzazioneDTO;
import com.finconsgroup.performplus.service.dto.OrganizzazioneEasyDTO;

@SuppressWarnings("serial")
public class OrganizzazioneHelper implements Serializable {

	private OrganizzazioneHelper() {
	}

	public static boolean contiene(OrganizzazioneDTO uo, OrganizzazioneDTO elem) {
		if (uo == null || uo.getId() == null || elem == null || elem.getId() == null)
			return false;
		if (uo.getId().equals(elem.getId()))
			return true;

		List<OrganizzazioneDTO> figli = uo.getFigli();
		if (figli != null) {
			for (OrganizzazioneDTO o : figli) {
				if (contiene(o, elem))
					return true;
			}

		}
		return false;
	}
	public static boolean contiene(OrganizzazioneDTO uo, OrganizzazioneSmartVM elem) {
		if (uo == null || uo.getId() == null || elem == null || elem.getId() == null)
			return false;
		if (uo.getId().equals(elem.getId()))
			return true;

		List<OrganizzazioneDTO> figli = uo.getFigli();
		if (figli != null) {
			for (OrganizzazioneDTO o : figli) {
				if (contiene(o, elem))
					return true;
			}

		}
		return false;
	}
	public static boolean contiene(OrganizzazioneSmartVM uo, OrganizzazioneDTO elem) {
		if (uo == null || uo.getId() == null || elem == null || elem.getId() == null)
			return false;
		if (uo.getId().equals(elem.getId()))
			return true;

		return elem.getCodiceCompleto().startsWith(uo.getCodiceCompleto()+".");
	}
	public static List<OrganizzazioneDTO> contiene(OrganizzazioneDTO uo) {
		List<OrganizzazioneDTO> list = new ArrayList<>();
		if (uo == null)
			return list;
		list.add(uo);

		if (uo.getFigli() == null)
			return list;
		for (OrganizzazioneDTO o : uo.getFigli()) {
			list.addAll(contiene(o));
		}
		return list;
	}

	public static boolean contenutoEasy(OrganizzazioneEasyDTO uo, List<OrganizzazioneEasyDTO> organizzazioni) {
		if (organizzazioni == null)
			return false;
		for (OrganizzazioneEasyDTO o : organizzazioni) {
			if (uo.getId().equals(o.getId()))
				return true;
		}
		return false;
	}

	public static boolean contenuto(OrganizzazioneEasyDTO uo, List<OrganizzazioneDTO> organizzazioni) {
		if (organizzazioni == null)
			return false;
		for (OrganizzazioneEasyDTO o : organizzazioni) {
			if (uo.getId().equals(o.getId()))
				return true;
		}
		return false;
	}

	public static boolean contenutoLoopEasy(OrganizzazioneEasyDTO uo, List<OrganizzazioneEasyDTO> organizzazioni) {
		if (organizzazioni == null)
			return false;
		for (OrganizzazioneEasyDTO o : organizzazioni) {
			if (uo.getId().equals(o.getId()))
				return true;
			List<OrganizzazioneEasyDTO> list = padri(o);
			for (OrganizzazioneEasyDTO p : list) {
				if (contenutoEasy(p, organizzazioni))
					return true;
			}
		}
		return false;
	}

	public static List<OrganizzazioneEasyDTO> padri(OrganizzazioneEasyDTO o) {
		List<OrganizzazioneEasyDTO> list = new ArrayList<>();
		OrganizzazioneEasyDTO p = o.getPadre();
		while (p != null) {
			list.add(p);
			p = p.getPadre();
		}
		return list;
	}

	public static OrganizzazioneEasyDTO padre(OrganizzazioneEasyDTO o, Livello liv) {
		OrganizzazioneEasyDTO p = o.getPadre();
		while (p != null) {
			if (p.getLivello() == liv)
				return p;
			p = p.getPadre();
		}
		return o;
	}

	public static boolean contenuto(Long id, List<Long> sel) {
		if (sel != null && !sel.isEmpty()) {
			for (Long l : sel) {
				if (l.equals(id))
					return true;
			}
		}
		return false;

	}

	public static List<OrganizzazioneDTO> figli(OrganizzazioneDTO organizzazione, Livello livello) {
		List<OrganizzazioneDTO> items = contiene(organizzazione);
		if (items == null)
			return null;
		List<OrganizzazioneDTO> list = new ArrayList<>();
		for (OrganizzazioneDTO o : items) {
			if (o.getLivello() == livello)
				list.add(o);
		}
		return list;
	}


	public static boolean contenutoLoop(OrganizzazioneDTO uo, List<OrganizzazioneDTO> organizzazioni) {
		if (organizzazioni == null)
			return false;
		for (OrganizzazioneEasyDTO o : organizzazioni) {
			if (uo.getId().equals(o.getId()))
				return true;
			List<OrganizzazioneEasyDTO> list = padri(o);
			for (OrganizzazioneEasyDTO p : list) {
				if (contenuto(p, organizzazioni))
					return true;
			}
		}
		return false;
	}

	public static String ridotto(final String codiceCompleto) {
		if (StringUtils.isBlank(codiceCompleto)) {
			return codiceCompleto;
		}
		int k = codiceCompleto.indexOf('.');
		if (k < 0) {
			return "";
		}
		return codiceCompleto.substring(k + 1);
	}

	public static String getNomeCompleto(Organizzazione o) {
		return OrganizzazioneHelper.ridotto(o.getCodiceCompleto()) + " " + o.getIntestazione();
	}

	public static String getNomeCompleto(OrganizzazioneSmartVM o) {
		return OrganizzazioneHelper.ridotto(o.getCodiceCompleto()) + " " + o.getIntestazione();
	}

	public static String settore(OrganizzazioneSmartVM o) {
		switch (o.getLivello()) {
		case ENTE:
			return "";
		case SUPERIORE:
			return OrganizzazioneHelper.ridotto(o.getCodiceCompleto());
		case MEDIO:
			return OrganizzazioneHelper.ridotto(precedente(o.getCodiceCompleto()));
		case INFERIORE:
			return OrganizzazioneHelper.ridotto(precedente(precedente(o.getCodiceCompleto())));
		}
		return "";
	}

	private static String precedente(String c) {
		if (StringUtils.isBlank(c))
			return "";
		int k = c.lastIndexOf('.');
		if (k <= 0)
			return null;
		return c.substring(0, k);
	}

	public static String settore(OrganizzazioneDTO o) {
		switch (o.getLivello()) {
		case ENTE:
			return "";
		case SUPERIORE:
			return OrganizzazioneHelper.ridotto(o.getCodiceCompleto());
		case MEDIO:
			return OrganizzazioneHelper.ridotto(precedente(o.getCodiceCompleto()));
		case INFERIORE:
			return OrganizzazioneHelper.ridotto(precedente(precedente(o.getCodiceCompleto())));
		}
		return "";
	}

	public static String servizio(OrganizzazioneSmartVM o) {
		switch (o.getLivello()) {
		case ENTE:
			return "";
		case SUPERIORE:
			return "";
		case MEDIO:
			return OrganizzazioneHelper.ridotto(o.getCodiceCompleto());
		case INFERIORE:
			return OrganizzazioneHelper.ridotto(precedente(o.getCodiceCompleto()));
		}
		return "";
	}

	public static String ufficio(OrganizzazioneSmartVM o) {
		switch (o.getLivello()) {
		case ENTE:
			return "";
		case SUPERIORE:
			return "";
		case MEDIO:
			return "";
		case INFERIORE:
			return OrganizzazioneHelper.ridotto(o.getCodiceCompleto());
		}
		return "";
	}
	public static String servizio(OrganizzazioneDTO o) {
		switch (o.getLivello()) {
		case ENTE:
			return "";
		case SUPERIORE:
			return "";
		case MEDIO:
			return OrganizzazioneHelper.ridotto(o.getCodiceCompleto());
		case INFERIORE:
			return OrganizzazioneHelper.ridotto(precedente(o.getCodiceCompleto()));
		}
		return "";
	}

	public static String ufficio(OrganizzazioneDTO o) {
		switch (o.getLivello()) {
		case ENTE:
			return "";
		case SUPERIORE:
			return "";
		case MEDIO:
			return "";
		case INFERIORE:
			return OrganizzazioneHelper.ridotto(o.getCodiceCompleto());
		}
		return "";
	}
}
