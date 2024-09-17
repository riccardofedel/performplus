package com.finconsgroup.performplus.service.business.impl;

import com.opencsv.bean.ColumnPositionMappingStrategy;

class CustomMappingStrategy<T> extends ColumnPositionMappingStrategy<T> {
 
	private static final String[] HEADER = 
			new String[] { "id_ente", "piano_codice", "codice_interno", "tipo_nodo", "codice",
			"denominazione", "inizio", "scadenza", "capitolo_bilancio", "stato_nodo_piano", "stato_proposta",
			"strategico", "note", "target_anno", "target_periodo", "target_valore", "rend_anno", "rend_periodo",
			"rend_valore", "anno", "anno_inizio", "anno_fine", "padre_codice", "piano_codice_interno",
			"cognome", "nome", "codice_fiscale", "direzione_codice", "direzione_desc", "tags", "territori",
			"terr_altro" };

    @Override
    public String[] generateHeader(T bean) {
        return HEADER;
    }
}