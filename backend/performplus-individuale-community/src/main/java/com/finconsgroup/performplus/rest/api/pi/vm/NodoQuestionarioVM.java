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
public class NodoQuestionarioVM extends QuestionarioVM{
	private TipoNodoQuestionario tipo;
	private Long idQuestionario;
	private String codice;
	private Boolean foglia=false;
	private Long idPadre;
	private TipoNodoQuestionario tipoPadre;
	private String codiceCompleto;
	private BigDecimal peso;
	private Integer Livello;
	private String nomeQuestionario;
	private BigDecimal pesoMancataAssegnazione;
	private BigDecimal pesoMancatoColloquio;
	private Boolean flagSoloAdmin=false;

	
	public NodoQuestionarioVM(Long id,String intestazione,String descrizione) {
		setId(id);
		this.tipo=TipoNodoQuestionario.questionario;
		this.idQuestionario=id;
		this.codice="";
		setIntestazione(intestazione);
		setDescrizione(descrizione);
		this.codiceCompleto="";
		this.Livello=1;
		this.nomeQuestionario=intestazione;
	}

	public NodoQuestionarioVM(Long id,Long idQuestionario, String codice, String intestazione, String descrizione,
			boolean foglia, Long idPadre, String codiceCompleto, BigDecimal peso, TipoNodoQuestionario tipoPadre,
			String nomeQuestionario) {
		setId(id);
		this.tipo=TipoNodoQuestionario.ambito;
		this.idQuestionario = idQuestionario;
		this.codice = codice;
		setIntestazione(intestazione);
		setDescrizione(descrizione);
		this.foglia = foglia;
		this.idPadre = idPadre;
		this.codiceCompleto = codiceCompleto;
		this.tipoPadre=tipoPadre;
		this.peso = peso;
		this.Livello=StringUtils.countMatches(this.codiceCompleto, ".")+2;
		this.nomeQuestionario=nomeQuestionario;
	}
	public NodoQuestionarioVM(Long id, Long idQuestionario, String codice, String intestazione, String descrizione, 
			Long idPadre, String codiceCompletoAmbito, BigDecimal peso, String nomeQuestionario) {
		setId(id);
		this.tipo=TipoNodoQuestionario.valore;
		this.idQuestionario = idQuestionario;
		this.codice = codice;
		setIntestazione(intestazione);
		setDescrizione(descrizione);
		this.idPadre = idPadre;
		this.codiceCompleto = codiceCompletoAmbito+"."+codice;
		this.tipoPadre=TipoNodoQuestionario.ambito;
		this.peso = peso;
		this.Livello=StringUtils.countMatches(this.codiceCompleto, ".")+2;
		this.nomeQuestionario=nomeQuestionario;
	}
	public NodoQuestionarioVM nomeQuestinario(String nomeQuestionario) {
		this.nomeQuestionario=nomeQuestionario;
		return this;
		
	}
	public NodoQuestionarioVM tipo(TipoNodoQuestionario tipo) {
		this.tipo=tipo;
		return this;
		
	}
	public NodoQuestionarioVM Livello(Integer Livello) {
		this.Livello=Livello;
		return this;
		
	}
	public NodoQuestionarioVM id(Long id) {
		setId(id);
		return this;
		
	}
	public NodoQuestionarioVM intestazione(String intestazione) {
		setIntestazione(intestazione);
		return this;
		
	}

	public NodoQuestionarioVM pesoMancataAssegnazione(BigDecimal pesoMancataAssegnazione) {
		setPesoMancataAssegnazione(pesoMancataAssegnazione);
		return this;
	}

	public NodoQuestionarioVM pesoMancatoColloquio(BigDecimal pesoMancatoColloquio) {
		setPesoMancatoColloquio(pesoMancatoColloquio);
		return this;
	}

	public NodoQuestionarioVM flagSoloAdmin(Boolean flagSoloAdmin) {
		setFlagSoloAdmin(flagSoloAdmin);
		return this;
	}
}

