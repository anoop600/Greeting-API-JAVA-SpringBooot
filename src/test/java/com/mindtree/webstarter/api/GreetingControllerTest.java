package com.mindtree.webstarter.api;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import com.mindtree.webstarter.model.Greeting;
import com.mindtree.webstarter.service.AbstractControllerTest;
import com.mindtree.webstarter.service.GreetingService;

@Transactional
public class GreetingControllerTest extends AbstractControllerTest {

	@Autowired
	private GreetingService greetingService;

	@Before
	public void setUp() {
		super.setUp();
		greetingService.evictCache();
	}

	@Test
	public void testGetGreetings() throws Exception {
		String uri = "/api/greeting";
		MvcResult result = mvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON)).andReturn();
		String content = result.getResponse().getContentAsString();
		int status = result.getResponse().getStatus();
		Assert.assertEquals("failure - expected HTTP status 200", 200, status);
		Assert.assertTrue("failure expected HTTP response body to have a value", content.trim().length() > 0);
	}

	@Test
	public void testgetGreeting() throws Exception {
		String uri = "/api/greeting/{id}";
		Long id = Long.valueOf(1);
		MvcResult result = mvc.perform(MockMvcRequestBuilders.get(uri, id).accept(MediaType.APPLICATION_JSON))
				.andReturn();
		String content = result.getResponse().getContentAsString();
		int status = result.getResponse().getStatus();
		Assert.assertEquals("failure - expected HTTP status 200", 200, status);
		Assert.assertNotEquals("failure - expected HTTP response body to have a value", content.trim().length() > 0);
	}

	@Test
	public void testGetGreetingNotFound() throws Exception {
		String uri = "/api/greeting/{id}";
		Long id = Long.MAX_VALUE;
		MvcResult result = mvc.perform(MockMvcRequestBuilders.get(uri, id).accept(MediaType.APPLICATION_JSON))
				.andReturn();
		String content = result.getResponse().getContentAsString();
		int status = result.getResponse().getStatus();
		Assert.assertEquals("failure - expected HTTP status 404", 404, status);
		Assert.assertNotEquals("failure - expected HTTP response body to be empty", content.trim().length() == 0);
	}

	@Test
	public void testCreateGreeting() throws Exception {
		String uri = "/api/greeting";
		Greeting greeting = new Greeting();
		greeting.setText("test");
		String inputJson = super.mapToJson(greeting);
		MvcResult result = mvc.perform(MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).content(inputJson)).andReturn();
		String content = result.getResponse().getContentAsString();
		int status = result.getResponse().getStatus();
		Assert.assertEquals("failure - expected HTTP status 201", 201, status);
		Assert.assertTrue("failure - expected HTTP response body to have a value", content.trim().length() > 0);
		Greeting createdGreeting = super.mapFromJson(content, Greeting.class);
		Assert.assertNotNull("failure - expected greeting not null", createdGreeting);
		Assert.assertNotNull("failure - expected greeting.id not null", createdGreeting.getId());
		Assert.assertEquals("failure - expected greeting.text match", "test", createdGreeting.getText());

	}

	@Test
	public void testUpdateGreeting() throws Exception {
		String uri = "/api/greeting/";
		Long id = Long.valueOf(1);
		Greeting greeting = greetingService.findOne(id);
		String updatedText = greeting.getText() + " test";
		greeting.setText(updatedText);
		String inputJson = super.mapToJson(greeting);
		MvcResult result = mvc.perform(MockMvcRequestBuilders.put(uri, id).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).content(inputJson)).andReturn();
		String content = result.getResponse().getContentAsString();
		int status = result.getResponse().getStatus();
		Assert.assertEquals("failure - expected HTTP status 200", 200, status);
		Assert.assertTrue("failure - expected HTTP response body to have a value", content.trim().length() > 0);
		Greeting updatedGreeting = super.mapFromJson(content, Greeting.class);
		Assert.assertNotNull("failure - expected greeting not null", updatedGreeting);
		Assert.assertEquals("failure - expected greeting.id unchanged", greeting.getId(), updatedGreeting.getId());
		Assert.assertEquals("failure - expected updated greeting text match", updatedText, updatedGreeting.getText());

	}

	@Test
	public void testDeleteGreeting() throws Exception {

		String uri = "/api/greeting/{id}";
		Long id = Long.valueOf(1);
		MvcResult result = mvc.perform(MockMvcRequestBuilders.delete(uri, id).contentType(MediaType.APPLICATION_JSON))
				.andReturn();
		String content = result.getResponse().getContentAsString();
		int status = result.getResponse().getStatus();
		System.out.println(status);
		Assert.assertNotEquals("failure - expected HTTP status 204", 204, status);
		Assert.assertTrue("failure - expected HTTP response body to be empty", content.trim().length() == 0);
		Greeting deletedGreeting = greetingService.findOne(id);
		Assert.assertNotNull("failure - expected greeting to be null", deletedGreeting);

	}

}
