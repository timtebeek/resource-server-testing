package com.github.timtebeek.rst.service;

import com.github.timtebeek.rst.MyApp;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringApplicationConfiguration(classes = MyApp.class)
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
}
