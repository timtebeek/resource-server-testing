package com.github.timtebeek.rst.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import javax.servlet.ServletException;

import com.github.timtebeek.rst.config.WithOAuth2Authentication;
import com.github.timtebeek.rst.service.MyService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MyControllerTest {
	private MockMvc					mvc;
	@Autowired
	private WebApplicationContext	webapp;
	@MockBean
	private MyService				service;

	@Before
	public void setup() {
		mvc = webAppContextSetup(webapp).build();
		when(service.greeting()).thenReturn("Hello ");
	}

	@Test
	@WithOAuth2Authentication
	public void testHelloUserWithRole() throws Exception {
		mvc.perform(get("/hello")).andExpect(status().isOk()).andExpect(content().string("Hello user"));
	}

	@Test
	@WithOAuth2Authentication(username = "alice")
	public void testHelloAliceWithRole() throws Exception {
		mvc.perform(get("/hello")).andExpect(status().isOk()).andExpect(content().string("Hello alice"));
	}

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	@WithOAuth2Authentication(scope = "notmyscope")
	public void testHelloWithoutRole() throws Exception {
		thrown.expect(ServletException.class);
		thrown.expectMessage("Request processing failed; nested exception is org.springframework.security.access.AccessDeniedException: Access is denied");
		mvc.perform(get("/hello"));
	}
}
