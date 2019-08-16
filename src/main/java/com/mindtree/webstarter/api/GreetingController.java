package com.mindtree.webstarter.api;

import java.util.Collection;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mindtree.webstarter.model.Greeting;
import com.mindtree.webstarter.service.EmailService;
import com.mindtree.webstarter.service.GreetingService;

@RestController
@RequestMapping(value = "/api")
public class GreetingController {
	@Autowired
	private GreetingService greetingService;

	@Autowired
	private EmailService emailService;
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@GetMapping(value = "/greeting", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Collection<Greeting>> getGreetings() {
		logger.info("> Get Greetings");
		Collection<Greeting> greetings = greetingService.findAll();
		logger.info("< Get Greetings");
		return new ResponseEntity<Collection<Greeting>>(greetings, HttpStatus.OK);
	}

	@GetMapping(value = "/greeting/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Greeting> getGreeting(@PathVariable("id") Long id) {
		logger.info("> Get Greeting");
		Greeting greeting = greetingService.findOne(id);
		if (greeting == null) {
			return new ResponseEntity<Greeting>(HttpStatus.NOT_FOUND);
		}
		logger.info("< Get Greeting");
		return new ResponseEntity<Greeting>(greeting, HttpStatus.OK);
	}

	@PostMapping(value = "/greeting", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Greeting> createGreeting(@RequestBody Greeting greeting) {
		logger.info("> Create Greeting");
		Greeting savedGreeting = greetingService.create(greeting);
		logger.info("< Create Greeting");
		return new ResponseEntity<Greeting>(savedGreeting, HttpStatus.CREATED);
	}

	@PutMapping(value = "/greeting", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Greeting> updateGreeting(@RequestBody Greeting greeting) {
		logger.info("> Update Greeting");
		Greeting updatesGreeting = greetingService.update(greeting);
		logger.info("< Update Greeting");
		if (updatesGreeting == null) {
			return new ResponseEntity<Greeting>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<Greeting>(updatesGreeting, HttpStatus.OK);

	}

	@DeleteMapping(value = "/greeting/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Greeting> deleteGreeting(@PathVariable("id") Long id, @RequestBody Greeting greeting) {
		logger.info("> Delete Greeting");
		greetingService.delete(id);
		logger.info("< Delete Greeting");
		return new ResponseEntity<Greeting>(HttpStatus.NO_CONTENT);
	}

	@PostMapping(value = "/greeting/{id}/send", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Greeting> sendGreeting(@PathVariable("id") Long id,
			@RequestParam(value = "wait", defaultValue = "false") boolean waitForAsyncResult) {
		logger.info("> SendingGreeting");
		Greeting greeting = null;
		try {
			greeting = greetingService.findOne(id);
			if (greeting == null) {
				return new ResponseEntity<Greeting>(HttpStatus.OK);
			}

			if (waitForAsyncResult) {
				Future<Boolean> asyncResponse = emailService.sendAsyncWithResult(greeting);
				boolean emailSent = asyncResponse.get();
				logger.info("- greeting email sent ? {}", emailSent);
			} else {
				emailService.sendAsync(greeting);
			}
		} catch (Exception e) {
			logger.error("A problem occured sending the greeting. ", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		logger.info("< SendingGreeting");
		return new ResponseEntity<Greeting>(greeting, HttpStatus.OK);

	}
}
