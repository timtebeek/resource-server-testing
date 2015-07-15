package com.github.timtebeek.rst.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.github.timtebeek.rst.MyApp;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MyApp.class)
@WebAppConfiguration
public class MyControllerTest {
	@Autowired
	private WebApplicationContext webapp;

	private MockMvc mvc;

	@Before
	public void before() {
		mvc = MockMvcBuilders.webAppContextSetup(webapp).apply(springSecurity()).build();
	}

	@Test
	@WithMockUser(roles = "myrole")
	public void testHelloAnnotationWithRole() throws Exception {
		mvc.perform(get("/hello")).andDo(print()).andExpect(status().isOk());
	}

	@Test
	@WithMockUser
	public void testHelloAnnotationWithoutRole() throws Exception {
		mvc.perform(get("/hello")).andDo(print()).andExpect(status().isOk());
	}

	@Test
	public void testHelloAuthenticationWithRole() throws Exception {
		mvc.perform(get("/hello").with(authentication(token("myrole")))).andDo(print()).andExpect(status().isOk());
	}

	@Test
	public void testHelloAuthenticationWithoutRole() throws Exception {
		mvc.perform(get("/hello").with(authentication(token("unrelatedrole")))).andDo(print()).andExpect(status().isOk());
	}

	@Test
	public void testHelloUser() throws Exception {
		mvc.perform(get("/hello").with(user("user"))).andDo(print()).andExpect(status().isOk());
	}

	private static Authentication token(final String role) {
		return new TestingAuthenticationToken("user", null, role);
	}
}
