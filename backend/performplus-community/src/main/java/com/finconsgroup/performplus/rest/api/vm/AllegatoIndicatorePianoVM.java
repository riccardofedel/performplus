package com.finconsgroup.performplus.rest.api.vm;

import com.finconsgroup.performplus.rest.api.vm.EntityVM;

import lombok.Data;
import lombok.EqualsAndHashCode;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode
public class AllegatoIndicatorePianoVM extends EntityVM{
	private String fileName;
	private String nome;
	private String descrizione;
	private String contentType;
	private EntityVM indicatorePiano;


	public String getTipo() {
		return getFormat();
	}
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
