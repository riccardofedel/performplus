package com.finconsgroup.performplus.domain;

import com.finconsgroup.performplus.enumeration.TipoConsuntivazione;
import com.finconsgroup.performplus.enumeration.Livello;
import com.finconsgroup.performplus.service.dto.AnnoInterface;
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
@Table(name = "CONFIGURAZIONE")
@Data
@EqualsAndHashCode
public class Configurazione extends AbstractAuditingEntity implements EnteInterface, AnnoInterface{

	private static final long serialVersionUID = -2954612542185526735L;
	
    @Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;

	@Column(name="ID_ENTE", nullable = false)
	private Long idEnte=0l;
	@Column(name = "ANNO", nullable = false)
	private Integer anno=2023;
	@Column(name = "MAX_PERC_PESO_OPERATIVI")
	private Double maxPercentualePesoOperativi=75d;
	@Column(name = "TIPO_CONSUNTIVAZIONE")
	private TipoConsuntivazione tipoConsuntivazione=TipoConsuntivazione.SEMESTRE;
	@Column(name = "MAX_Livello")
	@Enumerated(EnumType.STRING)
	private Livello maxLivello=Livello.INFERIORE;
	@Column(name = "MAX_ORE")
	private Integer maxOre=8;
	@Column(name = "ABILITA_OBIETTIVO_GESTIONALE")
	private Boolean abilitaObiettivoGestionale;

}