package com.finconsgroup.performplus.domain;

import com.finconsgroup.performplus.service.dto.EnteInterface;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@SuppressWarnings("serial")
@Entity
@Table(name = "UTENTE")
@Data
@EqualsAndHashCode

public class Utente extends AbstractAuditingEntity implements EnteInterface {
    @Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "UTENTE_STORE")
	@SequenceGenerator(name = "UTENTE_STORE", sequenceName = "UTENTE_SEQ"
			, allocationSize = 1)

    protected Long id;

	@Column(name = "USERID", nullable = false)
	private String userid;
	@Column(nullable = false, name = "NOME")
	private String nome;
	@Column(nullable = false, length = 200, name = "PASSWD")
	private String passwd;
	@Column(name = "ADMIN", nullable = false)
	private boolean admin;
	@Column(nullable = false, name = "ID_ENTE")
	private Long idEnte = 0l;
	@Column(name = "SUPER_ADMIN")
	private Boolean superAdmin;
	@Column(name = "CODICE_INTERNO")
	private String codiceInterno;

}