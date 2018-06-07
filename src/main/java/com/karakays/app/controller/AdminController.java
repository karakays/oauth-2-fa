package com.karakays.app.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminController {

	@GetMapping("foo")
	public ResponseEntity<Void> foo() {
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
}