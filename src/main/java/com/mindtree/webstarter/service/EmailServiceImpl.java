package com.mindtree.webstarter.service;

import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.mindtree.webstarter.model.Greeting;
import com.mindtree.webstarter.util.AsyncResponse;

@Service
public class EmailServiceImpl implements EmailService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public Boolean send(Greeting greeting) {
		logger.info("> send");

		Boolean success = Boolean.FALSE;

		long pause = 5000;
		try {
			Thread.sleep(pause);
		} catch (Exception e) {

		}
		logger.info("Processing time was {} seconds", pause / 1000);
		success = Boolean.TRUE;
		return success;
	}

	@Override
	@Async
	public void sendAsync(Greeting greeting) {
		logger.info("> sendAsunc");
		try {
			send(greeting);
		} catch (Exception e) {
			logger.warn("Exception caught sending asynchronous mail", e);
		}
		logger.info("< sendAsync");
	}

	@Override
	public Future<Boolean> sendAsyncWithResult(Greeting greeting) {
		logger.info("> sendAsyncWithResult");

		AsyncResponse<Boolean> response = new AsyncResponse<Boolean>();
		try {
			Boolean success = send(greeting);
			response.complete(success);
		} catch (Exception e) {
			logger.warn("Exception caught sending asynchronous mail", e);
			response.completeExceptionally(e);
		}
		logger.info("< sendAsyncWithResult");
		return response;
	}

}
