package com.finconsgroup.performplus.rest.api.vm.v2.programmazione;

import com.finconsgroup.performplus.enumeration.TipoNodo;
import com.finconsgroup.performplus.rest.api.vm.EntityVM;
import com.finconsgroup.performplus.service.business.utils.NodoPianoHelper;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode
@ToString
public class NodoPianoSmartVM extends EntityVM {
	private String codiceCompleto;
	private String denominazione;
	private TipoNodo tipoNodo;
	private Long idPadre;
	private String codiceCompletoStruttura;
	private Integer anno;
	private Integer annoInizio;
	private Integer annoFine;
	private Boolean strategico;
	private String statoNodo;
	private Long idStruttura;
	private int ordine=0;
	private String codiceInterno;
	

	public NodoPianoSmartVM idPadre(Long idPadre) {
		setIdPadre(idPadre);
		return this;
	}
	public NodoPianoSmartVM annoInizio(Integer annoInizio) {
		setAnnoInizio(annoInizio);
		return this;
	}
	public NodoPianoSmartVM annoFine(Integer annoFine) {
		setAnnoFine(annoFine);
		return this;
	}
	public NodoPianoSmartVM codiceCompletoStruttura(String codiceCompletoStruttura) {
		this.codiceCompletoStruttura=codiceCompletoStruttura;
		return this;
	}
	public NodoPianoSmartVM strategico(Boolean strategico) {
		this.strategico=strategico;
		return this;
	}
	public String getCodiceRidotto() {
		return NodoPianoHelper.ridotto(codiceCompleto);
	}
	public String getNomeCompleto() {
		return getCodiceRidotto()+" "+getDenominazione();
	}
	public NodoPianoSmartVM ordine(int ordine) {
		this.ordine=ordine;
		return this;
	}
	public NodoPianoSmartVM statoNodo(String statoNodo) {
		this.statoNodo=statoNodo;
		return this;
	}
	public NodoPianoSmartVM idStruttura(Long idStruttura) {
		this.idStruttura=idStruttura;
		return this;
	}
	public NodoPianoSmartVM codiceIterno(String codiceInterno) {
		this.codiceInterno=codiceInterno;
		return this;
	}
}
