package com.finconsgroup.performplus.rest.api.pi.vm;

import java.time.LocalDate;
import java.util.List;

import com.finconsgroup.performplus.enumeration.TipoNodo;
import com.finconsgroup.performplus.enumeration.TipoRegolamento;
import com.finconsgroup.performplus.rest.api.vm.EntityVM;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode
@ToString
@NoArgsConstructor
public class PesoNodoPianoRegistrazioneVM extends EntityVM {
	private Long idNodo;
	private Long idRegistrazione;
	private String denominazioneNodo;
	private Long idStruttura;
	private String denominazioneStruttura;
	private Long idResponsabileStruttura;
	private String responsabileStruttura;
	private TipoNodo tipoNodo;
	private Float peso = 0f;
	private Float sommaPesi = 0f;
	private LocalDate inizio;
	private LocalDate fine;
	private TipoRegolamento tipoRegolamento;
	List<PesoIndicatoreRegistrazioneVM> indicatori;

	public PesoNodoPianoRegistrazioneVM(Long idRegistrazione, Long idNodo, String denominazioneNodo, Long idStruttura,
			String denominazioneStruttura, Long idResponsabileStruttura, String responsabileStruttura,
			TipoNodo tipoNodo, LocalDate inizio, LocalDate fine) {
		super();
		this.idRegistrazione = idRegistrazione;
		this.idNodo = idNodo;
		this.denominazioneNodo = denominazioneNodo;
		this.idStruttura = idStruttura;
		this.denominazioneStruttura = denominazioneStruttura;
		this.idResponsabileStruttura = idResponsabileStruttura;
		this.responsabileStruttura = responsabileStruttura;
		this.tipoNodo = tipoNodo;
		this.inizio = inizio;
		this.fine = fine;
	}

}
