package com.finconsgroup.performplus.service.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.persistence.Column;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.finconsgroup.performplus.enumeration.Fascia;
import com.finconsgroup.performplus.enumeration.Gruppo;
import com.finconsgroup.performplus.enumeration.StatoPiano;
import com.finconsgroup.performplus.enumeration.TipoNodo;
import com.finconsgroup.performplus.enumeration.TipoPiano;
import com.finconsgroup.performplus.service.business.utils.Mapping;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode
@ToString
public class NodoPianoDTO extends NodoPianoEasyDTO {

	public NodoPianoDTO(Long idEnte, Integer anno, TipoNodo tipoNodo) {
		super(idEnte, anno, tipoNodo);
	}

	public NodoPianoDTO(Long idEnte, Integer anno) {
		super(idEnte, anno);
	}

	public NodoPianoDTO() {
		super();
	}
	private PianoEasyDTO piano;

	private String note;
	private String noteConsuntivo;
	private LocalDate dataModificaNota;
	private String descrizione;
	private List<NodoPianoDTO> figli;
	private List<IndicatorePianoDTO> indicatori;
	private List<OrganizzazioneDTO> organizzazioni;
	private List<StakeholderDTO> stakeholders;
	private BigDecimal peso;
	private Integer percentualeFigli;
	private BigDecimal prioritaStrategica;
	private OrganizzazioneDTO organizzazione;
	private boolean figliCrossed;
	private List<String> stakeholdersCollegati;
	
	private String portatoriInteressi;
	private String altriEnti;

	private List<String> assessoriCollegati;
	private Boolean bloccato=false;

	private List<RisorsaUmanaDTO> amministratori;

	private Fascia LivelloStrategicita;
	private Fascia LivelloComplessita;

	private EntityDTO collegamentoEsterno;

	private LocalDate inizio;
	private LocalDate scadenza;
	private EntityDTO predecessore;

	private String codiceEsterno;
	private String soggettiEsterni;
	private Boolean prenotazione;

	private LocalDate inizioEffettivo;
	private LocalDate scadenzaEffettiva;

	private Integer annoInizio;
	private Integer annoFine;
	private LocalDate approvazione;
	private TipoPiano tipoPiano;
	private StatoPiano stato;
	private EntityDTO pianoOrigine;
	private String introduzioneObiettivoOperativo;

	private boolean strategico;


	public void add(IndicatorePianoDTO indicatore) {
		if (getIndicatori() == null)
			setIndicatori(new ArrayList<>());
		for (IndicatorePianoDTO a : getIndicatori()) {
			if (a.getId().longValue() == indicatore.getId().longValue())
				return;
		}
		getIndicatori().add(indicatore);
	}

	public void remove(IndicatorePianoDTO indicatore) {
		if (getIndicatori() == null || indicatore == null)
			return;
		int n = getIndicatori().size();
		for (int i = 0; i < n; i++) {
			if (getIndicatori().get(i).getId().equals(indicatore.getId())) {
				getIndicatori().remove(i);
				break;
			}
		}
	}

	public void add(OrganizzazioneDTO uo) {
		if (getOrganizzazioni() == null)
			setOrganizzazioni(new ArrayList<>());
		for (OrganizzazioneDTO a : getOrganizzazioni()) {
			if (a.getId().equals(uo.getId()))
				return;
		}
		getOrganizzazioni().add(uo);
	}

	public void remove(final OrganizzazioneDTO uo) {
		if (getOrganizzazioni() == null || uo == null)
			return;
		getOrganizzazioni().removeIf(o -> o.getId().equals(uo.getId()));
	}

	public void add(StakeholderDTO nodo) {
		if (getStakeholders() == null)
			setStakeholders(new ArrayList<>());
		for (StakeholderDTO a : getStakeholders()) {
			if (a.getId().equals(nodo.getId()))
				return;
		}
		getStakeholders().add(nodo);
	}

	public void remove(StakeholderDTO nodo) {
		if (getStakeholders() == null || nodo == null)
			return;
		getStakeholders().removeIf(s -> s.getId().equals(nodo.getId()));
	}

	@JsonIgnore
	public String getCodiceRidotto() {
		String code = getCodiceCompleto();
		int k = code.indexOf('.');
		if (k >= 0) {
			code = code.substring(k + 1);
		} else {
			code = "";
		}
		return code;
	}

//	@JsonIgnore
//	public NodoPianoDTO nodo2Tipo() {
//		switch (getTipoNodo()) {
//		case RISULTATO_ATTESO:
//			return Mapping.mapping(this, ObiettivoGestionaleDTO.class);
//		case MISSIONE:
//			return Mapping.mapping(this, ObiettivoStrategicoDTO.class);
//		case AZIONE:
//			return Mapping.mapping(this, ObiettivoOperativoDTO.class);
//		case PIANO:
//			return Mapping.mapping(this, PianoDTO.class);
//		default:
//			return this;
//		}
//
//	}
	
	@JsonIgnore
	public String getNomeCompleto() {
		StringBuilder sb = new StringBuilder();
		String tipo = getTipoNodo().name();
		sb.append(getCodiceRidotto());
		if (getDenominazione() == null || !getDenominazione().toLowerCase().startsWith(tipo.toLowerCase() + " ")) {
			sb.append(" ").append(tipo);
		}
		if (getDenominazione() != null) {
			sb.append(" ").append(getDenominazione());
		}
		return sb.toString();
	}
	
	@JsonIgnore
	public String getNomeCorto() {
		StringBuilder sb = new StringBuilder();
		sb.append(getCodiceVisibile());
		if (getDenominazione() != null)
			sb.append(" ").append(getDenominazione());
		return sb.toString();
	}

	@JsonIgnore
	public String getNomeNodo() {
		if (TipoNodo.PIANO.equals(getTipoNodo()))
			return getDenominazione();
		StringBuilder sb = new StringBuilder();
		sb.append(getTipoNodo().name()).append(" ").append(getCodiceVisibile());
		if (getDenominazione() != null)
			sb.append(" ").append(getDenominazione());
		return sb.toString();
	}

	@JsonIgnore
	public String getNome() {
		if (TipoNodo.PIANO.equals(getTipoNodo()))
			return getDenominazione();
		StringBuilder sb = new StringBuilder();
		sb.append(getTipoNodo().name());
		if (getDenominazione() != null)
			sb.append(" ").append(getDenominazione());
		return sb.toString();
	}
	@JsonIgnore
	public String getCodiceVisibile() {
		if (StringUtils.isNotBlank(getCodiceEsterno())) {
			return getCodiceEsterno();
		}
		return getCodice();
	}

	public void add(NodoPianoDTO figlio) {
		if (getFigli() == null)
			setFigli(new ArrayList<>());
		figli.add(figlio);
	}

	@JsonIgnore
	public List<OrganizzazioneDTO> getUnitaOrganizzative() {
		return getOrganizzazioni();
	}
	
	@JsonIgnore
	public List<OrganizzazioneDTO> getUnitaOrganizzativePrincipali() {
		final List<OrganizzazioneDTO> out=new ArrayList<>();
		if(getOrganizzazione()!=null)
			out.add(getOrganizzazione());
		return out;
	}

	@JsonIgnore
	public boolean isCrossed() {
		return getOrganizzazioni()!=null&& !getOrganizzazioni().isEmpty();
	}

	public String getGruppo() {
		if(getTipoNodo()==null)
			return null;
		switch(getTipoNodo()) {
		case PIANO:
		case AREA:
			return Gruppo.LPM.name();
		case OBIETTIVO:
			return Gruppo.OBIETTIVO_OPERATIVO.name();
		case AZIONE:
			return Gruppo.PDO.name();
		}
		return null;
	}
}
