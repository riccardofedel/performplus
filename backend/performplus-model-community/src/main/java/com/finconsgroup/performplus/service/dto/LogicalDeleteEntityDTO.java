package com.finconsgroup.performplus.service.dto;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.EqualsAndHashCode;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode
public class LogicalDeleteEntityDTO extends AuditEntityDTO{
	    private LocalDateTime dateDelete;
	    private String userDelete;

}
