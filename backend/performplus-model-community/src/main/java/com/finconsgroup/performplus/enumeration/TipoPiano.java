package com.finconsgroup.performplus.enumeration;

public enum TipoPiano {
    PIANO,
    VARIAZIONE,
    RELAZIONE;
    public TipoPiano getSucc() {
	int ord = ordinal() + 1;
	for (TipoPiano l : TipoPiano.values()) {
	    if (l.ordinal() == ord)
		return l;
	}
	return null;
    }

  }
