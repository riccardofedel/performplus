package com.finconsgroup.performplus.domain;

import com.finconsgroup.performplus.service.dto.EnteInterface;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@SuppressWarnings("serial")
@Entity
@Table(name = "IMAGE_ENTRY")
@Data
@EqualsAndHashCode
public class ImageEntry extends AbstractAuditingEntity implements EnteInterface{
    @Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;

    @OneToOne(optional = true)
    @JoinColumn(name="RISORSA_UMANA_ID")
    private RisorsaUmana risorsa;
    @Column(name="FILE_NAME",nullable=false)
    private String fileName;
    @Column(name="THUMB_NAME")
    private String thumbName;
    @Column(name="NOME",nullable=false,length=50)
    private String nome;
    @Column(name="CONTENT_TYPE",nullable=false)
     private String contentType;
    @Column(name="ID_ENTE",nullable=false)
    private Long idEnte=0l;
    
}