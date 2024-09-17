package com.finconsgroup.performplus.rest.api.vm.v2;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode()
@ToString
public class AggiornaIntroduzioneRequest implements Serializable{
	ElementoIntroduzioneVM elemento;
}
