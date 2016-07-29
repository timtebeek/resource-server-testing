package com.github.timtebeek.rst.controller;

import java.security.Principal;

import com.github.timtebeek.rst.service.MyService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class MyController {
	MyService service;
	
	@RequestMapping("/hello")
	public String hello(final Principal principal) {
		return service.greeting() + principal.getName();
	}
}
