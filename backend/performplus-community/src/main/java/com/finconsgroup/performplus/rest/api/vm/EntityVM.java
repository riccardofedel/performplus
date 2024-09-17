package com.finconsgroup.performplus.rest.api.vm;

import java.io.Serializable;

import com.finconsgroup.performplus.rest.api.vm.EntityVM;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class EntityVM implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -515133065895566060L;
	
	private Long id;
	public EntityVM() {}
	public EntityVM(Long id) {
		super();
		this.id = id;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EntityVM other = (EntityVM) obj;
		if (id == null) {
			if (other.getId() != null)
				return false;
		} else if (!id.equals(other.getId()))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}


}
