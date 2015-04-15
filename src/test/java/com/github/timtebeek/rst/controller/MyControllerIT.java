package com.github.timtebeek.rst.controller;

import static org.junit.Assert.assertTrue;

import com.github.timtebeek.rst.MyApp;
import com.github.timtebeek.rst.OAuthHelper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.common.exceptions.InsufficientScopeException;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MyApp.class)
@WebIntegrationTest(randomPort = true)
@Slf4j
public class MyControllerIT {
	@Value("http://localhost:${local.server.port}")
	private String	host;

	@Test
	public void testHelloOAuth2WithRole() {
		OAuth2RestTemplate restTemplate = OAuthHelper.withMyscope(host + "/oauth/token");
		log.info("{}", restTemplate.getAccessToken().getValue());
		ResponseEntity<String> entity = restTemplate.getForEntity(host + "/hello", String.class);
		assertTrue(entity.getStatusCode().is2xxSuccessful());
	}

	@Test(expected = InsufficientScopeException.class)
	public void testHelloOAuth2WithoutRole() {
		OAuth2RestTemplate restTemplate = OAuthHelper.withoutMyscope(host + "/oauth/token");
		log.info("{}", restTemplate.getAccessToken().getValue());
		ResponseEntity<String> entity = restTemplate.getForEntity(host + "/hello", String.class);
		assertTrue(entity.getStatusCode().is2xxSuccessful());
	}
}
