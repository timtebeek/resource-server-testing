package com.github.timtebeek.rst.controller;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.github.timtebeek.rst.config.OAuthHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
public class MyControllerTest {
	@Autowired
	private WebApplicationContext	context;
	private MockMvc					mvc;

	@Before
	public void before() {
		mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).alwaysDo(print()).build();
	}

	@Autowired
	private OAuthHelper helper;

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

	@Test
	public void testHelloAliceEveDenied() throws Exception {
		RequestPostProcessor bearerToken = helper.bearerToken("myclientwith", "eve");
		mvc.perform(get("/hello").with(bearerToken)).andExpect(status().isForbidden());
	}

}
