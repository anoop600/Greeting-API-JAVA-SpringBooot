package com.mindtree.webstarter.service;

import java.util.concurrent.Future;

import com.mindtree.webstarter.model.Greeting;

public interface EmailService {
	Boolean send(Greeting greeting);

	void sendAsync(Greeting greeting);

	Future<Boolean> sendAsyncWithResult(Greeting greeting);
}
