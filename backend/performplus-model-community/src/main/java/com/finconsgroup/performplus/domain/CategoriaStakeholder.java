package com.finconsgroup.performplus.domain;

import com.finconsgroup.performplus.service.dto.EnteInterface;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "CATEGORIA_STAKEHOLDER",uniqueConstraints = { @UniqueConstraint(columnNames = {
		"ID_ENTE","CODICE" }) })
@Data
@EqualsAndHashCode
public class CategoriaStakeholder extends AbstractAuditingEntity implements EnteInterface{
	
 	private static final long serialVersionUID = -8656131931170121503L;
 	
    @Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;

 	 @Column(name="CODICE",nullable = false,length=10)
	private String codice;
 	@Column(nullable = false,name="DESCRIZIONE",length=100)
    private String descrizione;
    @ManyToOne(optional=true)
    @JoinColumn(name="IMAGE_ENTRY_ID")
    private ImageEntry imageEntry;
    @Column(nullable = false, name = "ID_ENTE")
    private Long idEnte=0l;


}