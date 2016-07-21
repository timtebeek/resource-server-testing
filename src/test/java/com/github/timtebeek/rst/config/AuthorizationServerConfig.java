package com.github.timtebeek.rst.config;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

@Configuration
@EnableAuthorizationServer
class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
	@Value("${resource-server-testing.oauth2.jwt.private-key-value}")
	private String	signing;
	@Value("${security.oauth2.resource.jwt.key-value}")
	private String	verifier;

	@Bean
	public JwtAccessTokenConverter accessTokenConverter() throws Exception {
		JwtAccessTokenConverter jwt = new JwtAccessTokenConverter();
		jwt.setSigningKey(signing);
		jwt.setVerifierKey(verifier);
		jwt.afterPropertiesSet();
		return jwt;
	}

	@Autowired
	private AuthenticationManager	authenticationManager;

	@Override
	public void configure(final AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints
		.authenticationManager(authenticationManager)
		.accessTokenConverter(accessTokenConverter());
	}

	@Override
	public void configure(final ClientDetailsServiceConfigurer clients) throws Exception {
		clients.inMemory()
		.withClient("myclientwith")
		.authorizedGrantTypes("password")
		.authorities("myauthorities")
		.resourceIds("myresource")
		.scopes("myscope")

		.and()
		.withClient("myclientwithout")
		.authorizedGrantTypes("password")
		.authorities("myauthorities")
		.resourceIds("myresource")
		.scopes(UUID.randomUUID().toString());
	}
}

@Configuration
@EnableWebSecurity
class AuthServerConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	@SuppressWarnings("static-method")
	public void configureGlobal(final AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication()
		.withUser("user").password("password").authorities("user").and()
		.withUser("alice").password("password").authorities("user").and()
		.withUser("bob").password("password").authorities("user").and()
		.withUser("eve").password("password").authorities("user");
	}
}
