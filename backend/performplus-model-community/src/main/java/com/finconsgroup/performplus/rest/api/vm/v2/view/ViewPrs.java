package com.finconsgroup.performplus.rest.api.vm.v2.view;

import java.io.Serializable;
import java.time.LocalDate;

import jakarta.persistence.Entity;

import org.springframework.data.annotation.Immutable;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode()
@ToString

public class ViewPrs implements Serializable {
	private String tipo_nodo;
	private String codice_interno;
	private String codice_completo;
	private String denominazione;
	private LocalDate inizio;
	private LocalDate scadenza;
	private String stato_nodo_piano;
	private String stato_proposta;
	private Boolean strategico;
	private String note_proposta;
	private Integer anno;
	private String prs;
	private Integer anno_inizio;
	private Integer anno_fine;
	private String padre;
	private String direzione;
	private String responsabile_direzione;
	private String responsabile_azione;
	private Double peso;
	private String note;
	private String area;
	private Double target1;
	private Double target2;
	private Double target3;
	private Double target4;
	private Double risultato1;
	private String stato_avanzamento1;
	private String stato_rendicontazione1;
	private Double risultato2;
	private String stato_avanzamento2;
	private String stato_rendicontazione2;
	private Double risultato3;
	private String stato_avanzamento3;
	private String stato_rendicontazione3;
	private Double risultato4;
	private String stato_avanzamento4;
	private String stato_rendicontazione4;
	private Double target11;
	private Double target12;
	private Double target13;
	private Double target14;
	private Double risultato11;
	private String stato_avanzamento11;
	private String stato_rendicontazione11;
	private Double risultato12;
	private String stato_avanzamento12;
	private String stato_rendicontazione12;
	private Double risultato13;
	private String stato_avanzamento13;
	private String stato_rendicontazione13;
	private Double risultato14;
	private String stato_avanzamento14;
	private String stato_rendicontazione14;
	private Double target21;
	private Double target22;
	private Double target23;
	private Double target24;
	private Double risultato21;
	private String stato_avanzamento21;
	private String stato_rendicontazione21;
	private Double risultato22;
	private String stato_avanzamento22;
	private String stato_rendicontazione22;
	private Double risultato23;
	private String stato_avanzamento23;
	private String stato_rendicontazione23;
	private Double risultato24;
	private String stato_avanzamento24;
	private String stato_rendicontazione24;
	private Double target31;
	private Double target32;
	private Double target33;
	private Double target34;
	private Double risultato31;
	private String stato_avanzamento31;
	private String stato_rendicontazione31;
	private Double risultato32;
	private String stato_avanzamento32;
	private String stato_rendicontazione32;
	private Double risultato33;
	private String stato_avanzamento33;
	private String stato_rendicontazione33;
	private Double risultato34;
	private String stato_avanzamento34;
	private String stato_rendicontazione34;
	private Double target41;
	private Double target42;
	private Double target43;
	private Double target44;
	private Double risultato41;
	private String stato_avanzamento41;
	private String stato_rendicontazione41;
	private Double risultato42;
	private String stato_avanzamento42;
	private String stato_rendicontazione42;
	private Double risultato43;
	private String stato_avanzamento43;
	private String stato_rendicontazione43;
	private Double risultato44;
	private String stato_avanzamento44;
	private String stato_rendicontazione44;
	private Double target51;
	private Double target52;
	private Double target53;
	private Double target54;
	private Double risultato51;
	private String stato_avanzamento51;
	private String stato_rendicontazione51;
	private Double risultato52;
	private String stato_avanzamento52;
	private String stato_rendicontazione52;
	private Double risultato53;
	private String stato_avanzamento53;
	private String stato_rendicontazione53;
	private Double risultato54;
	private String stato_avanzamento54;
	private String stato_rendicontazione54;
	private Double target61;
	private Double target62;
	private Double target63;
	private Double target64;
	private Double risultato61;
	private String stato_avanzamento61;
	private String stato_rendicontazione61;
	private Double risultato62;
	private String stato_avanzamento62;
	private String stato_rendicontazione62;
	private Double risultato63;
	private String stato_avanzamento63;
	private String stato_rendicontazione63;
	private Double risultato64;
	private String stato_avanzamento64;
	private String stato_rendicontazione64;

}
