package com.github.timtebeek.rst.service;

import com.github.timtebeek.rst.config.WithOAuth2Authentication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MyServiceTest {
	@Autowired
	MyService myservice;

	@Test
	@WithMockUser
	public void testGreetingWithMockUser() {
		myservice.greeting();
	}

	@Test(expected = AccessDeniedException.class)
	@WithMockUser("eve")
	public void testGreetingWithMockEve() {
		myservice.greeting();
	}

	@Test
	@WithOAuth2Authentication
	public void testGreetingWithOAuth2AuthenticationUser() {
		myservice.greeting();
	}

	@Test(expected = AccessDeniedException.class)
	@WithOAuth2Authentication(username = "eve")
	public void testGreetingWithOAuth2AuthenticationEve() {
		myservice.greeting();
	}
}
