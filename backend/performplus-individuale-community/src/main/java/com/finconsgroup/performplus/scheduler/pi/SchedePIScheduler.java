package com.finconsgroup.performplus.scheduler.pi;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.finconsgroup.performplus.rest.api.pi.vm.SchedaValutatoVM;

@Service
@EnableScheduling
@ConditionalOnProperty(value = "scheduler-agg-schede-enabled", havingValue = "true", matchIfMissing = false)
public class SchedePIScheduler {


	@Scheduled(cron = "${scheduler-agg-schede-cron}"
//			fixedRate = 300000000, initialDelay = 10
	)
	public void esegui() throws Throwable {
		System.out.println("---INIZIO aggiorna Schede PI");
		System.out.println("---FINE aggiorna Schede PI");

	}

}
