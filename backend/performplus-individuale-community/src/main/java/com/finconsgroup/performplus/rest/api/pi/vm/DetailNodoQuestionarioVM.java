package com.finconsgroup.performplus.rest.api.pi.vm;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode
@ToString
@NoArgsConstructor
public class DetailNodoQuestionarioVM extends NodoQuestionarioVM{

	private List<NodoQuestionarioVM> figli;

	public DetailNodoQuestionarioVM(Long id, Long idQuestionario, String codice, String intestazione,
			String descrizione, boolean foglia, Long idPadre, String codiceCompleto, BigDecimal peso,
			TipoNodoQuestionario tipoPadre, String nomeQuestionario) {
		super(id, idQuestionario, codice, intestazione, descrizione, foglia, idPadre, codiceCompleto, peso, tipoPadre,
				nomeQuestionario);
	}
	public DetailNodoQuestionarioVM(Long id, Long idQuestionario, String codice, String intestazione,
			String descrizione, Long idPadre, String codiceCompletoAmbito, BigDecimal peso, String nomeQuestionario) {
		super(id, idQuestionario, codice, intestazione, descrizione, idPadre, codiceCompletoAmbito, peso, nomeQuestionario);
	}
	public DetailNodoQuestionarioVM(Long id, String intestazione, String descrizione) {
		super(id, intestazione, descrizione);
	}
	
	
	public DetailNodoQuestionarioVM nomeQuestinario(String nomeQuestionario) {
		setNomeQuestionario(nomeQuestionario);
		return this;
		
	}
	public DetailNodoQuestionarioVM tipo(TipoNodoQuestionario tipo) {
		setTipo(tipo);
		return this;
		
	}
	public DetailNodoQuestionarioVM Livello(Integer Livello) {
		setLivello(Livello);
		return this;
		
	}
	public DetailNodoQuestionarioVM id(Long id) {
		setId(id);
		return this;
		
	}
	public DetailNodoQuestionarioVM intestazione(String intestazione) {
		setIntestazione(intestazione);
		return this;
		
	}

	public DetailNodoQuestionarioVM figli(List<NodoQuestionarioVM> figli) {
		this.figli=figli;
		return this;
		
	}
}

