package com.finconsgroup.performplus.rest.api.vm.v2.consuntivazione;

import java.time.LocalDate;

import com.finconsgroup.performplus.enumeration.CalcoloConsuntivazione;
import com.finconsgroup.performplus.enumeration.Periodicita;
import com.finconsgroup.performplus.enumeration.TipoFormula;
import com.finconsgroup.performplus.rest.api.vm.v2.ValoreDetailVM;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode
@ToString
public class ValutazioneVM extends ValoreDetailVM{
	private Long idIndicatore;
	private TipoFormula tipoFormula;
	private CalcoloConsuntivazione calcoloConsuntivazione; 
	private String descrizione;
	private String numeratore;
	private String denominatore;
	private String note;
	private Double peso;
	private Boolean percentuale;
	private Boolean decrescente;
	private Boolean strategico;
	private Boolean sviluppoSostenibile;
	private Double pesoRelativo;
    private Integer decimali;
    private Integer decimaliA;
    private Integer decimaliB;
    private LocalDate dataRilevazione;
    private String unitaMisura;
    private String storico;
    private String baseline;
    private String target;
    private Periodicita periodicitaTarget;
    private Periodicita periodicitaRend;

}
