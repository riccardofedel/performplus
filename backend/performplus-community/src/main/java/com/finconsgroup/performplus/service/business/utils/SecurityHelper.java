package com.finconsgroup.performplus.service.business.utils;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.finconsgroup.performplus.enumeration.Ruolo;
import com.finconsgroup.performplus.enumeration.TipoNodo;
import com.finconsgroup.performplus.manager.security.BaseSecurityHelper;
import com.finconsgroup.performplus.rest.api.vm.OrganizzazioneSmartVM;
import com.finconsgroup.performplus.rest.api.vm.ProfiloVM;
import com.finconsgroup.performplus.rest.api.vm.UtenteVM;
import com.finconsgroup.performplus.rest.api.vm.v2.ObiettivoVM;
import com.finconsgroup.performplus.rest.api.vm.v2.authentication.Abilitazione;
import com.finconsgroup.performplus.service.business.IPianoBusiness;
import com.finconsgroup.performplus.service.business.IRisorsaUmanaBusiness;
import com.finconsgroup.performplus.service.business.IUtenteBusiness;
import com.finconsgroup.performplus.service.business.utils.DateHelper;

public class SecurityHelper extends BaseSecurityHelper {

	private static final Logger logger = LoggerFactory.getLogger(SecurityHelper.class);

	private SecurityHelper() {
	}

	public static OrganizzazioneSmartVM struttura(final UtenteVM utente, final Integer anno) {
		if (utente.getProfili() != null && !utente.getProfili().isEmpty()) {

			for (ProfiloVM p : utente.getProfili()) {
				if (anno.equals(p.getAnno()) && !Boolean.TRUE.equals(p.getAggiunto())) {
					return p.getOrganizzazione();
				}
			}
		}
		return null;
	}

	public static OrganizzazioneSmartVM strutturaAggiunta(final UtenteVM utente, final Integer anno) {
		if (utente.getProfili() != null && !utente.getProfili().isEmpty()) {

			for (ProfiloVM p : utente.getProfili()) {
				if (anno.equals(p.getAnno()) && Boolean.TRUE.equals(p.getAggiunto())) {
					return p.getOrganizzazione();
				}
			}
		}
		return null;
	}

	public static Integer anno(final Long idEnte, final Integer year, final IPianoBusiness pianoBusiness) {
		Integer anno = year;
		if (anno == null) {
			anno = DateHelper.getAnno();
			final List<Integer> anni = pianoBusiness.getAnni(idEnte);

			if (anni != null && !anni.isEmpty() && !anni.contains(anno)) {
				anno = anni.get(anni.size() - 1);
			}
		}
		return anno;
	}

	public static RuoloEStrutture ruoloEStrutture(final IUtenteBusiness utenteBusiness,
			final IRisorsaUmanaBusiness risorsaUmanaBusiness, final Integer anno) {
		final RuoloEStrutture out = new RuoloEStrutture();
		UtenteVM utente = SecurityHelper.utente(utenteBusiness);
		Long idRisorsa = SecurityHelper.risorsa(utente, anno);
		Ruolo ruolo = SecurityHelper.ruolo(utente, anno);
		Ruolo ruoloAggiunto = SecurityHelper.ruoloAggiunto(utente, anno);
		out.setStrutturaProf(SecurityHelper.struttura(utente, anno));
		out.setStrutturaProfAggiunta(SecurityHelper.strutturaAggiunta(utente, anno));
		out.setRuolo(ruolo);
		out.setRuoloAggiunto(ruoloAggiunto);	
		List<OrganizzazioneSmartVM> resp = null;
		List<OrganizzazioneSmartVM> dip = null;
		if (idRisorsa != null) {
			resp = risorsaUmanaBusiness.getOrganizzazioniResponsabile(idRisorsa);
			dip = risorsaUmanaBusiness.getOrganizzazioni(idRisorsa);
		}
		out.setStruttureResp(resp);
		out.setStruttureDip(dip);
		out.setIdRisorsa(idRisorsa);
		return out;
	}

	public static Abilitazione enabling(final String name, final ObiettivoVM npd, final RuoloEStrutture ruoloEStrutture,
			final List<Abilitazione> enablings) {
		final String codiceCompleto = npd.getOrganizzazione() == null ? null
				: npd.getOrganizzazione().getCodiceCompleto();
		if (StringUtils.isBlank(name) || ruoloEStrutture == null || ruoloEStrutture.getRuolo() == null) {
			return new Abilitazione(name);
		}
		final Ruolo ruolo = ruoloEStrutture.getRuolo();
		final Abilitazione out = enabling(name, enablings);

//		if (TipoNodo.AZIONE.equals(npd.getTipoNodo())) {
//			if (Ruolo.POSIZIONE_ORGANIZZATIVA.equals(ruolo)) {
//				out.read(LETTURA.contains(ruolo)).write(false);
//				return out;
//			}
//		}
		if (!out.isRead())
			return out;
		if (StringUtils.isBlank(codiceCompleto) || ADMINS.contains(ruolo))
			return out;

		if (ruoloEStrutture.getStrutturaProf() != null) {
			if (codiceCompleto.contains(ruoloEStrutture.getStrutturaProf().getCodiceCompleto()))
				return out;
		}
		if (ruoloEStrutture.getStrutturaProfAggiunta() != null) {
			if (codiceCompleto.contains(ruoloEStrutture.getStrutturaProfAggiunta().getCodiceCompleto()))
				return out;
		}
		if (ruoloEStrutture.getStruttureResp() == null || ruoloEStrutture.getStruttureResp().isEmpty()) {
			if (!LETTURA_SENZA_RISORSE.contains(ruolo))
				return new Abilitazione(name).read(LETTURA.contains(ruolo)).write(false);
		} else {
			for (OrganizzazioneSmartVM org : ruoloEStrutture.getStruttureResp()) {
				if (codiceCompleto.equals(org.getCodiceCompleto())) {
					return out;
				}
			}

		}
		return new Abilitazione(name).read(true).write(false);
	}

	public static Abilitazione enabling(final String name, final String codiceCompleto,
			final RuoloEStrutture ruoloEStrutture, final List<Abilitazione> enablings) {
		if (StringUtils.isBlank(name) || ruoloEStrutture == null || ruoloEStrutture.getRuolo() == null) {
			return new Abilitazione(name);
		}
		final Ruolo ruolo = ruoloEStrutture.getRuolo();
		final Abilitazione out = enabling(name, enablings);

		if (!out.isRead())
			return out;
		if (StringUtils.isBlank(codiceCompleto) || ADMINS.contains(ruolo))
			return out;
		if (ruoloEStrutture.getStrutturaProf() != null) {
			if (codiceCompleto.contains(ruoloEStrutture.getStrutturaProf().getCodiceCompleto()))
				return out;
		}
		if (ruoloEStrutture.getStrutturaProfAggiunta() != null) {
			if (codiceCompleto.contains(ruoloEStrutture.getStrutturaProfAggiunta().getCodiceCompleto()))
				return out;
		}

		if (ruoloEStrutture.getStruttureResp() == null || ruoloEStrutture.getStruttureResp().isEmpty()) {
			if (!LETTURA_SENZA_RISORSE.contains(ruolo))
				return new Abilitazione(name).read(LETTURA.contains(ruolo)).write(false);
		} else {
			for (OrganizzazioneSmartVM org : ruoloEStrutture.getStruttureResp()) {
				if (codiceCompleto.equals(org.getCodiceCompleto())) {
					return out;
				}
			}

		}
		return new Abilitazione(name).read(true).write(false);
	}

	public static boolean validatore(Ruolo ruolo) {
		if (ruolo == null)
			return false;
		return VALIDATORI.contains(ruolo);
	}

}
