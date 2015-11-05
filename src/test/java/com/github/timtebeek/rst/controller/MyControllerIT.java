package com.github.timtebeek.rst.controller;

import static org.junit.Assert.assertTrue;

import com.github.timtebeek.rst.MyApp;
import lombok.Getter;
import lombok.Setter;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.test.OAuth2ContextConfiguration;
import org.springframework.security.oauth2.client.test.OAuth2ContextSetup;
import org.springframework.security.oauth2.client.test.RestTemplateHolder;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestOperations;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MyApp.class)
@WebIntegrationTest(randomPort = true)
@OAuth2ContextConfiguration(MyDetails.class)
public class MyControllerIT implements RestTemplateHolder {
	@Value("http://localhost:${local.server.port}")
	@Getter
	String						host;

	@Getter
	@Setter
	RestOperations				restTemplate	= new TestRestTemplate();

	@Rule
	public OAuth2ContextSetup	context			= OAuth2ContextSetup.standard(this);

	@Test
	public void testHelloOAuth2WithRole() {
		ResponseEntity<String> entity = getRestTemplate().getForEntity(host + "/hello", String.class);
		assertTrue(entity.getStatusCode().is2xxSuccessful());
	}
}

class MyDetails extends ResourceOwnerPasswordResourceDetails {
	public MyDetails(final Object obj) {
		MyControllerIT it = (MyControllerIT) obj;
		setAccessTokenUri(it.getHost() + "/oauth/token");
		setClientId("myclientwith");
		setUsername("user");
		setPassword("password");
	}
}
