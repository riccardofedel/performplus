package com.finconsgroup.performplus.service.dto;

import java.util.List;

import lombok.Data;

@SuppressWarnings("serial")
@Data
public class RicercaRisorseUmaneOrg extends RicercaRisorseUmane{
    private List<Long> organizzazioni;
   
}