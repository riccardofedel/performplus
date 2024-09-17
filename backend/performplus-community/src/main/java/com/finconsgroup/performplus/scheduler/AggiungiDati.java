package com.finconsgroup.performplus.scheduler;

import java.io.FileReader;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.finconsgroup.performplus.domain.NodoPiano;
import com.finconsgroup.performplus.domain.Organizzazione;
import com.finconsgroup.performplus.domain.Profilo;
import com.finconsgroup.performplus.domain.RisorsaUmana;
import com.finconsgroup.performplus.domain.TemplateData;
import com.finconsgroup.performplus.domain.Utente;
import com.finconsgroup.performplus.enumeration.Disponibile;
import com.finconsgroup.performplus.enumeration.Livello;
import com.finconsgroup.performplus.enumeration.Ruolo;
import com.finconsgroup.performplus.enumeration.StatoPiano;
import com.finconsgroup.performplus.enumeration.TipoNodo;
import com.finconsgroup.performplus.enumeration.TipoPiano;
import com.finconsgroup.performplus.enumeration.TipoStruttura;
import com.finconsgroup.performplus.enumeration.TipologiaStruttura;
import com.finconsgroup.performplus.manager.security.IEncrypterManager;
import com.finconsgroup.performplus.repository.NodoPianoRepository;
import com.finconsgroup.performplus.repository.OrganizzazioneRepository;
import com.finconsgroup.performplus.repository.ProfiloRepository;
import com.finconsgroup.performplus.repository.RisorsaUmanaRepository;
import com.finconsgroup.performplus.repository.TemplateDataRepository;
import com.finconsgroup.performplus.repository.UtenteRepository;
import com.finconsgroup.performplus.service.business.utils.DateHelper;
import com.finconsgroup.performplus.service.business.utils.NodoPianoHelper;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class AggiungiDati {

	@Autowired
	private OrganizzazioneRepository organizzazioneRepository;
	@Autowired
	private RisorsaUmanaRepository risorsaUmanaRepository;
	@Autowired
	private NodoPianoRepository nodoPianoRepository;

	@Autowired
	private UtenteRepository utenteRepository;

	@Autowired
	private ProfiloRepository profiloRepository;
	@Autowired
	private TemplateDataRepository templateDataRepository;

	@Autowired
	private IEncrypterManager encrypterManager;

	@Value("${path.files}")
	String pathFiles;

	private Organizzazione ente;
	private Organizzazione direzioneSanitaria;
	private RisorsaUmana resp001;
	private RisorsaUmana resp002;
	private RisorsaUmana resp003;
	private RisorsaUmana resp004;
	private NodoPiano piano;

	public long eseguiSistema() {
		long nro = risorse();
		if (nro > 0)
			return nro;
		strutture();
		piano();
		utenti();
		templates();
		return 0;
	}

	private void templates() {
	
		/*
		 * 
'["idPadre","tipoNodo","codiceRidottoPadre","codice","denominazione","descrizione","inizio","scadenza","responsabili","organizzazione","organizzazioni","note"]', 'PROGRAMMAZIONE', 'OBIETTIVO', 'system', 'system');
 '["idPadre","tipoNodo","codiceRidottoPadre","codice","denominazione","descrizione","inizio","scadenza","responsabili","organizzazione","organizzazioni","note"]', 'PROGRAMMAZIONE', 'AZIONE', 'system', 'system');
'["idPadre","tipoNodo","codiceRidottoPadre","codice","denominazione","descrizione","inizio","scadenza","responsabili","organizzazione","organizzazioni","note"]', 'PROGRAMMAZIONE', 'FASE', 'system', 'system');

		 */
		TemplateData td=new TemplateData();
		td.setContainer("PROGRAMMAZIONE");
		td.setType("PIANO");
		td.setFormlyFieldConfig("[\"denominazione\",\"descrizione\",\"inizio\",\"scadenza\"]");
		td.setIdEnte(0l);
		td.setId(1l);	
		templateDataRepository.save(td);
		td=new TemplateData();
		td.setContainer("PROGRAMMAZIONE");
		td.setType("AREA");
		td.setFormlyFieldConfig("[\"idPadre\",\"tipoNodo\",\"codice\",\"denominazione\",\"descrizione\",\"inizio\",\"scadenza\",\"responsabili\",\"organizzazione\",\"note\"]");
		td.setIdEnte(0l);
		td.setId(2l);	
		templateDataRepository.save(td);
		td=new TemplateData();
		td.setContainer("PROGRAMMAZIONE");
		td.setType("OBIETTIVO");
		td.setFormlyFieldConfig("[\"idPadre\",\"tipoNodo\",\"codiceRidottoPadre\",\"codice\",\"denominazione\",\"descrizione\",\"inizio\",\"scadenza\",\"responsabili\",\"organizzazione\",\"note\"]");
		td.setIdEnte(0l);
		td.setId(3l);	
		templateDataRepository.save(td);
		td=new TemplateData();
		td.setContainer("PROGRAMMAZIONE");
		td.setType("AZIONE");
		td.setFormlyFieldConfig("[\"idPadre\",\"tipoNodo\",\"codiceRidottoPadre\",\"codice\",\"denominazione\",\"descrizione\",\"inizio\",\"scadenza\",\"responsabili\",\"organizzazione\",\"note\"]");
		td.setIdEnte(0l);
		td.setId(4l);	
		templateDataRepository.save(td);
		td=new TemplateData();
		td.setContainer("FASE");
		td.setType("PIANO");
		td.setFormlyFieldConfig("[\"idPadre\",\"tipoNodo\",\"codiceRidottoPadre\",\"codice\",\"denominazione\",\"descrizione\",\"inizio\",\"scadenza\",\"responsabili\",\"organizzazione\",\"note\"]");
		td.setIdEnte(0l);
		td.setId(5l);	
		templateDataRepository.save(td);
	}

	private void utenti() {
		long nro = utenteRepository.count();
		if (nro > 0)
			return;
		Utente u = new Utente();
		u.setIdEnte(0l);
		u.setAdmin(true);
		u.setNome("admin");
		u.setUserid("admin");
		u.setPasswd(encrypterManager.encode("Community2024!"));
		u = utenteRepository.save(u);
		Profilo p = new Profilo();
		p.setAnno(DateHelper.getAnno());
		p.setIdEnte(0l);
		p.setRuolo(Ruolo.AMMINISTRATORE);
		p.setOrganizzazione(ente);
		p.setUtente(u);
		profiloRepository.save(p);
	}

	private void strutture() {
		long nro = organizzazioneRepository.count();
		if (nro > 0)
			return;
		try {
			Organizzazione o = new Organizzazione();
			o.setIdEnte(0l);
			o.setAnno(DateHelper.getAnno());
			o.setCodice("ente");
			o.setCodiceCompleto("ente");
			o.setDescrizione("Azienda Community");
			o.setIntestazione("Community");
			o.setLivello(Livello.ENTE);
			o.setInizioValidita(DateHelper.inizioAnnoInCorso());
			o.setFineValidita(DateHelper.MAX);

			o.setOrdine(0);

			ente = organizzazioneRepository.save(o);

			o = new Organizzazione();
			o.setIdEnte(0l);
			o.setAnno(DateHelper.getAnno());
			o.setCodice("001");
			o.setCodiceCompleto("ente.001");
			o.setDescrizione("Direzione uno");
			o.setIntestazione("Direzione uno");
			o.setLivello(Livello.SUPERIORE);
			o.setOrdine(1);
			o.setPadre(ente);
			o.setCodiceInterno("1");
			o.setInizioValidita(DateHelper.inizioAnnoInCorso());
			o.setFineValidita(DateHelper.MAX);
			o.setResponsabile(resp001);
			direzioneSanitaria = organizzazioneRepository.save(o);

			strutture(pathFiles + "/strutture.csv");
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	private long risorse() {
		long nro = risorsaUmanaRepository.count();
		if (nro > 0)
			return nro;
		RisorsaUmana r = new RisorsaUmana();
		r.setIdEnte(0l);
		r.setAnno(DateHelper.getAnno());
		r.setOrdine(1);
		r.setCodiceFiscale("CDCFSC01E01F001L");
		r.setCodiceInterno("100000");
		r.setCognome("ROSSI");
		r.setNome("MARIO");
		r.setDisponibile(Disponibile.DISPONIBILE);
		r.setEmail("mario.rossi@community.com");
		r.setEsterna(false);
		r.setPolitico(false);
		r.setPartTime(false);
		r.setInizioValidita(DateHelper.inizioAnnoInCorso());
		r.setFineValidita(DateHelper.MAX);
		this.resp001 = risorsaUmanaRepository.save(r);
		r = new RisorsaUmana();
		r.setIdEnte(0l);
		r.setAnno(DateHelper.getAnno());
		r.setOrdine(1);
		r.setCodiceFiscale("CDCFSC02E02F002L");
		r.setCodiceInterno("200000");
		r.setCognome("VERDI");
		r.setNome("LUIGI");
		r.setDisponibile(Disponibile.DISPONIBILE);
		r.setEmail("luigi.verdi@community.com");
		r.setEsterna(false);
		r.setPolitico(false);
		r.setPartTime(false);
		r.setInizioValidita(DateHelper.inizioAnnoInCorso());
		r.setFineValidita(DateHelper.MAX);
		this.resp002 = risorsaUmanaRepository.save(r);
		r = new RisorsaUmana();
		r.setIdEnte(0l);
		r.setAnno(DateHelper.getAnno());
		r.setOrdine(1);
		r.setCodiceFiscale("CDCFSC03E03F003L");
		r.setCodiceInterno("300000");
		r.setCognome("BIANCHI");
		r.setNome("ANTONIO");
		r.setDisponibile(Disponibile.DISPONIBILE);
		r.setEmail("antonio.bianchi@community.com");
		r.setEsterna(false);
		r.setPolitico(false);
		r.setPartTime(false);
		r.setInizioValidita(DateHelper.inizioAnnoInCorso());
		r.setFineValidita(DateHelper.MAX);
		this.resp003 = risorsaUmanaRepository.save(r);
		r = new RisorsaUmana();
		r.setIdEnte(0l);
		r.setAnno(DateHelper.getAnno());
		r.setOrdine(1);
		r.setCodiceFiscale("CDCFSC04E03F003L");
		r.setCodiceInterno("400000");
		r.setCognome("GIALLI");
		r.setNome("ENRICO");
		r.setDisponibile(Disponibile.DISPONIBILE);
		r.setEmail("enrico.gialli@community.com");
		r.setEsterna(false);
		r.setPolitico(false);
		r.setPartTime(false);
		r.setInizioValidita(DateHelper.inizioAnnoInCorso());
		r.setFineValidita(DateHelper.MAX);
		this.resp004 = risorsaUmanaRepository.save(r);

		return 0;
	}

	private void piano() {
		long nro = nodoPianoRepository.count();
		if (nro > 0)
			return;

		NodoPiano np = new NodoPiano();
		np.setTipoNodo(TipoNodo.PIANO);
		np.setIdEnte(0l);
		np.setAnno(DateHelper.getAnno());
		np.setOrdine(0);
		np.setAnnoInizio(np.getAnno());
		np.setAnnoFine(np.getAnno() + 4);
		np.setCodice("piano_" + np.getAnno());
		np.setCodiceCompleto("piano_" + np.getAnno());
		np.setInizio(DateHelper.inizioAnnoInCorso());
		np.setScadenza(DateHelper.fineAnno(np.getAnnoFine()));
		np.setDenominazione("Piano " + np.getAnno());
		np.setOrganizzazione(ente);
		np.setStatoPiano(StatoPiano.ATTIVO);
		np.setTipoPiano(TipoPiano.PIANO);
		this.piano = nodoPianoRepository.save(np);

	}

	public void strutture(final String path) throws Throwable {

		CSVParser csvParser = new CSVParserBuilder().withQuoteChar('"')
				// .withEscapeChar('"')
				.withIgnoreLeadingWhiteSpace(true).withStrictQuotes(false).withSeparator(';').build();
		List<String[]> r;
		try (CSVReader reader = new CSVReaderBuilder(new FileReader(path)).withCSVParser(csvParser) // custom CSV
																									// parser
				.withSkipLines(1) // skip the first line, header info
				.build()) {
			r = reader.readAll();
			reader.close();
		} catch (Throwable e) {
			System.err.println(e.getMessage());
			return;
		}
		try {

			int k = 0;
			/*
			 * dipartimento,tipo,tipologia,descrizione
			 */

			for (String[] a : r) {

				int n = 0;

				String dipartimento = a[n++];
				String tipo = a[n++];
				String tipologia = a[n++];
				String descrizione = a[n++];
				eseguiSt(dipartimento, tipo, tipologia, descrizione, k);
			}

		} catch (Throwable e) {
			System.err.println(e.getMessage());
			throw new Exception(e.getMessage());
		}
		System.out.println("---FINE STRUTTURE ");

	}

	private void eseguiSt(String dipartimento, String tipo, String tipologia, String descrizione, int k) {
		TipologiaStruttura tipologiaStruttura = tipologia(tipologia);
		TipoStruttura tipoStruttura = tipo(tipologia);
		if (tipologiaStruttura == null || StringUtils.isBlank(dipartimento))
			return;
		Organizzazione dip = null;
		Organizzazione o = new Organizzazione();
		o.setIdEnte(0l);
		o.setAnno(DateHelper.getAnno());
		if (!tipologiaStruttura.equals(TipologiaStruttura.DIPARTIMENTO)) {
			List<Organizzazione> dips = organizzazioneRepository
					.findByIdEnteAndAnnoAndIntestazioneAndTipologiaStruttura(0l, DateHelper.getAnno(), dipartimento,
							TipologiaStruttura.DIPARTIMENTO);
			if (dips == null || dips.isEmpty()) {
				System.err.println("Non trovato dipartimento:" + dipartimento);
				return;
			}
			dip = dips.get(0);
			o.setPadre(dip);
			o.setLivello(Livello.INFERIORE);
			o.setDescrizione(descrizione);
			o.setIntestazione(descrizione);
		} else {
			o.setLivello(Livello.MEDIO);
			o.setPadre(direzioneSanitaria);
			o.setDescrizione(dipartimento);
			o.setIntestazione(dipartimento);
		}
		int nro = organizzazioneRepository.countByPadre(o.getPadre());
		o.setCodice(StringUtils.leftPad((nro+1) + "", 3, '0'));
		o.setCodiceCompleto(o.getPadre().getCodiceCompleto() + "." + o.getCodice());
		o.setOrdine(o.getLivello().ordinal());
		o.setCodiceInterno(NodoPianoHelper.ridotto(o.getCodiceCompleto()));
		o.setInizioValidita(DateHelper.inizioAnnoInCorso());
		o.setFineValidita(DateHelper.MAX);
		o.setTipologiaStruttura(tipologiaStruttura);
		o.setTipoStruttura(tipoStruttura);
		int r = (k % 4) + 1;
		if (r == 1)
			o.setResponsabile(resp001);
		else if (r == 2)
			o.setResponsabile(resp002);
		else if (r == 3)
			o.setResponsabile(resp003);
		else if (r == 4)
			o.setResponsabile(resp004);

		organizzazioneRepository.save(o);

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

	private TipoStruttura tipo(String tipo) {
		if (StringUtils.isBlank(tipo))
			return null;
		if (tipo.toLowerCase().startsWith("osp"))
			return TipoStruttura.OSPEDALIERA;
		if (tipo.toLowerCase().startsWith("ter"))
			return TipoStruttura.TERRITORIALE;

		return null;
	}

	private TipologiaStruttura tipologia(String tipologia) {
		if (StringUtils.isBlank(tipologia))
			return null;
		if (tipologia.toLowerCase().startsWith("dip"))
			return TipologiaStruttura.DIPARTIMENTO;
		if (tipologia.toLowerCase().startsWith("stru"))
			return TipologiaStruttura.STRUTT_COMP;
		if (tipologia.toLowerCase().equals("uos"))
			return TipologiaStruttura.UOS;
		if (tipologia.toLowerCase().equals("uosvd"))
			return TipologiaStruttura.UOSVD;

		return null;
	}

}
