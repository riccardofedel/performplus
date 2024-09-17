package com.finconsgroup.performplus.rest.api.vm;

import java.io.Serializable;
import java.util.List;

import com.finconsgroup.performplus.enumeration.Periodo;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@SuppressWarnings("serial")
@Data
public class RelazioneIndicatoreRequest implements Serializable{
	private Long  idEnte=0l;
	@NotNull
	private Integer anno;
	private  Periodo periodo;
	private  List<Long> selezionati;
}
