package com.finconsgroup.performplus.rest.api.pi.vm;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode
@ToString
public class AmbitoValutazioneDetailVM extends AmbitoValutazioneVM{
    List<NodoQuestionarioVM> figli;
}
