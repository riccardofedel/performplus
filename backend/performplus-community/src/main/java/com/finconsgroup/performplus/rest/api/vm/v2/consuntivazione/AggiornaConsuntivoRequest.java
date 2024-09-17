package com.finconsgroup.performplus.rest.api.vm.v2.consuntivazione;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

import lombok.Data;
import lombok.ToString;

@SuppressWarnings("serial")
@Data
@ToString
public class AggiornaConsuntivoRequest implements Serializable{
  private Long id;
  private LocalDate dataRilevazione;
  private Integer anno;
  private Integer periodo;
  private Boolean valoreBooleano;
  private Double valoreNumerico;
  private Double valoreNumericoA;
  private Double valoreNumericoB;
  private LocalDate valoreTemporale;
  
}
