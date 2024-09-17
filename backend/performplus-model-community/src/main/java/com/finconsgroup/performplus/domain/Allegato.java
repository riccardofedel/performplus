package com.finconsgroup.performplus.domain;

import com.finconsgroup.performplus.service.dto.EnteInterface;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "ALLEGATO")
@Data
@EqualsAndHashCode
public class Allegato extends AbstractAuditingEntity implements EnteInterface {

	private static final long serialVersionUID = -3652416408535519139L;

    @Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;

	@Column(name="FILE_NAME",nullable=false)
    private String fileName;
    @Column(name="NOME",nullable=false,length=100)
    private String nome;
    @Column(name="DESCRIZIONE",nullable=false,length=4000)
    private String descrizione;
    @Column(name="CONTENT_TYPE",nullable=false)
    private String contentType;
    @Column(name="ID_ENTE",nullable=false)
    private Long idEnte=0l;
}