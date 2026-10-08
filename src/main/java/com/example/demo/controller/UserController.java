package com.example.demo.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.*;
import com.example.demo.service.AccountService;
import com.example.demo.service.UserService;

@RestController
public class UserController {
	private final UserService service;
	public UserController(UserService service) {
		this.service=service;
	}
	@PostMapping("/user/create")
	public User createUser(@RequestBody User user) {
	    return service.createUser(user);
	}

}