package com.github.timtebeek.rst.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
@EnableWebSecurity
@EnableResourceServer
public class SecurityConfig extends ResourceServerConfigurerAdapter {
	@Override
	public void configure(final HttpSecurity http) throws Exception {
		http.authorizeRequests()
		.anyRequest().authenticated();
	}

	@Override
	public void configure(final ResourceServerSecurityConfigurer resources) throws Exception {
		resources
		.resourceId("myresource")
		.tokenStore(tokenStore());
	}

	@Value("${my.verifierkey}")
	private String	verifierkey;

	@Bean
	public JwtTokenStore tokenStore() throws Exception {
		JwtAccessTokenConverter enhancer = new JwtAccessTokenConverter();
		enhancer.setVerifierKey(verifierkey);
		enhancer.afterPropertiesSet();
		return new JwtTokenStore(enhancer);
	}
}
