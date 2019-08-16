package com.mindtree.webstarter.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindtree.webstarter.WebStarter1ApplicationTests;
import com.mindtree.webstarter.api.GreetingController;
import com.mindtree.webstarter.web.api.BaseController;

@WebAppConfiguration
public class AbstractControllerTest extends WebStarter1ApplicationTests {

	protected MockMvc mvc;

	@Autowired
	protected WebApplicationContext webApplicationContext;

	protected void setUp() {
		mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}

	protected void setUp(GreetingController controller) {
		mvc = MockMvcBuilders.standaloneSetup(controller).build();
	}

	protected String mapToJson(Object object) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(object);
	}

	protected <T> T mapFromJson(String json, Class<T> clazz)
			throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(json, clazz);
	}
}
