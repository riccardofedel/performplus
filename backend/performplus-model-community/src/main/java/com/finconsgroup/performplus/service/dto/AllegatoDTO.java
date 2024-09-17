package com.finconsgroup.performplus.service.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode
public class AllegatoDTO extends EntityDTO implements EnteInterface {

	private String fileName;
	private String descrizione;
	private String nome;
	private String contentType;
	private Long idEnte = 0l;

	public String getSimpleName() {
		if (getFileName() == null)
			return "";
		int k = getFileName().lastIndexOf("/");
		return k < 0 ? getFileName() : getFileName().substring(k + 1);
	}

	public String getFormat() {
		if (getContentType() == null)
			return "gif";
		int k = getContentType().lastIndexOf("/");
		return k < 0 ? "gif" : getContentType().substring(k + 1);
	}

}
