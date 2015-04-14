package com.github.timtebeek.rst.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SuppressWarnings("static-method")
public class MyController {
	@RequestMapping("/hello")
	@PreAuthorize("hasRole('myrole')")
	public String hello() {
		return "world";
	}
}
