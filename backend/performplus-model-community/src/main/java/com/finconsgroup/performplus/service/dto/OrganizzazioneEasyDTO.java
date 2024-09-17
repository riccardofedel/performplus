package com.finconsgroup.performplus.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.finconsgroup.performplus.enumeration.Livello;

import lombok.Data;
import lombok.EqualsAndHashCode;



	@SuppressWarnings("serial")
	@Data
	@EqualsAndHashCode

	public class OrganizzazioneEasyDTO extends EntityDTO
			implements Comparable<OrganizzazioneEasyDTO>, EnteInterface, AnnoInterface {

		private Long idEnte = 0l;
		private int ordine;
		private String codice;
		private String codiceCompleto;
		private String intestazione;
		private Livello livello;
		private String codiceInterno;
		private Integer anno;
		private OrganizzazioneEasyDTO padre;
		private RisorsaUmanaDTO responsabile;

		public OrganizzazioneEasyDTO() {

		}

		public OrganizzazioneEasyDTO(Long idEnte, Integer anno) {
			this(idEnte, anno, null);
		}

		public OrganizzazioneEasyDTO(Long idEnte, Integer anno, Livello livello) {
			super();
			this.idEnte = idEnte;
			this.anno = anno;
			this.livello = livello;
		}
		@Override
		public int compareTo(OrganizzazioneEasyDTO o) {
			if (o == null)
				return -1;
			String a = getCodiceCompleto();
			String b = o.getCodiceCompleto();
			int comp = (a == null ? "" : a).compareTo(b == null ? "" : b);
			if (comp == 0)
				return getOrdine() - o.getOrdine();
			else
				return comp;
		}

		@JsonIgnore
		public String getCodiceRidotto() {
			String code = getCodiceCompleto();
			int k = code.indexOf('.');
			if (k >= 0) {
				code = code.substring(k + 1);
			} else {
				code = "";
			}
			return code;
		}

		@JsonIgnore
		public String getNomeCompletoDaLivello() {
			StringBuilder sb = new StringBuilder();
			sb.append(getLivello().name()).append(" ").append(getCodiceRidotto());
			if (getIntestazione() != null)
				sb.append(" ").append(getIntestazione());
			return sb.toString();
		}


	}

