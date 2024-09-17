package com.finconsgroup.performplus.domain;

import com.finconsgroup.performplus.service.dto.EnteInterface;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "CATEGORIA_RISORSA_UMANA",uniqueConstraints = { @UniqueConstraint(columnNames = {
		"ID_ENTE","CODICE" }) })
@Data
@EqualsAndHashCode
public class CategoriaRisorsaUmana extends AbstractAuditingEntity implements EnteInterface {

	private static final long serialVersionUID = -2959555207384808572L;

    @Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;

	@Column(name = "CODICE", nullable = false, length = 15)
	private String codice;
	@Column(name = "DESCRIZIONE", nullable = false)
	private String descrizione;
	@Column(nullable = false, name = "ID_ENTE")
	private Long idEnte = 0l;

}