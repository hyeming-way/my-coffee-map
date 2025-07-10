package com.mycoffeemap.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	UserService userService;
	
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	//로그인 화면 보여주기
	@GetMapping("/login")
	public String login(HttpServletRequest request, HttpSession session) {
	    String referer = request.getHeader("Referer");
	    if (referer != null && !referer.contains("/login")) {
	        session.setAttribute("prevPage", referer);  // 로그인 직전 페이지 기억
	    }
	    return "user/login";
	}	
	
	//회원가입 폼 화면 보여주기
	@GetMapping("/join")
	public String join(Model model) {
		model.addAttribute("JoinForm", new JoinForm());
	    return "user/join"; 
	}	
	
	//사용자가 회원가입 입력 폼에 값을 입력하고 '송신'버튼을 눌렀을 때 호출
	@PostMapping("/join")
	public String submit(@Valid @ModelAttribute("JoinForm") JoinForm joinForm,
						 @RequestParam("imgUpload") MultipartFile imgFile,
	                     BindingResult bindingResult, Model model) {
		
		log.info("UserController의 submit 메소드 실행");
		
		//비밀번호 재확인 서버단에서 한번 더 검증
	    if (!joinForm.getPass().equals(joinForm.getPassCheck())) {
	        bindingResult.rejectValue("passCheck", "error.passCheck", "パスワードが一致しません。");
	        return "user/join";
	    }
		//서버단에서 유효성 검사
	    if (bindingResult.hasErrors()) {
	    	model.addAttribute("submitted", true);
	        return "user/join"; 
	    }	    
	    	    
	    //사용자에게 본인 인증 이메일 보내기
	    userService.registerUser(joinForm, imgFile); 
	    
	    return "user/mail-sended";
	    
	} //submit
	
	
	//이메일 중복 검사
	@GetMapping("/checkEmail")
	@ResponseBody
	public boolean checkEmail(@RequestParam("email") String email) {
		return userService.isEmailAvailable(email);  // true : 사용 가능
	}	
	
	
	//닉네임 중복 검사
	@GetMapping("/checkNick")
	@ResponseBody
	public boolean checkNick(@RequestParam("nick") String nick) {
		return userService.isNickAvailable(nick);  // true : 사용 가능
	}		
	
	
	//사용자 이메일 인증
	@GetMapping("/verify")
	public String verifyUser(@RequestParam("token") String token, Model model) {		
		return userService.verifyUser(token, model);
	} 
	

	//로그인 처리
	@PostMapping("/login.do")
	public String doLogin(@RequestParam("email") String email, @RequestParam("pass") String pass,
	                      HttpSession session, Model model) {
	    User user = userRepository.findByEmail(email);
	    if (user == null || !passwordEncoder.matches(pass, user.getPass())) {
	    	model.addAttribute("loginError", "メールアドレスまたはパスワードが違います。");
	        return "user/login";
	    }

	    if (!user.isEnabled()) {
		    model.addAttribute("loginError", "メール認証がまだ完了していません。");
		    return "user/login";
		}

		    session.setAttribute("user", user);

		    // 이전 페이지가 있다면 해당 페이지로 리다이렉트
		    String redirectUrl = (String) session.getAttribute("prevPage");
		    session.removeAttribute("prevPage");

		    if (redirectUrl != null) {
		        return "redirect:" + redirectUrl;
		    }

		    // 없으면 메인으로
		    return "redirect:/mycoffeemap";
		    
		} //doLogin

	
	//로그아웃 처리
	@GetMapping("/logout.do")
	public String logout (HttpSession session) {
	    session.invalidate(); 
	    return "redirect:/mycoffeemap";
	} //logout
	
	
	//프로필 수정 폼 이동
	@GetMapping("/profile")
	public String profile (HttpSession session, Model model) {
		
		User user = (User)session.getAttribute("user");		
		if (user == null) {
			model.addAttribute("loginError", "このサービスを利用するには、ログインが必要です。");
			return "user/login";
		}else {
			model.addAttribute("UpdateProfile", new UpdateProfile());
			return "user/profile"; //프로필 폼 이동
		}		
	}
	
	
	//프로필 수정
	@PostMapping("/updateProfile")
	public String updateProfile (@Valid @ModelAttribute("UpdateProfile") UpdateProfile updateProfile,
								 @RequestParam("profileImg") String profileImg,
						 		 @RequestParam("imgUpload") MultipartFile imgFile, HttpSession session,
						 		 BindingResult bindingResult, Model model) {
		
		log.info("UserController의 updateProfile 메소드 실행");
		
		if (!updateProfile.getPass().isBlank()) {			
			//비밀번호 재확인 서버단에서 한번 더 검증
		    if (!updateProfile.getPass().equals(updateProfile.getPassCheck())) {
		        bindingResult.rejectValue("passCheck", "error.passCheck", "パスワードが一致しません。");
		        return "user/profile";
		    }
		}
		
		//서버단에서 유효성 검사
	    if (bindingResult.hasErrors()) {
	    	model.addAttribute("submitted", true);
	        return "user/profile"; 
	    }
	    
	    User user = (User) session.getAttribute("user");
	    
	    //프로필 업데이트
	    User updatedUser = userService.updateUserProfile(user.getId(), updateProfile, imgFile, profileImg);
	    log.info("✔ 사용자 정보 DB 업데이트 완료");
	    
	    //세션 정보 업데이트
	    session.setAttribute("user", updatedUser);
	    
	    model.addAttribute("updateMsg", "プロフィールを編集しました。");
	         	    
	    return "user/profile"; 
	    
	} //updateProfile
	
	
	@GetMapping("/test")
	public String test(Model model) {
	    return "user/test"; 
	}
		
	
	
	
}




