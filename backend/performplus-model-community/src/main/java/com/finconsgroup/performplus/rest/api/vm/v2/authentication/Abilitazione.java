package com.finconsgroup.performplus.rest.api.vm.v2.authentication;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode
@ToString
public class Abilitazione extends Enabling{
	private String name;
	public Abilitazione() {
		super();
	}
	public Abilitazione(String name) {
		super();
		this.name=name;
	}
	@Override
	public Abilitazione read(boolean read) {
		setRead(read);
		return this;
	}
	@Override
	public Abilitazione write(boolean write) {
		setWrite(write);
		return this;
	}
}
