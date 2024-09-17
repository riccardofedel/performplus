package com.finconsgroup.performplus.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@EnableScheduling
@Component
public class AggiungiDatiScheduler {

	@Autowired
	private AggiungiDati aggiungiDati;

	@Scheduled(fixedRate = 300000000, initialDelay = 10)
	public void esegui() throws Throwable {
		try {
			long nro=aggiungiDati.eseguiSistema();
		} catch (Throwable e) {
			e.printStackTrace();
			System.err.println(e.getMessage());
		}
		System.out.println("---FINE AGGIUNGI DATI");

	}

}
