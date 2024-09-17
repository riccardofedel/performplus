package com.finconsgroup.performplus.enumeration;

public enum TipoFormula implements EnumWithLabel{

    TIPO_FORMULA_NUMERO("Numero"), TIPO_FORMULA_DATA("Data"), TIPO_FORMULA_RAPPORTO("Rapporto"), TIPO_FORMULA_BULEANO("Sì/No");
	private String label; 
	
	private TipoFormula(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
