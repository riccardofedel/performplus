package com.finconsgroup.performplus.rest.api.vm.v2;

import java.io.Serializable;
import java.util.List;

import com.finconsgroup.performplus.rest.api.vm.v2.authentication.Enabling;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.ToString;

@SuppressWarnings("serial")
@Data
@ToString
public class NodoAlberoVM implements Serializable{
	
	@NotBlank
	private Long id;

	@NotBlank
	private String item;
	
	@NotNull
	private Integer level = 1;
	
	@NotNull
	private Integer tipoNodo = 0;

	private Integer anno;
	private Integer annoInizio;
	private Integer annoFine;
	
	private Boolean  expandable = false;
    private Boolean isLoading = false;
    private Enabling enabling;
    private int ordine=0;
    private String statoNodo;
 	private List<NodoAlberoVM> children;
 	private Boolean  expanded = false;
 	private Boolean foglia=false;

	@NotBlank
	private String codiceCompleto;

	public NodoAlberoVM() {}
	
	public NodoAlberoVM(Long id,String item,  Integer level) {
		this(id,item, level, null, null);
	}

	public NodoAlberoVM(Long id, String item, Integer level, Boolean expandable,
			Boolean isLoading) {
		super();
		this.id=id;
		this.item = item;
		this.level = level;
		this.expandable = expandable;
		this.isLoading = isLoading;
	}
	public NodoAlberoVM enabling(Enabling enabilng) {
		setEnabling(enabilng);
		return this;
	}
	public NodoAlberoVM ordine(int ordine) {
		setOrdine(ordine);
		return this;
	}
	public NodoAlberoVM statoNodo(String statoNodo) {
		setStatoNodo(statoNodo);
		return this;
	}

	public NodoAlberoVM codiceCompleto(String codiceCompleto) {
		this.codiceCompleto=codiceCompleto;
		return this;
	}

}
