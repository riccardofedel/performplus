package com.finconsgroup.performplus.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode
public class AllegatoIndicatorePianoDTO extends EntityDTO{
	private String fileName;
	private String nome;
	private String descrizione;
	private String contentType;
	private EntityDTO indicatorePiano;

	@JsonIgnore
	public String getTipo() {
		return getFormat();
	}
	
	@JsonIgnore
	public String getSimpleName() {
		if (getFileName() == null)
			return "";
		int k = getFileName().lastIndexOf("/");
		return k < 0 ? getFileName() : getFileName().substring(k + 1);
	}
	
	@JsonIgnore
	public String getFormat() {
		if (getContentType() == null)
			return "gif";
		int k = getContentType().lastIndexOf("/");
		return k < 0 ? "gif" : getContentType().substring(k + 1);
	}
}
