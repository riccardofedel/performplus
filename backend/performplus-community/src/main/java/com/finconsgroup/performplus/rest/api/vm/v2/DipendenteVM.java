package com.finconsgroup.performplus.rest.api.vm.v2;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.finconsgroup.performplus.domain.RisorsaUmana;
import com.finconsgroup.performplus.domain.RisorsaUmanaOrganizzazione;
import com.finconsgroup.performplus.enumeration.Disponibile;
import com.finconsgroup.performplus.enumeration.Over;
import com.finconsgroup.performplus.rest.api.vm.v2.struttura.DisponibilitaStrutturaVM;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode
@ToString
public class DipendenteVM extends PersonaVM {
	private Boolean esterno = false;
	private Float contributo;
	private Float contributoTemporale;
	private Disponibile disponibile;
	private Integer mesi;
	private Boolean partTime = false;
	private Integer orePartTimeSettimanali;
	private Float contributoEffettivo;
	private List<DisponibilitaStrutturaVM> disponibilita;
	private LocalDate inizioValidita;
	private LocalDate fineValidita;
	private Over utilizzo;
	public DipendenteVM() {}
	public DipendenteVM(RisorsaUmana r) {
		setId(r.getId());
		setCodiceInterno(r.getCodiceInterno());
		setCognome(r.getCognome());
		setNome(r.getNome());
		this.esterno=r.getEsterna();
		this.contributo=0f;
		this.contributoTemporale=0f;
		this.disponibile=r.getDisponibile();
		this.mesi=r.getMesi();
		this.partTime=r.getPartTime();
		this.contributoEffettivo=0f;
		this.disponibilita=new ArrayList<>();
		this.utilizzo=Over.SOTTO_UTILIZZATO;
		this.orePartTimeSettimanali=r.getOrePartTime();
		setCodiceFiscale(r.getCodiceFiscale());
	}
	public DipendenteVM(RisorsaUmanaOrganizzazione ro) {
		this(ro.getRisorsaUmana());
		this.disponibilita =new ArrayList<>();
		this.disponibilita.add(new DisponibilitaStrutturaVM(ro.getOrganizzazione()));
	}

}
