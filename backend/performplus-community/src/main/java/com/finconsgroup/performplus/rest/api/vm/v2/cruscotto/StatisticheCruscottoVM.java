package com.finconsgroup.performplus.rest.api.vm.v2.cruscotto;

import java.io.Serializable;

import lombok.Data;
import lombok.ToString;

@SuppressWarnings("serial")
@Data
@ToString
public class StatisticheCruscottoVM implements Serializable{
   private Float statoAvanzamentoPiano;
   private Float statoAvanzamentoConsuntivazione;
}
