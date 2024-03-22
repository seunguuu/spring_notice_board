package com.hello.forum.sample.web;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IntroduceController {
	
	// http://localhost:8080/introduce
	
	@GetMapping("/introduce")
	public String introduceJSP(Model model) {
	
		model.addAttribute("name", "유승훈");
		model.addAttribute("age", 26);
		model.addAttribute("location", "Changwon");
		
		return "introduce";
	}
	
}
