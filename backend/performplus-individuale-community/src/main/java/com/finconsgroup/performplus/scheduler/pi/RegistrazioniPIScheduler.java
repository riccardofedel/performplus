package com.finconsgroup.performplus.scheduler.pi;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.finconsgroup.performplus.domain.Cruscotto;
@Component
@EnableScheduling
@ConditionalOnProperty(value = "scheduler-agg-reg-enabled", havingValue = "true", matchIfMissing = false)
public class RegistrazioniPIScheduler {

	@Scheduled(
			cron = "${scheduler-agg-reg-cron}")
//			fixedRate = 1000 * 60 * 60 * 24, initialDelay = 10)

	public void esegui() throws Throwable {
		System.out.println("---FINE AggiornaRegistrazioneiPI");
	}


}
