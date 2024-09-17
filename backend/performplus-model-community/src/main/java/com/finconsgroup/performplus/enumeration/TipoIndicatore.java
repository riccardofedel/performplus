package com.finconsgroup.performplus.enumeration;

public enum TipoIndicatore implements EnumWithLabel{
   OUTCOME("OUTCOME"),OUTPUT("OUTPUT");
	private String label; 
	
	private TipoIndicatore(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
