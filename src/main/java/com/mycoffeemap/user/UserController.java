package com.mycoffeemap.user;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
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
import org.springframework.web.multipart.MultipartFile;
import com.mycoffeemap.common.EmailService;
import com.mycoffeemap.common.FileStorageService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	UserRepository userRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private EmailService emailService;
	@Autowired
	private FileStorageService fileStorageService;
	
	
	//로그인 화면 보여주기
	@GetMapping("/login")
	public String login() {
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
	    
	    String storedFilename = null;
	    if (!imgFile.isEmpty()) {
	    	storedFilename = fileStorageService.storeFile(imgFile);
	    }
	    
	    //사용자 본인 인증용 토큰 생성
	    String token = UUID.randomUUID().toString();
	    
	    //user 객체 생성 후 DB 저장
	    User user = new User();
	    user.setEmail(joinForm	.getEmail());
	    user.setPass(passwordEncoder.encode(joinForm.getPass()));
	    user.setNick(joinForm.getNick());
	    user.setProfileImg(storedFilename);
	    user.setVerificationToken(token);
	    user.setTokenExpiry(LocalDateTime.now().plusHours(24));  //인증 메일 24시간 유효하도록 설정
	    user.setEnabled(false);
	    
	    userRepository.save(user);
	    log.info("✔ 사용자 정보 DB 저장 완료");
	    	    
	    //본인 인증 이메일 보내기	    
	    String verifyUrl = "http://localhost:8070/user/verify?token=" + token;
	    emailService.sendVerificationEmail(user.getEmail(), verifyUrl);
	    log.info("✉ 사용자 본인 인증 이메일 보내기 완료");
	    	    
	    return "user/mail-sended";
	    
	} //submit
	
	
	//사용자 이메일 인증
	@GetMapping("/verify")
	public String verifyUser(@RequestParam("token") String token, Model model) {
		
		Optional<User> optionalUser = userRepository.findByVerificationToken(token);
		
		//인증 링크가 유효하지 않을 경우
		if (optionalUser.isEmpty()) {
			model.addAttribute("message", "無効な認証リンクです。");
			return "user/verify-fail"; //인증 실패 뷰 반환
		}
		
		User user = optionalUser.get();
		
		//인증 링크가 만료 시간을 경과했을 경우
		if (user.getTokenExpiry().isBefore(LocalDateTime.now())) {		
			model.addAttribute("message", "認証リンクの有効期限が切れています。");
			return "user/verify-fail"; //인증 실패 뷰 반환
		}
		
		//이메일 인증 완료 처리
		user.setEnabled(true);
		user.setVerificationToken(null);
		user.setTokenExpiry(null);
		userRepository.save(user);
		
		return "user/verify-success"; //인증 성공 뷰 반환
		
	} //verifyUser
	
	
	//로그인 처리
	@PostMapping("/login.do")
	public String doLogin (@RequestParam("email") String email, @RequestParam("pass") String pass,
						   HttpSession session, Model model) {
		
		User user = userRepository.findByEmail(email);
		
		//이메일 또는 패스워드가 틀린 경우
		if (user == null || !passwordEncoder.matches(pass, user.getPass())) {
			model.addAttribute("loginError", "メールアドレスまたはパスワードが違います。");
			return "user/login";
		}
		
		//이메일 인증을 하지 않은 경우
		if (!user.isEnabled()) {
			model.addAttribute("loginError", "メール認証がまだ完了していません。");
			return "user/login";
		}
		
		session.setAttribute("user", user);
			
		return "fragments/main-content";
		
	} //doLogin
	
	
	//로그아웃 처리
	@GetMapping("/logout.do")
	public String logout (HttpSession session) {
		log.info("로그아웃처리 중..........");
	    session.invalidate(); 
		log.info("로그아웃처리 완료");
		return "fragments/main-content";
	}
	
	
	
	
	
	
	
	
	
	@GetMapping("/test")
	public String test(Model model) {
	    return "user/test"; 
	}
	
	
	
	
	
	
}




