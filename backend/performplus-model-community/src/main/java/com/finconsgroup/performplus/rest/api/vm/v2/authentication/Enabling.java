package com.finconsgroup.performplus.rest.api.vm.v2.authentication;

import java.io.Serializable;

import lombok.Data;
import lombok.ToString;

@SuppressWarnings("serial")
@Data
@ToString
public class Enabling implements Serializable{
	private boolean read;
	private boolean write;

	public Enabling read(boolean read) {
		setRead(read);
		return this;
	}
	public Enabling write(boolean write) {
		setWrite(write);
		return this;
	}
}
