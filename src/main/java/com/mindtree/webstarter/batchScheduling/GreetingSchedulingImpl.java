package com.mindtree.webstarter.batchScheduling;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.mindtree.webstarter.model.Greeting;
import com.mindtree.webstarter.service.GreetingService;

@Component
@Profile("batch")
public class GreetingSchedulingImpl {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private GreetingService greetingService;

	@Scheduled(cron = "${batch.greeting.cron}")
	public void cronJob() {
		logger.info("> cronJob");
		Collection<Greeting> greetings = greetingService.findAll();
		logger.info("There are {} greetings in the data store", greetings.size());
		logger.info("< cronJob");
	}

	@Scheduled(initialDelayString = "${batch.greeting.initialDelay}", fixedRateString = "${batch.greeting.fixedRate}")
	public void fixedRateJobWithInitialDelay() {
		logger.info("> fixedRateJobWithInitialDelay");

		long pause = 5000;
		long start = System.currentTimeMillis();
		do {
			if ((start + pause) < System.currentTimeMillis()) {
				break;
			}
		} while (true);
		logger.info("Processing time was {} seconds.", pause / 1000);
		logger.info("< fixedRateJobWithInitialDelay");
	}

	@Scheduled(initialDelayString = "${batch.greeting.initialDelay}", fixedDelayString = "${batch.greeting.fixedDelay}")
	public void fixedDelayJobWithInitialDelay() {
		logger.info("> fixedDelayJobWithInitialDelay");

		long pause = 5000;
		long start = System.currentTimeMillis();
		do {
			if (start + pause < System.currentTimeMillis()) {
				break;
			}
		} while (true);
		logger.info("Processing time was {} seconds.", pause / 1000);
		logger.info("< fixedDelayJobWithInitialDelay");
	}
}
