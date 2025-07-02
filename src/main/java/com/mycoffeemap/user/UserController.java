package com.mycoffeemap.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@GetMapping("/login")
	public String login() {
	    return "user/login"; 
	}
	
	@GetMapping("/join")
	public String join() {
	    return "user/join"; 
	}
	
}




