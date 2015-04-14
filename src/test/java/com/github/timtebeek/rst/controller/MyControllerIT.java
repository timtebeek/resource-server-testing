package com.github.timtebeek.rst.controller;

import static org.junit.Assert.assertTrue;

import com.github.timtebeek.rst.MyApp;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MyApp.class)
@WebIntegrationTest(randomPort = true)
public class MyControllerIT {
	@Value("http://localhost:${local.server.port}")
	private String	host;

	@Test
	@WithMockUser(roles = "myrole")
	public void testHello() {
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> entity = restTemplate.getForEntity(host + "/hello", String.class);
		assertTrue(entity.getStatusCode().is2xxSuccessful());
	}
}
