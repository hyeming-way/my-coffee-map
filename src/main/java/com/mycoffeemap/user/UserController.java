package com.mycoffeemap.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UserController {
	
	@GetMapping("/user/login")
	public String login() {
	    return "user/login"; 
	}
	
}




