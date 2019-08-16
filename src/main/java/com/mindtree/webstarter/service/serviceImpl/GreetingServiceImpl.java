package com.mindtree.webstarter.service.serviceImpl;

import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.mindtree.webstarter.model.Greeting;
import com.mindtree.webstarter.repository.GreetingRepository;
import com.mindtree.webstarter.service.GreetingService;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class GreetingServiceImpl implements GreetingService {

	@Autowired
	private GreetingRepository greetingRepository;

	@Override
	public Collection<Greeting> findAll() {
		Collection<Greeting> greetings = greetingRepository.findAll();
		return greetings;
	}

	@Override
	@Cacheable(value = "greeting", key = "#id")
	public Greeting findOne(Long id) {
		Optional<Greeting> optionalGreeting = greetingRepository.findById(id);
		if (optionalGreeting.isPresent()) {
			return optionalGreeting.get();
		}
		return null;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	@CachePut(value = "greeting", key = "#result.id")
	public Greeting create(Greeting greeting) {
		Greeting savedGreeting = greetingRepository.save(greeting);
		//if (savedGreeting.getId() == 4L) {
			//throw new RuntimeException("Roll me back");
		//}
		return savedGreeting;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	@CachePut(value = "greeting", key = "#greeting.id")
	public Greeting update(Greeting greeting) {
		Greeting checkPresent = findOne(greeting.getId());
		if (checkPresent == null) {
			return null;
		}
		return greetingRepository.save(greeting);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	@CacheEvict(value = "greeting", key = "#id")
	public void delete(Long id) {
		greetingRepository.deleteById(id);
	}

	@Override
	@CacheEvict(value = "greeting", allEntries = true)
	public void evictCache() {

	}

}
