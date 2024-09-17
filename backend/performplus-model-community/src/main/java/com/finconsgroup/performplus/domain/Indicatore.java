package com.finconsgroup.performplus.domain;

import com.finconsgroup.performplus.enumeration.CalcoloConsuntivazione;
import com.finconsgroup.performplus.enumeration.RaggruppamentoIndicatori;
import com.finconsgroup.performplus.enumeration.TipoFormula;
import com.finconsgroup.performplus.service.dto.EnteInterface;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "INDICATORE")
@Data
@EqualsAndHashCode

public class Indicatore extends AbstractAuditingEntity implements EnteInterface {

	private static final long serialVersionUID = -2370684681575480509L;

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected Long id;

	@Column(name = "DECIMALI")
	private Integer decimali;
	@Column(name = "DECIMALI_A")
	private Integer decimaliA;
	@Column(name = "DECIMALI_B")
	private Integer decimaliB;
	@Column(name = "DENOMINAZIONE", nullable = false)
	private String denominazione;
	@Column(nullable = false, name = "DESCRIZIONE", length = 5000)
	private String descrizione;
	@Column(name = "NOME_VALORE_A")
	private String nomeValoreA;
	@Column(name = "NOME_VALORE_B")
	private String nomeValoreB;
	@Column(nullable = false, name = "TIPO_FORMULA")
	@Enumerated(EnumType.STRING)
	private TipoFormula tipoFormula;
	@Column(name = "PERCENTUALE")
	private Boolean percentuale;
	@Column(nullable = false, name = "ID_ENTE")
	private Long idEnte = 0l;
	@Column(name = "RAGGRUPPAMENTO")
	@Enumerated(EnumType.STRING)
	private RaggruppamentoIndicatori raggruppamento = RaggruppamentoIndicatori.SPECIFICO;
	@Column(name = "CALCOLO_CONSUNTIVAZIONE")
	@Enumerated(EnumType.STRING)
	private CalcoloConsuntivazione calcoloConsuntivazione = CalcoloConsuntivazione.DEFAULT;
	@Column(name = "DECRESCENTE")
	private Boolean decrescente;
}