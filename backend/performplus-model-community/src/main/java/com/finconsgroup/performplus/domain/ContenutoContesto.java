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
@Table(name = "CONTENUTO_CONTESTO")
@Data
@EqualsAndHashCode

public class ContenutoContesto extends AbstractAuditingEntity  {


	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected Long id;

	@Column(name = "NOME", nullable = false)
	private String nome;
	@Column(name = "GRUPPO")
	private String gruppo;
	
	@Column(name = "CONTENUTO", nullable = false)
	private String contenuto;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "PIANO_ID")
	private NodoPiano piano;

	public ContenutoContesto() {
	}

	public ContenutoContesto(String nome, String gruppo, String contenuto) {
		super();
		this.nome = nome;
		this.gruppo = gruppo;
		this.contenuto = contenuto;
	}

}