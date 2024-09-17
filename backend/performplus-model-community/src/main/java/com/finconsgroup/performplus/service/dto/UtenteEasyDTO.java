package com.finconsgroup.performplus.service.dto;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode

public class UtenteEasyDTO extends EntityDTO implements EnteInterface {

	private static final long serialVersionUID = 1L;

	private String username;
	private String nome;
	private String passwd;
	private boolean admin;
//	private Boolean superAdmin;
	private RisorsaUmanaDTO risorsaUmana;
	private String risorsaUmanaString;
	private Long idEnte = 0l;
	private String codiceFiscale;


	public UtenteEasyDTO passwd(String passwd) {
		setPasswd(passwd);
		return this;
	}

}