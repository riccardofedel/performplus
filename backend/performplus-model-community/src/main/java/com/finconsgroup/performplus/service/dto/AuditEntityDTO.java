package com.finconsgroup.performplus.service.dto;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.EqualsAndHashCode;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode
public class AuditEntityDTO extends EntityDTO{
	    private LocalDateTime dateInsert=LocalDateTime.now();

	    private LocalDateTime dateModify=LocalDateTime.now();

	    private String userInsert="system";

	    private String userModify="system";

}
