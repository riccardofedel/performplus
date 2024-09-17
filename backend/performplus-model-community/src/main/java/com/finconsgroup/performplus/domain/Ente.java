package com.finconsgroup.performplus.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "ENTE")
@Data
@EqualsAndHashCode

public class Ente extends AbstractAuditingEntity {

	private static final long serialVersionUID = -1655918506228891634L;
    @Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;

	@Column(name = "NOME", nullable = false, length = 10)
	private String nome;
	@Column(nullable = false, name = "DESCRIZIONE", length = 100)
	private String descrizione;
	@ManyToOne(optional = true)
	@JoinColumn(name = "IMAGE_ENTRY_ID")
	private ImageEntry imageEntry;


}