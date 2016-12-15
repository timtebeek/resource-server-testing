package com.github.timtebeek.rst.config;

import java.util.HashSet;
import java.util.Set;

import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithOAuth2AuthenticationSecurityContextFactory implements WithSecurityContextFactory<WithOAuth2Authentication> {
	@Override
	public SecurityContext createSecurityContext(final WithOAuth2Authentication oauth) {
		SecurityContext context = SecurityContextHolder.createEmptyContext();
		Set<String> scope = new HashSet<>();
		scope.add(oauth.scope());
		OAuth2Request request = new OAuth2Request(null, oauth.clientId(), null, true, scope, null, null, null, null);
		Authentication auth = new OAuth2Authentication(request, new TestingAuthenticationToken(oauth.username(), null, "read"));
		context.setAuthentication(auth);
		return context;
	}
}
