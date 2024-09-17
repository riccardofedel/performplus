package com.finconsgroup.performplus.manager.security;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.finconsgroup.performplus.enumeration.Ruolo;
import com.finconsgroup.performplus.rest.api.vm.ProfiloVM;
import com.finconsgroup.performplus.rest.api.vm.UtenteVM;
import com.finconsgroup.performplus.rest.api.vm.v2.authentication.Abilitazione;
import com.finconsgroup.performplus.rest.api.vm.v2.authentication.WhoIsResponse;
import com.finconsgroup.performplus.service.business.IUtenteBusiness;

public class BaseSecurityHelper {
	public static final String STAMPE = "STAMPE";
	public static final String CONSUNTIVAZIONE = "CONSUNTIVAZIONE";
	public static final String INTRODUZIONE = "INTRODUZIONE";
	public static final String ORGANIGRAMMA = "ORGANIGRAMMA";
	public static final String RISORSE = "RISORSE";
	public static final String AMMINISTATORI = "AMMINISTATORI";
	public static final String DUP = "DUP";
	public static final String PROGRAMMAZIONE = "PROGRAMMAZIONE";
	public static final String STRUTTURA = "STRUTTURA";
	public static final String SISTEMA = "SISTEMA";

	public static final String STRUTTURA_ORGANIGRAMMA = STRUTTURA + "." + ORGANIGRAMMA;
	public static final String DUP_PROGRAMMAZIONE = DUP + "." + PROGRAMMAZIONE;
	public static final String DUP_PROGRAMMAZIONE_RISORSE = DUP + "." + PROGRAMMAZIONE + "." + RISORSE;
	public static final String DUP_CONSUNTIVAZIONE = DUP + "." + CONSUNTIVAZIONE;
	public static final String STRUTTURA_AMMINISTATORI = STRUTTURA + "." + AMMINISTATORI;
	public static final String STRUTTURA_RISORSE = STRUTTURA + "." + RISORSE;
	public static final String DUP_INTRODUZIONE = DUP + "." + INTRODUZIONE;

	public static final String QUESTIONARI = "QUESTIONARI";
	public static final String REGISTRAZIONI = "REGISTRAZIONI";
	public static final String SCHEDA = "SCHEDA";
	public static final String PRIORITA = "PRIORITA";
	public static final String REGOLAMENTI = "REGOLAMENTI";

	
	//public static final Ruolo ADMIN = Ruolo.AMMINISTRATORE;
	public static final List<Ruolo> ADMINS= List.of(Ruolo.AMMINISTRATORE,
			Ruolo.SUPPORTO_SISTEMA);
	public static final List<Ruolo> LETTURA = List.of(Ruolo.AMMINISTRATORE, Ruolo.RISORSA,
			Ruolo.SUPPORTO_SISTEMA, Ruolo.POSIZIONE_ORGANIZZATIVA, Ruolo.REFERENTE, Ruolo.POSIZIONE_ORGANIZZATIVA,
			Ruolo.OIV);
	public static final List<Ruolo> SCRITTURA_AG = List.of(Ruolo.AMMINISTRATORE, Ruolo.OIV);
	public static final List<Ruolo> LETTURA_SENZA_RISORSE = List.of(Ruolo.AMMINISTRATORE, 
			Ruolo.SUPPORTO_SISTEMA, Ruolo.POSIZIONE_ORGANIZZATIVA, Ruolo.REFERENTE, Ruolo.POSIZIONE_ORGANIZZATIVA,
			Ruolo.OIV);
	public static final List<Ruolo> SCRITTURA = List.of(Ruolo.AMMINISTRATORE, Ruolo.SUPPORTO_SISTEMA,
			Ruolo.POSIZIONE_ORGANIZZATIVA, Ruolo.REFERENTE);
	public static final List<Ruolo> SCRITTURA_PIU = List.of(Ruolo.AMMINISTRATORE, Ruolo.SUPPORTO_SISTEMA,
			Ruolo.POSIZIONE_ORGANIZZATIVA, Ruolo.REFERENTE);
	
	public static final List<Ruolo> PROGRAMMAZIONE_FILTRATA = List.of(
			Ruolo.POSIZIONE_ORGANIZZATIVA, Ruolo.REFERENTE, Ruolo.RISORSA);

	public static final List<Ruolo> VALIDATORI = List.of(Ruolo.AMMINISTRATORE, Ruolo.SUPPORTO_SISTEMA,
			Ruolo.POSIZIONE_ORGANIZZATIVA);
	public static final List<Ruolo> ALTRI_VALIDATORI = List.of(Ruolo.AMMINISTRATORE, Ruolo.SUPPORTO_SISTEMA);
	public static final List<Ruolo> CAMBIA_STATO = List.of(Ruolo.AMMINISTRATORE, Ruolo.SUPPORTO_SISTEMA,
			Ruolo.POSIZIONE_ORGANIZZATIVA, Ruolo.REFERENTE);
	public static final List<Ruolo> PROPONENTI = List.of(Ruolo.AMMINISTRATORE, Ruolo.SUPPORTO_SISTEMA,
			Ruolo.POSIZIONE_ORGANIZZATIVA, Ruolo.REFERENTE, Ruolo.POSIZIONE_ORGANIZZATIVA);
	public static final List<Ruolo> ALTRI_PROPONENTI = List.of(Ruolo.AMMINISTRATORE, Ruolo.SUPPORTO_SISTEMA);

	
	public static final List<Ruolo> SCRITTURA_RISORSE = List.of(Ruolo.AMMINISTRATORE);

	protected BaseSecurityHelper() {
	}

	public final static Ruolo ruolo(final UtenteVM utente, final Integer anno) {
		if (utente.getProfili() != null && !utente.getProfili().isEmpty()) {

			for (ProfiloVM p : utente.getProfili()) {
				if (anno.equals(p.getAnno()) && !Boolean.TRUE.equals(p.getAggiunto())) {
					return p.getRuolo();
				}
			}
		}
		return null;
	}
	public final static Ruolo ruoloAggiunto(final UtenteVM utente, final Integer anno) {
		if (utente.getProfili() != null && !utente.getProfili().isEmpty()) {

			for (ProfiloVM p : utente.getProfili()) {
				if (anno.equals(p.getAnno())&& Boolean.TRUE.equals(p.getAggiunto())) {
					return p.getRuolo();
				}
			}
		}
		return null;
	}
	public final static void ruolo(final UtenteVM utente, final String username, final Integer anno,
			final WhoIsResponse out) {
		if (utente.getRisorsaUmana() != null)
			out.setCode(utente.getRisorsaUmana().getCodiceInterno());
		out.setRuolo(ruolo(utente, anno));
		out.setRuoloAggiunto(ruoloAggiunto(utente, anno));
		out.setIdRisorsa(risorsa(utente,anno));
		out.setDescription(utente.getNome());
		if (StringUtils.isBlank(out.getCode())) {
			out.setCode(username);
		}
		out.setIdEnte(utente.getIdEnte() == null ? 0l : utente.getIdEnte());
	}
	
	public final static void enablings(final WhoIsResponse out) {
		final Ruolo ruolo = out.getRuolo();
		List<Abilitazione> enablings = enablings(ruolo);
		out.setEnablings(enablings);
	}

	public final static List<Abilitazione> enablings(final Ruolo ruolo) {
		final List<Abilitazione> enablings = new ArrayList<>();
		if (ruolo == null)
			return enablings;
		enablings.add(new Abilitazione(SISTEMA).read(ADMINS.contains(ruolo)).write(ADMINS.contains(ruolo)));
		enablings.add(
				new Abilitazione(STRUTTURA).read(LETTURA_SENZA_RISORSE.contains(ruolo)).write(ADMINS.contains(ruolo)));
		enablings.add(
				new Abilitazione(STRUTTURA_AMMINISTATORI).read(LETTURA_SENZA_RISORSE.contains(ruolo)).write(ADMINS.contains(ruolo)));
		enablings.add(new Abilitazione(STRUTTURA_RISORSE).read(LETTURA_SENZA_RISORSE.contains(ruolo))
				.write(SCRITTURA.contains(ruolo)));
		enablings.add(new Abilitazione(STRUTTURA_ORGANIGRAMMA).read(LETTURA_SENZA_RISORSE.contains(ruolo))
				.write(ADMINS.contains(ruolo)));

		enablings.add(new Abilitazione(DUP).read(LETTURA.contains(ruolo)).write(SCRITTURA.contains(ruolo)));
		enablings
				.add(new Abilitazione(DUP_INTRODUZIONE).read(ADMINS.contains(ruolo)).write(ADMINS.contains(ruolo)));
		enablings.add(
				new Abilitazione(DUP_PROGRAMMAZIONE).read(LETTURA.contains(ruolo)).write(SCRITTURA.contains(ruolo)));
		enablings.add(new Abilitazione(DUP_PROGRAMMAZIONE_RISORSE).read(LETTURA_SENZA_RISORSE.contains(ruolo))
				.write(SCRITTURA.contains(ruolo)));
		enablings.add(
				new Abilitazione(DUP_CONSUNTIVAZIONE).read(LETTURA.contains(ruolo)).write(SCRITTURA.contains(ruolo)));

		enablings.add(new Abilitazione(STAMPE).read(LETTURA_SENZA_RISORSE.contains(ruolo)).write(ADMINS.contains(ruolo)));

		enablings.add(new Abilitazione(QUESTIONARI).read(ADMINS.contains(ruolo)).write(ADMINS.contains(ruolo)));
		enablings.add(new Abilitazione(REGISTRAZIONI).read(ADMINS.contains(ruolo)).write(ADMINS.contains(ruolo)));
		enablings.add(new Abilitazione(REGOLAMENTI).read(ADMINS.contains(ruolo)).write(ADMINS.contains(ruolo)));
		enablings.add(new Abilitazione(PRIORITA).read(LETTURA_SENZA_RISORSE.contains(ruolo)).write(SCRITTURA.contains(ruolo)));
		enablings.add(new Abilitazione(SCHEDA).read(LETTURA.contains(ruolo)).write(SCRITTURA.contains(ruolo)));

		return enablings;
	}

	
	public final static UtenteVM utente(final IUtenteBusiness utenteBusiness) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username = auth.getName();
		return utenteBusiness.leggiPerUserid(username);
	}
	
	public final static Long risorsa(final UtenteVM utente, final Integer anno) {
		if (utente.getProfili() != null && !utente.getProfili().isEmpty()) {

			for (ProfiloVM p : utente.getProfili()) {
				if (anno.equals(p.getAnno()) && !Boolean.TRUE.equals(p.getAggiunto()) ) {
					return p.getIdRisorsa();
				}
			}
		}
		return null;
	}
	public final static ProfiloVM profilo(final UtenteVM utente, final Integer anno) {
		if (utente.getProfili() != null && !utente.getProfili().isEmpty()) {

			for (ProfiloVM p : utente.getProfili()) {
				if (anno.equals(p.getAnno()) && !Boolean.TRUE.equals(p.getAggiunto())) {
					return p;
				}
			}
		}
		return null;
	}
	public final static ProfiloVM profiloAggiunto(final UtenteVM utente, final Integer anno) {
		if (utente.getProfili() != null && !utente.getProfili().isEmpty()) {

			for (ProfiloVM p : utente.getProfili()) {
				if (anno.equals(p.getAnno()) && Boolean.TRUE.equals(p.getAggiunto())) {
					return p;
				}
			}
		}
		return null;
	}
	public final static Abilitazione enabling(String name, final List<Abilitazione> enablings) {
		final Abilitazione out = new Abilitazione(name);
		if (enablings == null || enablings.isEmpty())
			return out;
		for (Abilitazione e : enablings) {
			if (name.equalsIgnoreCase(e.getName())) {
				return out.read(e.isRead()).write(e.isWrite());
			}
		}
		int k = name.indexOf('.');
		if (k > 0) {
			String parent = name.substring(0, k);
			for (Abilitazione e : enablings) {
				if (parent.equalsIgnoreCase(e.getName())) {
					return out.read(e.isRead()).write(e.isWrite());
				}
			}
		}
		return out;
	}

}
