package com.github.timtebeek.rst.service;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
@SuppressWarnings("static-method")
public class MyService {
	@PreAuthorize("authentication.name == 'alice' or authentication.name == 'user'")
	public String greeting() {
		return "Hello ";
	}
}
