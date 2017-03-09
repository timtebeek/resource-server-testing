package com.github.timtebeek.rst.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import lombok.Getter;
import lombok.Setter;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.resource.OAuth2AccessDeniedException;
import org.springframework.security.oauth2.client.test.OAuth2ContextConfiguration;
import org.springframework.security.oauth2.client.test.OAuth2ContextSetup;
import org.springframework.security.oauth2.client.test.RestTemplateHolder;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class MyControllerIT implements RestTemplateHolder {
	@Value("http://localhost:${local.server.port}")
	protected String			host;

	@Getter
	@Setter
	private RestOperations		restTemplate	= new RestTemplate();

	@Rule
	public OAuth2ContextSetup	context			= OAuth2ContextSetup.standard(this);

	@Test
	@OAuth2ContextConfiguration(UserDetails.class)
	public void testHelloUser() {
		ResponseEntity<String> entity = restTemplate.getForEntity(host + "/hello", String.class);
		assertTrue(entity.toString(), entity.getStatusCode().is2xxSuccessful());
		assertEquals("Hello user", entity.getBody());
	}

	@Test
	@OAuth2ContextConfiguration(AliceDetails.class)
	public void testHelloAlice() {
		ResponseEntity<String> entity = restTemplate.getForEntity(host + "/hello", String.class);
		assertTrue(entity.toString(), entity.getStatusCode().is2xxSuccessful());
		assertEquals("Hello alice", entity.getBody());
	}

	@Test(expected = OAuth2AccessDeniedException.class)
	@OAuth2ContextConfiguration(EveDetails.class)
	public void testHelloEve() {
		restTemplate.getForEntity(host + "/hello", String.class);
	}
}

class UserDetails extends ResourceOwnerPasswordResourceDetails {
	public UserDetails(final Object obj) {
		MyControllerIT it = (MyControllerIT) obj;
		setAccessTokenUri(it.host + "/oauth/token");
		setClientId("myclientwith");
		setUsername("user");
		setPassword("password");
	}
}

class AliceDetails extends ResourceOwnerPasswordResourceDetails {
	public AliceDetails(final Object obj) {
		MyControllerIT it = (MyControllerIT) obj;
		setAccessTokenUri(it.host + "/oauth/token");
		setClientId("myclientwith");
		setUsername("alice");
		setPassword("password");
	}
}

class EveDetails extends ResourceOwnerPasswordResourceDetails {
	public EveDetails(final Object obj) {
		MyControllerIT it = (MyControllerIT) obj;
		setAccessTokenUri(it.host + "/oauth/token");
		setClientId("myclientwith");
		setUsername("eve");
		setPassword("password");
	}
}
