package com.github.timtebeek.rst.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.github.timtebeek.rst.config.OAuthHelper;
import com.github.timtebeek.rst.service.MyService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

@RunWith(SpringRunner.class)
@WebMvcTest(includeFilters = @Filter(type = FilterType.REGEX, pattern = "com\\.github\\.timtebeek\\.rst\\.config\\..*"))
public class MyControllerTest {
	@Autowired
	private MockMvc		mvc;

	@Autowired
	private OAuthHelper	helper;

	@MockBean
	private MyService	service;

	@Before
	public void setup() {
		when(service.greeting()).thenReturn("Hello ");
	}

	@Test
	public void testHelloUserWithRole() throws Exception {
		RequestPostProcessor bearerToken = helper.bearerToken("myclientwith", "user");
		mvc.perform(get("/hello").with(bearerToken)).andExpect(status().isOk()).andExpect(content().string("Hello user"));
	}

	@Test
	public void testHelloAliceWithRole() throws Exception {
		RequestPostProcessor bearerToken = helper.bearerToken("myclientwith", "alice");
		mvc.perform(get("/hello").with(bearerToken)).andExpect(status().isOk()).andExpect(content().string("Hello alice"));
	}

	@Test
	public void testHelloWithoutRole() throws Exception {
		RequestPostProcessor bearerToken = helper.bearerToken("myclientwithout", "user");
		mvc.perform(get("/hello").with(bearerToken)).andExpect(status().isForbidden());
	}
}
