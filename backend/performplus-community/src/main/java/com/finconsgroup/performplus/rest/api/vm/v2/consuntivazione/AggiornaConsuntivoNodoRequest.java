package com.finconsgroup.performplus.rest.api.vm.v2.consuntivazione;

import java.io.Serializable;
import java.time.LocalDate;

import lombok.Data;
import lombok.ToString;

@SuppressWarnings("serial")
@Data
@ToString
public class AggiornaConsuntivoNodoRequest implements Serializable{
  private LocalDate dataInizio;
  private LocalDate dataScadenza;
  private String rendicontazioneDescrittiva;
}
