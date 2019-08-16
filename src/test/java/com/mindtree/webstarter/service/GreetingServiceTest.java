package com.mindtree.webstarter.service;

import java.util.Collection;

import javax.persistence.EntityExistsException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.mindtree.webstarter.WebStarter1ApplicationTests;
import com.mindtree.webstarter.model.Greeting;

@Transactional
public class GreetingServiceTest extends WebStarter1ApplicationTests {

	@Autowired
	private GreetingService service;

	@Before
	public void setup() {
		service.evictCache();
	}

	@After
	public void tearDown() {

	}

	@Test
	public void testFindAll() {
		Collection<Greeting> list = service.findAll();
		Assert.assertNotNull("failure = expected not null", list);
		Assert.assertEquals("failure - expected size", 2, list.size());
	}

	@Test
	public void testFindOne() {
		Long id = Long.valueOf(1);
		Greeting entity = service.findOne(id);
		Assert.assertNotNull("failure - expected not null", entity);
		Assert.assertEquals("failure - expected id attribute match", id, entity.getId());
	}

	@Test
	public void testCreate() {
		Greeting entity = new Greeting();
		entity.setText("test");
		Greeting createdEntity = service.create(entity);
		System.out.println(createdEntity);
		Assert.assertNotNull("failure - expected not null", createdEntity);
		Assert.assertNotNull("failure - expected id attribute not null", createdEntity.getId());
		Assert.assertEquals("failure - expected text attrubute match", "test", createdEntity.getText());
		Collection<Greeting> list = service.findAll();
		Assert.assertEquals("failure - expected size", 3, list.size());
	}

	@Test
	public void testCreateWithId() {
		Exception e = null;
		Greeting entity = new Greeting();
		entity.setId(Long.MAX_VALUE);
		entity.setText("test");

		try {
			service.create(entity);
		} catch (EntityExistsException exception) {
			e = exception;
		}
		Assert.assertNull("failure - expected exception", e);
		Assert.assertFalse("failure - expected EntityExistsException", e instanceof EntityExistsException);
	}

	@Test
	public void testFindOneNotFound() {
		Long id = Long.MAX_VALUE;
		Greeting entity = service.findOne(id);
		Assert.assertNull("failure - expected null", entity);

	}

	@Test
	public void testUpdate() {
		Long id = Long.valueOf(1);
		Greeting entity = service.findOne(id);
		Assert.assertNotNull("failure - expected not null", entity);
		String updatedText = entity.getText() + " test";
		entity.setText(updatedText);
		Greeting updatedEntity = service.update(entity);
		Assert.assertNotNull("failure - expected not null", updatedEntity);
		Assert.assertEquals("failure - expected id attribute match", id, updatedEntity.getId());
		Assert.assertEquals("failure - expected text attribute match", updatedText, updatedEntity.getText());

	}

	@Test
	public void testDelete() {
		Long id = Long.valueOf(1);
		Greeting entity = service.findOne(id);
		Assert.assertNotNull("failure - expected not null", entity);
		service.delete(id);
		Collection<Greeting> list = service.findAll();
		Assert.assertEquals("failure - expected size", 1, list.size());
		Greeting deletedEntity = service.findOne(id);
		Assert.assertNull("failure - expected null", deletedEntity);

	}
}
