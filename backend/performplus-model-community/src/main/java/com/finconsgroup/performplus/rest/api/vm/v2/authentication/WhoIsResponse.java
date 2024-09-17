package com.finconsgroup.performplus.rest.api.vm.v2.authentication;

import java.io.Serializable;
import java.util.List;

import com.finconsgroup.performplus.enumeration.Ruolo;
import com.finconsgroup.performplus.enumeration.UserStatus;

import lombok.Data;
import lombok.ToString;

@SuppressWarnings("serial")
@Data
@ToString
public class WhoIsResponse implements Serializable {
	private String code;
	private String description;
	private Ruolo ruolo;
	private Ruolo ruoloAggiunto;
	private UserStatus status=UserStatus.ACTIVE;
	private Long idEnte;
	private Integer anno;
	private Long idRisorsa;
	private List<Abilitazione> enablings;
	private boolean valutatore;
	private boolean valutato;
	private boolean referente;
}
