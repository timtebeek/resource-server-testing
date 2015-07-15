package com.github.timtebeek.rst;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

@Component
public class OAuthHelper {
	// For use with integration tests
	private static final String	ACCESS_TOKEN_URI	= "http://localhost:8080/oauth/token";

	public static OAuth2RestTemplate withMyscope(final String tokenurl) {
		ResourceOwnerPasswordResourceDetails details = new ResourceOwnerPasswordResourceDetails();
		details.setAccessTokenUri(tokenurl != null ? tokenurl : ACCESS_TOKEN_URI);
		details.setClientId("myclientwith");
		details.setUsername("user");
		details.setPassword("password");
		return new OAuth2RestTemplate(details);
	}

	public static OAuth2RestTemplate withoutMyscope(final String tokenurl) {
		ResourceOwnerPasswordResourceDetails details = new ResourceOwnerPasswordResourceDetails();
		details.setAccessTokenUri(tokenurl != null ? tokenurl : ACCESS_TOKEN_URI);
		details.setClientId("myclientwithout");
		details.setUsername("user");
		details.setPassword("password");
		return new OAuth2RestTemplate(details);
	}

	// For use with MockMvc
	@Autowired
	AuthorizationServerTokenServices tokenservice;

	public RequestPostProcessor bearerToken(final String clientid, final Set<String> scopes, final String principal) {
		return new RequestPostProcessor() {
			@Override
			public MockHttpServletRequest postProcessRequest(final MockHttpServletRequest mockRequest) {
				// Create OAuth2 token
				OAuth2Request oauth2Request = new OAuth2Request(null, clientid, null, true, scopes, null, null, null, null);
				Authentication userauth = new TestingAuthenticationToken(principal, null);
				OAuth2Authentication oauth2auth = new OAuth2Authentication(oauth2Request, userauth);
				OAuth2AccessToken token = tokenservice.createAccessToken(oauth2auth);

				// Set Authorization header to use Bearer
				mockRequest.addHeader("Authorization", "Bearer " + token.getValue());
				return mockRequest;
			}
		};
	}
}
