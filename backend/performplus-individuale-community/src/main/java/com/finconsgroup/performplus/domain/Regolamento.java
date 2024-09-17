package com.finconsgroup.performplus.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import com.finconsgroup.performplus.domain.AbstractAuditingEntity;
import com.finconsgroup.performplus.domain.CategoriaRisorsaUmana;
import com.finconsgroup.performplus.domain.Incarico;
import com.finconsgroup.performplus.service.dto.AnnoInterface;
import com.finconsgroup.performplus.service.dto.EnteInterface;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Data;

@SuppressWarnings("serial")
@Entity
@Table(name = "REGOLAMENTO")
@Data

public class Regolamento extends AbstractAuditingEntity implements EnteInterface, AnnoInterface {

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "REGOLAMENTO_STORE")
	@SequenceGenerator(name = "REGOLAMENTO_STORE", sequenceName = "REGOLAMENTO_SEQ"
			, allocationSize = 1)

	protected Long id;
	@Column(nullable = false, name = "ID_ENTE")
	private Long idEnte = 0l;
	@Column(name="ANNO", nullable = false)
	private Integer anno;
	@Column(name="PESO_OBIETTIVI_INDIVIDUALI")
	private BigDecimal pesoObiettiviIndividuali=BigDecimal.ZERO;
	@Column(name="PESO_OBIETTIVI_DI_STRUTTURA")
	private BigDecimal pesoObiettiviDiStruttura=BigDecimal.ZERO;
	@Column(name="PESO_OBIETTIVI_COLLETTIVI")
	private BigDecimal pesoObiettiviDiPerformance;
	@Column(name="PESO_COMPORTAMENTI_ORG")
	private BigDecimal pesoComportamentiOrganizzativi=BigDecimal.ZERO;
    @Column(name = "INTESTAZIONE", nullable = false)
    private String intestazione;
    @Column(name="PO")
	private Boolean po=false;
	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(name = "REGOLAMENTO_CATEGORIE")
	private List<CategoriaRisorsaUmana> categorie;
	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(name = "REGOLAMENTO_INCARICHI")
    private List<Incarico> incarichi;
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Regolamento other = (Regolamento) obj;
		return Objects.equals(id, other.id);
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(id);
		return result;
	}
	
	
}
