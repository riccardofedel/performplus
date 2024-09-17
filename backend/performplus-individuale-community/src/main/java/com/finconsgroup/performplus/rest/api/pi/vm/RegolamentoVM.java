package com.finconsgroup.performplus.rest.api.pi.vm;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import com.finconsgroup.performplus.rest.api.vm.DecodificaVM;
import com.finconsgroup.performplus.rest.api.vm.EntityVM;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode
@ToString
public class RegolamentoVM extends EntityVM{
	private Long idEnte = 0l;
	private Integer anno;
	private BigDecimal pesoObiettiviIndividuali=BigDecimal.ZERO;
	private BigDecimal pesoObiettiviDiStruttura=BigDecimal.ZERO;
	private BigDecimal pesoObiettiviDiPerformance=BigDecimal.ZERO;
	private BigDecimal pesoComportamentiOrganizzativi=BigDecimal.ZERO;
	private String intestazione;
	private Boolean po = false;
	private List<Long> categorie;
	private List<Long> incarichi;
	private String descCategorie;
	private String descIncarichi;

}
