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

@SuppressWarnings("serial")
@Entity
@Table(name = "ALLEGATO_INDICATORE_PIANO")
@Data
@EqualsAndHashCode
public class AllegatoIndicatorePiano extends AbstractAuditingEntity {
	
    @Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;

	@Column(name="FILE_NAME",nullable=false)
    private String fileName;
    @Column(name="NOME",nullable=true,length=100)
    private String nome;
    @Column(name="DESCRIZIONE",nullable=true,length=4000)
    private String descrizione;
    @Column(name="CONTENT_TYPE",nullable=false)
    private String contentType;
	@ManyToOne(optional = false)
	@JoinColumn(name = "INDICATORE_PIANO_ID")
    private IndicatorePiano indicatorePiano;
     
}