package com.mycoffeemap;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
	
	//GET  http://localhost:8070/mycoffeemap
	@GetMapping("/mycoffeemap")
    public String home(Model model) {
	    return "index";
    }
	

}




