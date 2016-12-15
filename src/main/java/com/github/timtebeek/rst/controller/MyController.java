package com.github.timtebeek.rst.controller;

import com.github.timtebeek.rst.service.MyService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class MyController {
	MyService service;

	@RequestMapping("/hello")
	@PreAuthorize("#oauth2.hasScope('myscope')")
	public String hello(@AuthenticationPrincipal final String username) {
		return service.greeting() + username;
	}
}
