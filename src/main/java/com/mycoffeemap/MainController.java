package com.mycoffeemap;


import org.springframework.ui.Model;

import java.util.ArrayList;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

	// http://localhost:8070/mycoffeemap
	@GetMapping("/mycoffeemap")
	public String index(Model model) {
        model.addAttribute("recentPosts", new ArrayList<>()); // 또는 실제 서비스 연결
        return "fragments/main-content"; // 프래그먼트 자체를 뷰로 리턴
    }
}
