package com.mycoffeemap;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MainController {
	
	//GET  http://localhost:8070/mycoffeemap
	@GetMapping("/mycoffeemap")
	@ResponseBody //URL 요청에 대한 응답으로 문자열을 브라우저로 리턴하라는 의미로 쓰였다.
    public String index() {
       return "하잉";
    }
	

}




