package com.finconsgroup.performplus.rest.api.pi.vm;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import com.finconsgroup.performplus.domain.AbstractAuditingEntity;
import com.finconsgroup.performplus.domain.AmbitoValutazione;
import com.finconsgroup.performplus.domain.Questionario;
import com.finconsgroup.performplus.rest.api.vm.EntityVM;
import com.finconsgroup.performplus.service.dto.EnteInterface;

import lombok.Data;
import lombok.EqualsAndHashCode;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode
public class AmbitoValutazioneVM extends EntityVM implements EnteInterface {
	private Long idQuestionario;
	private String codice;
	private String intestazione;
	private String descrizione;
	private Long idEnte = 0l;
	private boolean foglia = false;
	private Long idPadre;
	private String codiceCompleto;
	private BigDecimal peso;
	private BigDecimal pesoMancataAssegnazione;
	private BigDecimal pesoMancatoColloquio;
	private Boolean flagSoloAdmin=false;


}
