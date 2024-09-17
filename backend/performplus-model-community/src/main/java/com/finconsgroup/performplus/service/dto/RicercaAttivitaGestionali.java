package com.finconsgroup.performplus.service.dto;

import java.io.Serializable;
import java.time.LocalDate;

import lombok.Data;

@SuppressWarnings("serial")
@Data
public class RicercaAttivitaGestionali implements Serializable{
    private String testoRicerca;
    private String codice;
    private LocalDate minDate;
    private LocalDate maxDate;
    private Integer anno;
    private Long idEnte=0l;

}