package com.finconsgroup.performplus.service.business.utils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.StringJoiner;

import org.apache.commons.lang3.StringUtils;

import com.finconsgroup.performplus.domain.NodoPiano;
import com.finconsgroup.performplus.domain.Organizzazione;
import com.finconsgroup.performplus.domain.RisorsaUmana;
import com.finconsgroup.performplus.enumeration.Disponibile;
import com.finconsgroup.performplus.enumeration.TipoNodo;
import com.finconsgroup.performplus.service.dto.NodoPianoDTO;
import com.finconsgroup.performplus.service.dto.NodoPianoEasyDTO;
import com.finconsgroup.performplus.service.dto.OrganizzazioneDTO;
import com.finconsgroup.performplus.service.dto.RisorsaUmanaDTO;
import com.finconsgroup.performplus.service.dto.StakeholderDTO;

public class NodoPianoHelper implements Serializable {

	private static final long serialVersionUID = 1940493143306952768L;

	private static final List<String> ORDINE_AREE = List.of("IST", "ECON", "SOC", "TER", "BIL");

	public static boolean contiene(NodoPianoDTO programmazione, NodoPianoDTO elem) {
		if (programmazione == null || programmazione.getId() == null || elem == null || elem.getId() == null)
			return false;
		if (programmazione.getId().equals(elem.getId()))
			return true;

		List<NodoPianoDTO> figli = programmazione.getFigli();
		if (figli != null) {
			for (NodoPianoDTO o : figli) {
				if (contiene(o, elem))
					return true;
			}

		}
		return false;
	}

	public static List<NodoPianoDTO> contiene(NodoPianoDTO programmazione) {
		List<NodoPianoDTO> list = new ArrayList<>();
		if (programmazione == null)
			return list;
		list.add(programmazione);

		if (programmazione.getFigli() == null)
			return list;
		for (NodoPianoDTO o : programmazione.getFigli()) {
			list.addAll(contiene(o));
		}
		return list;
	}

	public static void loopPrioritaStrategica(NodoPianoDTO programmazione) {
		List<NodoPianoDTO> items = contiene(programmazione);
		for (NodoPianoDTO np : items) {
			if (np.getPeso() != null)
				continue;
			BigDecimal p = loopPrioritaStrategicaFigli(np);
			if (p == null)
				continue;
			if (TipoNodo.PIANO.equals(np.getTipoNodo())) {
				np.setPrioritaStrategica(null);
			} else {
				np.setPrioritaStrategica(p);
			}
		}
	}

	public static BigDecimal loopPrioritaStrategicaFigli(NodoPianoDTO programmazione) {
		BigDecimal tot = BigDecimal.ZERO;
		List<NodoPianoDTO> items = programmazione.getFigli();
		if (items != null) {
			for (NodoPianoDTO np : items) {
				np.setPrioritaStrategica(np.getPeso());
				if (np.getPeso() != null)
					tot = tot.add(np.getPeso());
			}
		}
		programmazione.setPrioritaStrategica(tot);
		return tot;
	}

	public static void loopPrioritaStrategicaStampe(NodoPianoDTO programmazione) {
		List<NodoPianoDTO> items = contiene(programmazione);
		for (NodoPianoDTO np : items) {
			BigDecimal p = loopPrioritaStrategicaFigli(np);
			np.setPrioritaStrategica(p);
		}
	}

	public static BigDecimal loopPrioritaStrategicaFigliStampe(NodoPianoDTO programmazione) {
		BigDecimal tot = BigDecimal.ZERO;
		List<NodoPianoDTO> items = programmazione.getFigli();
		if (items != null) {
			if (programmazione.getTipoNodo() == TipoNodo.AZIONE) {
				for (NodoPianoDTO np : items) {
					if (np.getPeso() != null)
						tot = tot.add(np.getPeso());
				}
			} else {
				for (NodoPianoDTO np : items) {
					BigDecimal p = loopPrioritaStrategicaFigli(np);
					np.setPrioritaStrategica(p);
				}
			}
		}
		return tot;
	}

	public static NodoPianoEasyDTO trovaParent(NodoPianoDTO nodo, TipoNodo tipo) {
		NodoPianoEasyDTO padre = nodo.getPadre();
		while (padre != null && padre.getTipoNodo() != tipo)
			padre = padre.getPadre();
		return padre;
	}

	public static boolean contenuto(NodoPianoDTO nodo, List<Long> filtroNodi) {
		if (filtroNodi == null)
			return true;
		for (Long id : filtroNodi) {
			if (id.equals(nodo.getId()))
				return true;
		}
		return false;
	}

	public static boolean contenuto(NodoPiano nodo, List<Long> filtroNodi) {
		if (filtroNodi == null)
			return true;
		for (Long id : filtroNodi) {
			if (id.equals(nodo.getId()))
				return true;
		}
		return false;
	}

	public static List<StakeholderDTO> listaStakeholders(NodoPianoDTO nodo) {

		List<StakeholderDTO> output = new ArrayList<>();
		List<NodoPianoDTO> items = NodoPianoHelper.contiene(nodo);
		List<String> doppi = new ArrayList<>();
		for (NodoPianoDTO np : items) {
			if (np.getStakeholders() == null || np.getStakeholders().isEmpty())
				continue;
			for (StakeholderDTO st : np.getStakeholders()) {
				if (doppi.contains(st.getId().toString()))
					continue;
				doppi.add(st.getId().toString());
				output.add(st);
			}
		}
		return output;
	}

	public static String responsabili(NodoPianoDTO nodo) {
		List<OrganizzazioneDTO> orgs = new ArrayList<>();
		if (nodo.getOrganizzazione() != null)
			orgs.add(nodo.getOrganizzazione());
		List<String> doppi = new ArrayList<>();
		List<String> lista = new ArrayList<>();
		if (orgs == null)
			return " ";
		for (OrganizzazioneDTO o : orgs) {
			String resp = o.getResponsabile() == null ? null : o.getResponsabile().getCognomeNome();
			if (resp == null)
				continue;
			if (doppi.contains(resp))
				continue;
			doppi.add(resp);
			lista.add(resp);
		}
		return listaSeparata(lista);
	}

	public static String responsabili(NodoPiano nodo) {
		List<Organizzazione> orgs = new ArrayList<>();
		if (nodo.getOrganizzazione() != null)
			orgs.add(nodo.getOrganizzazione());
		List<String> doppi = new ArrayList<>();
		List<String> lista = new ArrayList<>();
		if (orgs.isEmpty())
			return " ";
		for (Organizzazione o : orgs) {
			String resp = getCognomeNome(o.getResponsabile());
			if (StringUtils.isBlank(resp) || doppi.contains(resp))
				continue;
			doppi.add(resp);
			lista.add(resp);
		}
		return listaSeparata(lista);
	}


	public static String getCognomeNome(RisorsaUmana r) {
		if (r == null)
			return "";
		return (r.getCognome() == null ? "" : r.getCognome()) + " "
		+ (r.getNome() == null ? "" : r.getNome())
		+ (r.getCodiceInterno() == null ? "" : " ["+r.getCodiceInterno()+"]");

	}

	public static String stakeholders(NodoPianoDTO nodo) {
		List<StakeholderDTO> list = listaStakeholders(nodo);
		List<String> doppi = new ArrayList<>();
		List<String> lista = new ArrayList<>();
		if (list == null)
			return "";
		for (StakeholderDTO o : list) {
			String resp = o.getDescrizione();
			if (StringUtils.isBlank(resp) || doppi.contains(resp))
				continue;
			doppi.add(resp);
			lista.add(resp);
		}
		return listaSeparata(lista);
	}

	public static String stakeholdersNodo(NodoPianoDTO nodo) {
		List<StakeholderDTO> list = nodo.getStakeholders();
		List<String> doppi = new ArrayList<>();
		List<String> lista = new ArrayList<>();
		if (list == null || list.isEmpty()) {
			List<String> collegati = nodo.getStakeholdersCollegati();
			if (collegati != null && !collegati.isEmpty()) {
				for (String o : collegati) {
					String desc = o;
					if (desc == null)
						continue;
					if (doppi.contains(desc))
						continue;
					doppi.add(desc);
					lista.add(desc);
				}
			}
		} else {
			for (StakeholderDTO o : list) {
				String desc = o.getDescrizione();
				if (desc == null)
					continue;
				if (doppi.contains(desc))
					continue;
				doppi.add(desc);
				lista.add(desc);
			}
		}
		return listaSeparata(lista);
	}

	public static boolean contenuto(OrganizzazioneDTO p, List<OrganizzazioneDTO> princ) {
		if (princ == null)
			return false;
		for (OrganizzazioneDTO o : princ) {
			if (o.getId().equals(p.getId()))
				return true;

		}
		return false;
	}

	public static String organizzazioniPrincipali(NodoPianoDTO nodo) {
		List<OrganizzazioneDTO> orgs = new ArrayList<>();
		if (nodo.getOrganizzazione() != null)
			orgs.add(nodo.getOrganizzazione());

		List<String> doppi = new ArrayList<>();
		List<String> lista = new ArrayList<>();
		if (orgs == null)
			return " ";
		for (OrganizzazioneDTO o : orgs) {
			String desc = o.getNomeCompletoDaLivello();
			if (doppi.contains(desc))
				continue;
			doppi.add(desc);
			lista.add(desc);
		}
		return listaSeparata(lista);
	}

	public static String organizzazioniPrincipaliStampe(NodoPianoDTO nodo) {
		List<OrganizzazioneDTO> orgs = new ArrayList<>();
		if (nodo.getOrganizzazione() != null)
			orgs.add(nodo.getOrganizzazione());

		List<String> doppi = new ArrayList<>();
		List<String> lista = new ArrayList<>();
		if (orgs == null)
			return " ";
		for (OrganizzazioneDTO o : orgs) {
			String desc = o.getCodiceRidotto() + " " + o.getIntestazione();
			if (doppi.contains(desc))
				continue;
			doppi.add(desc);
			lista.add(desc);
		}
		return listaSeparata(lista);
	}

	public static String assessori(NodoPianoDTO nodo) {
		return amministratori(nodo);
	}

	public static String organizzazioniCoinvolte(NodoPianoDTO nodo) {
		List<OrganizzazioneDTO> orgs = nodo.getOrganizzazioni();
		List<String> doppi = new ArrayList<>();
		List<String> lista = new ArrayList<>();
		if (orgs == null)
			return " ";
		for (OrganizzazioneDTO o : orgs) {
			String desc = o.getNomeCompletoDaLivello();
			if (doppi.contains(desc))
				continue;
			doppi.add(desc);
			lista.add(desc);
		}
		return listaSeparata(lista);
	}

	public static String organizzazioniCoinvolteStampe(NodoPianoDTO nodo) {
		List<OrganizzazioneDTO> orgs = nodo.getOrganizzazioni();
		List<String> doppi = new ArrayList<>();
		List<String> lista = new ArrayList<>();
		if (orgs == null)
			return " ";
		for (OrganizzazioneDTO o : orgs) {
			String desc = o.getCodiceRidotto() + " " + o.getIntestazione();
			if (doppi.contains(desc))
				continue;
			doppi.add(desc);
			lista.add(desc);
		}
		return listaSeparata(lista);
	}

	private static String listaSeparata(List<String> lista) {
		if (lista == null || lista.isEmpty())
			return "";
		if (lista.size() == 1) {
			return lista.get(0);
		}
		lista.sort(Comparator.naturalOrder());
		final StringJoiner joiner = new StringJoiner("<br/>");
		lista.forEach(a -> joiner.add(a));
		return joiner.toString();
	}



	public static List<RisorsaUmanaDTO> assessoriDto(NodoPianoDTO nodoPiano) {
		return nodoPiano.getAmministratori();
	}

	public static String amministratori(NodoPianoDTO nodoPiano) {
		List<RisorsaUmanaDTO> items = nodoPiano.getAmministratori();
		if (items == null)
			return "";
		final StringJoiner joiner = new StringJoiner("<br/>");
		items.forEach(a -> joiner.add(a.getNomeAssessore()));
		return joiner.toString();
	}

	public static String ridotto(final String codiceCompleto) {
		if (StringUtils.isBlank(codiceCompleto)) {
			return "";
		}
		int k = codiceCompleto.indexOf('.');
		if (k < 0) {
			return "";
		}
		return senzaZeri(codiceCompleto.substring(k + 1));
	}

	public static String senzaZeri(final String code) {
		if (StringUtils.isBlank(code)) {
			return "";
		}
		String s = code;
		if (s.startsWith("piano_")) {
			int k = s.indexOf('.');
			if (k > 0)
				s = s.substring(k + 1);
		}
		String[] a = s.split("\\.");
		for (int i = 0; i < a.length; i++) {
			a[i] = a[i].replaceFirst("^0+(?!$)", "");
		}
		return StringUtils.join(a, '.');
	}

	public static String getNomeTipoNodo(TipoNodo tipoNodo) {
		return ModelHelper.normalize(tipoNodo);
	}

	public static TipoNodo tipoNodoFiglio(TipoNodo tipoNodo, TipoNodo[] tipiNodo) {
		for (int i = 0; i < tipiNodo.length; i++) {
			if (tipoNodo.equals(tipiNodo[i]) && i < (tipiNodo.length - 1))
				return tipiNodo[i + 1];
		}
		return null;
	}

	public static int ordine(String codiceCompleto) {
		int ordine = 0;
		if (StringUtils.isBlank(codiceCompleto))
			return ordine;
		String ca[] = codiceCompleto.split("\\.");
		if (ca != null && ca.length > 1) {
			ordine = ORDINE_AREE.indexOf(ca[1]) + 1;
		}
		return ordine;
	}

	public static String codiceInterno(NodoPiano np) {
		if (np == null)
			return null;
		if (StringUtils.isNotBlank(np.getCodiceCompleto())) {
			return senzaZeri(np.getCodiceCompleto());
		}
		if (np.getTipoNodo() == null || StringUtils.isBlank(np.getCodice()))
			return null;
		if (np.getPadre() == null)
			return senzaZeri(np.getCodice());
		return senzaZeri(np.getPadre().getCodiceCompleto() + "." + np.getCodice());
	}
}