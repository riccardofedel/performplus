package com.finconsgroup.performplus.domain;

import com.finconsgroup.performplus.enumeration.Ruolo;
import com.finconsgroup.performplus.service.dto.AnnoInterface;
import com.finconsgroup.performplus.service.dto.EnteInterface;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "PROFILO")
@Data
@EqualsAndHashCode
public class Profilo extends AbstractAuditingEntity implements EnteInterface, AnnoInterface {

	private static final long serialVersionUID = -5802258330333133825L;
    @Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "PROFILO_STORE")
	@SequenceGenerator(name = "PROFILO_STORE", sequenceName = "PROFILO_SEQ"
			, allocationSize = 1)

    protected Long id;

	@ManyToOne
	@JoinColumn(name = "ORGANIZZAZIONE_ID")
	private Organizzazione organizzazione;
	@Column(nullable = false, name = "RUOLO")
	@Enumerated(EnumType.STRING)
	private Ruolo ruolo;
	@ManyToOne(optional = false)
	@JoinColumn(name = "UTENTE_ID")
	private Utente utente;
	@Column(name = "id_ente")
	private Long idEnte = 0l;
	@Column(name = "anno")
	private Integer anno;

	@ManyToOne(optional = true)
	@JoinColumn(name = "RISORSA_UMANA_ID")
	private RisorsaUmana risorsaUmana;

	@Column(name = "AGGIUNTO")
	private Boolean aggiunto=false;
}