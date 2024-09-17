package com.finconsgroup.performplus.domain;

import com.finconsgroup.performplus.service.dto.AnnoInterface;
import com.finconsgroup.performplus.service.dto.EnteInterface;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@SuppressWarnings("serial")
@Entity
@Table(name = "CONTESTO")
@Data
@EqualsAndHashCode

public class Contesto extends AbstractAuditingEntity implements EnteInterface, AnnoInterface {
	
    @Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "PIANO_ID")
	private NodoPiano piano;
	@Column(name = "RELAZIONE")
	private boolean relazione;
	@Column(name = "ID_ENTE")
	private Long idEnte = 0l;
	@Column(name = "ANNO")
	private Integer anno;


	public Contesto piano(NodoPiano piano) {
		setPiano(piano);
		return this;
	}
	public Contesto anno(Integer anno) {
		setAnno(anno);
		return this;
	}
	public Contesto idEnte(Long idEnte) {
		setIdEnte(idEnte);
		return this;
	}
}