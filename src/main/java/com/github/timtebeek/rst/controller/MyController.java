package com.github.timtebeek.rst.controller;

import java.security.Principal;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SuppressWarnings("static-method")
public class MyController {
	@RequestMapping("/hello")
	public String hello(final Principal principal) {
		return "Hello " + principal.getName();
	}
}
