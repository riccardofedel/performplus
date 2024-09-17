package com.finconsgroup.performplus.enumeration;

public enum StatoPiano {
    ATTIVO,
    ARCHIVIO, 
    ANNULLATO,
    INIZIALIZZAZIONE;
    public StatoPiano getSucc() {
	int ord = ordinal() + 1;
	for (StatoPiano l : StatoPiano.values()) {
	    if (l.ordinal() == ord)
	    	return l;
	}
	return null;
    }

}
