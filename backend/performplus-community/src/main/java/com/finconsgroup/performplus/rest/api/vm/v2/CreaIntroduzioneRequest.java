package com.finconsgroup.performplus.rest.api.vm.v2;

import jakarta.validation.constraints.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode
@ToString
public class CreaIntroduzioneRequest extends AggiornaIntroduzioneRequest{

    @NotNull
    private Long   idEnte;
    @NotNull
    private Integer anno;
}
