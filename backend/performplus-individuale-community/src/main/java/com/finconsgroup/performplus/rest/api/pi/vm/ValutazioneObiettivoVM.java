package com.finconsgroup.performplus.rest.api.pi.vm;

import java.time.LocalDate;
import java.util.List;

import com.finconsgroup.performplus.domain.Organizzazione;
import com.finconsgroup.performplus.domain.SchedaValutazioneNodo;
import com.finconsgroup.performplus.enumeration.TipoNodo;
import com.finconsgroup.performplus.enumeration.TipoRegolamento;
import com.finconsgroup.performplus.rest.api.vm.EntityVM;
import com.finconsgroup.performplus.service.business.utils.NodoPianoHelper;
import com.finconsgroup.performplus.service.business.utils.OrganizzazioneHelper;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode
@ToString
@NoArgsConstructor
public class ValutazioneObiettivoVM {
	String struttura;
	Long idStruttura;
	Long idSchedaValutazioneObiettivo;
	String obiettivo;
	Long idObiettivo;
	String tipoNodo;
	String tipoRegolamento;
	LocalDate inizio;
	LocalDate scadenza;
	Float raggiungimentoObiettivo;
	Float pesoObiettivo;
	Float pesoForzatoObiettivo;
	Float valutazioneObiettivo;
	Float forzaturaObiettivo;
	String noteForzaturaObiettivo;
	String tipoObiettivo;
	String codiceObiettivo;

	public ValutazioneObiettivoVM(Organizzazione org) {
		this.idStruttura=org.getId();
		this.struttura=OrganizzazioneHelper.getNomeCompleto(org);
	}
	public ValutazioneObiettivoVM(SchedaValutazioneNodo svn) {
		this.setTipoRegolamento(svn.getTipoRegolamento().getLabel());
		this.setTipoNodo(svn.getNodoPiano().getTipoNodo().getLabel());
		this.setInizio(svn.getNodoPiano().getInizio());
		this.setScadenza(svn.getNodoPiano().getScadenza());
		this.idSchedaValutazioneObiettivo=svn.getId();
		this.idObiettivo=svn.getNodoPiano().getId();
		this.obiettivo=NodoPianoHelper.ridotto(svn.getNodoPiano().getCodiceCompleto())+ " " +svn.getNodoPiano().getDenominazione();
		this.raggiungimentoObiettivo=svn.getRaggiungimento();
		this.pesoObiettivo=svn.getPeso();
		this.pesoForzatoObiettivo=svn.getPesoForzato();
		this.valutazioneObiettivo=svn.getValutazione();
		this.forzaturaObiettivo=svn.getForzatura();
		this.noteForzaturaObiettivo=svn.getNoteForzatura();
		this.tipoObiettivo=svn.getNodoPiano().getTipo()==null?"":svn.getNodoPiano().getTipo().getLabel();
		this.codiceObiettivo=svn.getNodoPiano().getCodiceInterno();
		}

}
