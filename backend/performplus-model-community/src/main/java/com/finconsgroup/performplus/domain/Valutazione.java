package com.finconsgroup.performplus.domain;

import java.time.LocalDate;

import com.finconsgroup.performplus.enumeration.TipoValutazione;

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
@Table(name = "VALUTAZIONE")
@Data
@EqualsAndHashCode
public  class Valutazione extends AbstractValoreEntity {

	private static final long serialVersionUID = 695690211758877749L;
    @Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "VALUTAZIONE_STORE")
	@SequenceGenerator(name = "VALUTAZIONE_STORE", sequenceName = "VALUTAZIONE_SEQ"
			, allocationSize = 1)

    protected Long id;

	@ManyToOne(optional = false)
	@JoinColumn(name = "INDICATORE_PIANO_ID")
	private IndicatorePiano indicatorePiano;
	@Column(name = "TIPO_VALUTAZIONE", nullable = false)
	@Enumerated(EnumType.STRING)
	private TipoValutazione tipoValutazione;
	@Column(name = "DATA_RILEVAZIONE")
	private LocalDate dataRilevazione;
	@Column(name = "NOTE", length = 5000)
	private String note;


}
