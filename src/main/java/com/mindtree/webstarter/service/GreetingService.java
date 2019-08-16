package com.mindtree.webstarter.service;

import java.util.Collection;

import com.mindtree.webstarter.model.Greeting;

public interface GreetingService {
	Collection<Greeting> findAll();

	Greeting findOne(Long id);

	Greeting create(Greeting greeting);

	Greeting update(Greeting greeting);

	void delete(Long id);

	void evictCache();
}
