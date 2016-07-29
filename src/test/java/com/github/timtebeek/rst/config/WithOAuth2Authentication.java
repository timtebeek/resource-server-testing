package com.github.timtebeek.rst.config;

import java.lang.annotation.*;

import org.springframework.security.test.context.support.WithSecurityContext;

@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@WithSecurityContext(factory = WithOAuth2AuthenticationSecurityContextFactory.class)
public @interface WithOAuth2Authentication {
	String clientId() default "myclientwith";

	String username() default "user";
}
