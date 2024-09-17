package com.finconsgroup.performplus.service.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode
public class CruscottoDTO extends EntityDTO {
		private Date dataInizializzazioneAnno;
		private Boolean flagInizializzazioneAnno;
		
		private Date dataAttivazioneInserimentoInterventiDa;
		private Date dataAttivazioneInserimentoInterventiA;
		private Boolean flagAttivazioneInserimentoInterventi;
		
		private Date dataAttivazioneInserimentoIndicatoriDa;
		private Date dataAttivazioneInserimentoIndicatoriA;
		private Boolean flagAttivazioneInserimentoIndicatori;

		private Date dataAttivazioneInserimentoTargetDa;
		private Date dataAttivazioneInserimentoTargetA;
		private Boolean flagAttivazioneInserimentoTarget;

		private Date dataValidazionePiano;
		private Boolean flagValidazionePiano;

		private Date dataChiusuraPiano;
		private Boolean flagChiusuraPiano;

		private Date dataConsuntivazione01Da;
		private Date dataConsuntivazione01A;
		private Boolean flagConsuntivazione01;

		private Date dataConsuntivazione02Da;
		private Date dataConsuntivazione02A;
		private Boolean flagConsuntivazione02;

		private Date dataConsuntivazione03Da;
		private Date dataConsuntivazione03A;
		private Boolean flagConsuntivazione03;

		private Date dataConsuntivazione04Da;
		private Date dataConsuntivazione04A;
		private Boolean flagConsuntivazione04;

		private Date dataConsuntivazione05Da;
		private Date dataConsuntivazione05A;
		private Boolean flagConsuntivazione05;

		private Date dataConsuntivazione06Da;
		private Date dataConsuntivazione06A;
		private Boolean flagConsuntivazione06;

		private Long idEnte = 0l;
		private Integer anno;
		
		@JsonIgnore
		public CruscottoDTO idEnte(Long idEnte) {
			setIdEnte(idEnte);
			return this;
		}
		@JsonIgnore
		public CruscottoDTO anno(Integer anno) {
			setAnno(anno);
			return this;
		}
		@JsonIgnore
		public CruscottoDTO dataInizializzazioneAnno(Date dataInizializzazioneAnno) {
			setDataInizializzazioneAnno(dataInizializzazioneAnno);
			return this;
		}
		public CruscottoDTO flagInizializzazioneAnno(Boolean flagInizializzazioneAnno) {
			setFlagInizializzazioneAnno(flagInizializzazioneAnno);
			return this;
		}

}