package com.finconsgroup.performplus.service.dto;

import java.time.LocalDate;

import com.finconsgroup.performplus.enumeration.Livello;

import lombok.Data;

@Data

public class ConfigDTO implements AnnoAndEnteInterface{
    private String nomeEnte;
    private String descEnte;
    private String defaultVersion;
    private String provincia;
    private int oreMax;
    private Livello livelloMax;
    private boolean riepilogoPeg;
    private LocalDate now;
    private int[] entro=new int[]{15,15,15,15};
    private int[] aPartire=new int[]{0,0,0,0};
	private Integer anno;
	private Long idEnte=0l;

    
}
