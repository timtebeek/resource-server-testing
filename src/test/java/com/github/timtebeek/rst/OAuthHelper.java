package com.github.timtebeek.rst;

import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
import org.springframework.stereotype.Component;

@Component
public class OAuthHelper {
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
}
