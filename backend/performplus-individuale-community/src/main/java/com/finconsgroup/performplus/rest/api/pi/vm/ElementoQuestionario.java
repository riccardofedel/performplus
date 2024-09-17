package com.finconsgroup.performplus.rest.api.pi.vm;

import java.io.Serializable;
import java.util.List;

import com.finconsgroup.performplus.domain.AmbitoValutazione;
import com.finconsgroup.performplus.domain.ValoreAmbito;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode()
@ToString
@NoArgsConstructor
public class ElementoQuestionario implements Serializable{
	@NotNull
	private Long id;
	@NotBlank
	private String descrizione;
	private boolean risposta;
	private Float peso;
	private boolean selected;
	private List<ElementoQuestionario> figli;
	private Long gruppo;
	private boolean foglia=false;
	private String codice;
	public ElementoQuestionario(AmbitoValutazione a) {
		this.id=a.getId();
		this.risposta=false;
		this.selected=false;
		this.peso=a.getPeso()==null?0f:a.getPeso().floatValue();
		this.descrizione=a.getIntestazione();
		this.codice=a.getCodice();
		this.foglia=a.isFoglia();
		if(a.isFoglia())
			this.gruppo=this.id;
	}
	public ElementoQuestionario(ValoreAmbito d) {
		this.id=d.getId();
		this.risposta=true;
		this.selected=false;
		this.peso=d.getPeso()==null?0f:d.getPeso().floatValue();
		this.descrizione=d.getIntestazione();
		this.codice=d.getCodice();
	}
}
