package com.finconsgroup.performplus.service.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode

public class ImageEntryDTO extends EntityDTO implements EnteInterface {

	private RisorsaUmanaDTO risorsa;
	private String fileName;
	private String thumbName;
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
