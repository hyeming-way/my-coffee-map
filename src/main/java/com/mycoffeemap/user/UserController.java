package com.mycoffeemap.user;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@GetMapping("/login")
	public String login() {
	    return "user/login"; 
	}
	
	@GetMapping("/join")
	public String join(Model model) {
		model.addAttribute("JoinForm", new JoinForm());
	    return "user/join"; 
	}
	
	@PostMapping("/join")
	public String submit(@Valid @ModelAttribute("JoinForm") JoinForm joinForm,
	                     BindingResult bindingResult, Model model) {
				
	    if (bindingResult.hasErrors()) {
			if (!joinForm.getPass().equals(joinForm.getPassCheck())) {
				bindingResult.rejectValue("passCheck", "NotMatched", "비밀번호가 일치하지 않습니다.");
			}
	    	model.addAttribute("submitted", true);
	        return "user/join"; // 오류 났을 때 다시 폼으로 이동
	    }
	    
	    return null;
	}
	
}




