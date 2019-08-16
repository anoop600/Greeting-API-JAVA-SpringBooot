package com.mindtree.webstarter.api;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.any;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import com.mindtree.webstarter.model.Greeting;
import com.mindtree.webstarter.service.AbstractControllerTest;
import com.mindtree.webstarter.service.EmailService;
import com.mindtree.webstarter.service.GreetingService;

@Transactional
public class GreetingControllerMocksTest extends AbstractControllerTest {
	@Mock
	private EmailService emailService;

	@Mock
	private GreetingService greetingService;

	@InjectMocks
	private GreetingController controller;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);

		setUp(controller);
	}

	private Collection<Greeting> getEntityListStubData() {
		Collection<Greeting> list = new ArrayList<>();
		list.add(getEntityStubData());
		return list;
	}

	private Greeting getEntityStubData() {
		Greeting entity = new Greeting();
		entity.setId(1L);
		entity.setText("hello");
		return entity;
	}

	@Test
	public void testGetGreetings() throws Exception {
		Collection<Greeting> list = getEntityListStubData();
		when(greetingService.findAll()).thenReturn(list);
		String uri = "/api/greeting";
		MvcResult result = mvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON)).andReturn();
		String content = result.getResponse().getContentAsString();
		int status = result.getResponse().getStatus();
		verify(greetingService, times(1)).findAll();
		Assert.assertEquals("failure - expected HTTP status 200", 200, status);
		Assert.assertTrue("failure - expected HTTP response body to have a avalue", content.trim().length() > 0);
	}

	@Test
	public void testGetGreeting() throws Exception {
		Long id = Long.valueOf(1);
		Greeting entity = getEntityStubData();
		when(greetingService.findOne(id)).thenReturn(entity);
		String uri = "/api/greeting/{id}";
		MvcResult result = mvc.perform(MockMvcRequestBuilders.get(uri, id).accept(MediaType.APPLICATION_JSON))
				.andReturn();
		String content = result.getResponse().getContentAsString();
		int status = result.getResponse().getStatus();
		verify(greetingService, times(1)).findOne(id);
		Assert.assertEquals("failure - expected HTTP status 200", 200, status);
		Assert.assertTrue("failure - expected HTTP response body to have a value", content.trim().length() > 0);
	}

	@Test
	public void testGetGreetingNotFound() throws Exception {
		Long id = Long.MAX_VALUE;
		when(greetingService.findOne(id)).thenReturn(null);
		String uri = "/api/greeting/{id}";
		MvcResult result = mvc.perform(MockMvcRequestBuilders.get(uri, id).accept(MediaType.APPLICATION_JSON))
				.andReturn();
		String content = result.getResponse().getContentAsString();
		int status = result.getResponse().getStatus();
		verify(greetingService, times(1)).findOne(id);
		Assert.assertEquals("failure - expected HTTP status 404", 404, status);
		Assert.assertTrue("failure - expected HTTP response body to be empty", content.trim().length() == 0);
	}

	@Test
	public void testCreateGreeting() throws Exception {
		Greeting entity = getEntityStubData();
		when(greetingService.create(any(Greeting.class))).thenReturn(entity);
		String uri = "/api/greeting";
		String inputJson = super.mapToJson(entity);
		MvcResult result = mvc.perform(MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).content(inputJson)).andReturn();
		String content = result.getResponse().getContentAsString();
		int status = result.getResponse().getStatus();
		verify(greetingService, times(1)).create(any(Greeting.class));
		Assert.assertEquals("failure - expected HTTP status 201", 201, status);
		Assert.assertTrue("failure - expected HTTP response body to have a value", content.trim().length() > 0);
		Greeting createdEntity = super.mapFromJson(content, Greeting.class);
		Assert.assertNotNull("failure - expected entity not null", createdEntity);
		Assert.assertNotNull("failure - expected id attribute not null", createdEntity.getId());
		Assert.assertEquals("failure - expected text attribute match", entity.getText(), createdEntity.getText());
	}

	@Test
	public void testUpdateGreeting() throws Exception {
		Greeting entity = getEntityStubData();
		entity.setText(entity.getText() + " test");
		Long id = Long.valueOf(1);
		when(greetingService.update(any(Greeting.class))).thenReturn(entity);
		String uri = "/api/greeting/";
		String inputJson = super.mapToJson(entity);
		MvcResult result = mvc.perform(MockMvcRequestBuilders.put(uri, id).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).content(inputJson)).andReturn();
		String content = result.getResponse().getContentAsString();
		int status = result.getResponse().getStatus();
		verify(greetingService, times(1)).update(any(Greeting.class));
		Assert.assertEquals("failure - expected HTTP status 200", 200, status);
		Assert.assertTrue("failure - expected HTTP response body to have a value", content.trim().length() > 0);
		Greeting updatedEntity = super.mapFromJson(content, Greeting.class);
		Assert.assertNotNull("failure - expected entity not null", updatedEntity);
		Assert.assertEquals("failure - expected id attribute unchanged", entity.getId(), updatedEntity.getId());
		Assert.assertEquals("failure - expected text attribute match", entity.getText(), updatedEntity.getText());

	}

//	@Test
//	public void testDeleteGreeting() throws Exception {
//		Long id = Long.valueOf(1);
//		String uri = "/api/greeting/{id}";
//		MvcResult result =mvc.perform(MockMvcRequestBuilders.delete(uri, id)).andReturn();
//		String content = result.getResponse().getContentAsString();
//		int status = result.getResponse().getStatus();
//		verify(greetingService, times(1)).delete(id);
//		Assert.assertEquals("failure - expected HTTP status 415", 415, status);
//		Assert.assertTrue("failure - expected HTTP response body to be empty", content.trim().length() == 0);
//
//	}

}
