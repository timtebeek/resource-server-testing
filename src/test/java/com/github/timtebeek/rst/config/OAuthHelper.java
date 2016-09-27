package com.github.timtebeek.rst.config;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

@Component
@AllArgsConstructor
public class OAuthHelper {
	ClientDetailsService				clientDetailsService;
	UserDetailsService					userDetailsService;
	AuthorizationServerTokenServices	tokenservice;

	// For use with MockMvc
	public RequestPostProcessor bearerToken(final String clientid, final String username) {
		return mockRequest -> {
			OAuth2Authentication auth = oAuth2Authentication(clientid, username);
			OAuth2AccessToken token = tokenservice.createAccessToken(auth);
			mockRequest.addHeader("Authorization", "Bearer " + token.getValue());
			return mockRequest;
		};
	}

	// For use with @WithOAuth2Authentication
	public OAuth2Authentication oAuth2Authentication(final String clientId, final String username) {
		// Look up authorities, resourceIds and scopes based on clientId
		ClientDetails client = clientDetailsService.loadClientByClientId(clientId);
		Collection<GrantedAuthority> authorities = client.getAuthorities();
		Set<String> resourceIds = client.getResourceIds();
		Set<String> scopes = client.getScope();

		// Default values for other parameters
		Map<String, String> requestParameters = Collections.emptyMap();
		boolean approved = true;
		String redirectUrl = null;
		Set<String> responseTypes = Collections.emptySet();
		Map<String, Serializable> extensionProperties = Collections.emptyMap();

		// Create request
		OAuth2Request oAuth2Request = new OAuth2Request(requestParameters, clientId, authorities, approved, scopes, resourceIds, redirectUrl, responseTypes, extensionProperties);

		// Create OAuth2AccessToken
		UserDetails user = userDetailsService.loadUserByUsername(username);
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user, null, authorities);
		OAuth2Authentication auth = new OAuth2Authentication(oAuth2Request, authenticationToken);
		return auth;
	}
}
