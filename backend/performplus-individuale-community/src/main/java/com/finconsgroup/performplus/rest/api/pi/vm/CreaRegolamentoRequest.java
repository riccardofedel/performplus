package com.finconsgroup.performplus.rest.api.pi.vm;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode()
@ToString
public class CreaRegolamentoRequest implements Serializable{
	private Long idEnte = 0l;
	@NotNull(message = "Anno obbligatorio")
	private Integer anno;
	private BigDecimal pesoObiettiviIndividuali=BigDecimal.ZERO;
	private BigDecimal pesoObiettiviDiStruttura=BigDecimal.ZERO;
	private BigDecimal pesoObiettiviDiPerformance=BigDecimal.ZERO;
	private BigDecimal pesoComportamentiOrganizzativi=BigDecimal.ZERO;
	@NotBlank(message = "Intestazione obbligatoria")
	private String intestazione;
	private Boolean po = false;
	private List<Long> categorie;
	private List<Long> incarichi;

}
