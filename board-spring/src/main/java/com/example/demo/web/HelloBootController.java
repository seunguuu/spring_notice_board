package com.example.demo.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloBootController {
	
	@GetMapping("/hello")
	public ResponseEntity<String> hello(){
		return new ResponseEntity<>("Hello Boot Controller", HttpStatus.OK);
	}
}
