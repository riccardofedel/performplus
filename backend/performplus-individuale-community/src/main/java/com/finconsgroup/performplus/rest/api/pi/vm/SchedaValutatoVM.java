package com.finconsgroup.performplus.rest.api.pi.vm;

import java.time.LocalDateTime;
import java.util.List;

import com.finconsgroup.performplus.rest.api.vm.EntityVM;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode
@ToString
@NoArgsConstructor
public class SchedaValutatoVM extends EntityVM{
   private LocalDateTime dataOraCalcolo;
   private String codiceUnivoco;
   private Long idEnte;
   private Integer anno;
   private String cognome;
   private String nome;
   private Float valutazione;
   private List<SchedaVM> valutazioni;
}
