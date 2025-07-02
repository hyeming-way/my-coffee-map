package com.mycoffeemap;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

	// http://localhost:8070/mycoffeemap
    @GetMapping("/mycoffeemap")
    public String index() {
        return "index"; // templates/index.html 렌더링됨
    }
}
