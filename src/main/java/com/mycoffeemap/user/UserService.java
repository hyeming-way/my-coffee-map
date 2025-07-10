package com.mycoffeemap.user;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import com.mycoffeemap.common.EmailService;
import com.mycoffeemap.common.FileStorageService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
	
	@Autowired
	UserRepository userRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private EmailService emailService;
	@Autowired
	private FileStorageService fileStorageService;
	
	
	
	//사용자에게 본인 인증 이메일 보내기
	@Transactional
    public void registerUser(JoinForm joinForm, MultipartFile imgFile) {

		log.info("UserService의 registerUser 메소드 실행");
		
		String storedFilename = null;
		if (!imgFile.isEmpty()) {
			storedFilename = fileStorageService.storeFile(imgFile, "profile");
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
	    	
	} //registerUser
	
	
	//이메일 중복 검사
    public boolean isEmailAvailable(String email) {
        return !userRepository.existsByEmail(email);
    }
    

    //닉네임 중복 검사
    public boolean isNickAvailable(String nick) {
        return !userRepository.existsByNick(nick);
    }
    
	
    //사용자 이메일 인증
    public String verifyUser(String token, Model model) {
    	
        Optional<User> optionalUser = userRepository.findByVerificationToken(token);

        //인증 링크가 유효하지 않을 경우
        if (optionalUser.isEmpty()) {
            model.addAttribute("message", "無効な認証リンクです。");
            return "user/verify-fail";
        }

        User user = optionalUser.get();

        //인증 링크가 만료 시간을 경과했을 경우
        if (user.getTokenExpiry().isBefore(LocalDateTime.now())) {
            model.addAttribute("message", "認証リンクの有効期限が切れています。");
            return "user/verify-fail";
        }

        //이메일 인증 완료 처리
        user.setEnabled(true);
        user.setVerificationToken(null);
        user.setTokenExpiry(null);
        userRepository.save(user);

        return "user/verify-success";
    } //verifyUser
	
	
    //로그인 처리
	public User login(String email, String pass, Model model) {

		User user = userRepository.findByEmailAndDeletedFalse(email);

		//이메일 또는 패스워드가 틀린 경우
		if (user == null || !passwordEncoder.matches(pass, user.getPass())) {
			model.addAttribute("loginError", "メールアドレスまたはパスワードが違います。");
			return null;
		}

		//이메일 인증을 하지 않은 경우
		if (!user.isEnabled()) {
			if (user.getTokenExpiry().isBefore(LocalDateTime.now())) { //이메일 토큰이 만료된 경우								
				//이메일 재전송
				resendVerification(user);
				model.addAttribute("loginError", "メール認証の有効期限が切れたため、認証メールを再送信しました。メールをご確認のうえ、認証を完了してください。");				
			} else {
				model.addAttribute("loginError", "メール認証がまだ完了していません。");
			}
			return null;
		}
		return user;
	} //login
	
	
	//본인 인증 이메일 재전송
	public void resendVerification(User user) {

        String newToken = UUID.randomUUID().toString();
        
        user.setVerificationToken(newToken);
        user.setTokenExpiry(LocalDateTime.now().plusHours(24));
        userRepository.save(user);
        
	    //본인 인증 이메일 재발송    
	    String verifyUrl = "http://localhost:8070/user/verify?token=" + newToken;
	    emailService.sendVerificationEmail(user.getEmail(), verifyUrl);
	    log.info("✉ 사용자 본인 인증 이메일 보내기 완료");
	    	    
	} //resendVerification
	
	
	//프로필 수정
	@Transactional
	public User updateUserProfile(Integer userId, UpdateProfile updateProfile, MultipartFile imgFile, String profileImg) {

	    User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("ユーザーが見つかりません。"));

	    if (updateProfile.getPass() != null && !updateProfile.getPass().isBlank()) {
	        user.setPass(passwordEncoder.encode(updateProfile.getPass()));
	    }

	    user.setNick(updateProfile.getNick());

	    if (!imgFile.isEmpty()) {
	        String storedFilename = fileStorageService.storeFile(imgFile, "profile");
	        user.setProfileImg(storedFilename);
	    } else if(profileImg != null && !profileImg.trim().isEmpty()) {
	    	user.setProfileImg(profileImg);
	    } else {
	    	user.setProfileImg(null);
	    }

	    return userRepository.save(user); 
	    
	} //updateUserProfile

	
	//유저 탈퇴처리
	public void deleteUser(Integer id) {
	    Optional<User> optionalUser = userRepository.findById(id);
	    
	    if (optionalUser.isPresent()) {
	        User user = optionalUser.get();
	        user.setDeleted(true);
	        user.setDeletedDate(LocalDateTime.now());
	        userRepository.save(user);
	    }		
	} //deleteUser

	
	//비밀번호 재설정
	public boolean updatePass(String email) {

		//이메일 조회
		User user = userRepository.findByEmailAndDeletedFalse(email);			
		if(user == null) return false;
		
		//사용자 본인 인증용 토큰 생성
		String token = UUID.randomUUID().toString();
    	
	    user.setVerificationToken(token);
	    user.setTokenExpiry(LocalDateTime.now().plusMinutes(30));  //비밀번호 재설정 30분 유효하도록 설정
	    
	    userRepository.save(user);
	    log.info("✔ 비밀번호 재설정 토큰 DB 저장 완료");
	    
	    //비밀번호 재설정 이메일 보내기	    
	    String verifyUrl = "http://localhost:8070/user/updatePass?token=" + token;
	    emailService.sendUpdatePassEmail(user.getEmail(), verifyUrl);
	    log.info("✉ 비밀번호 재설정 이메일 보내기 완료");
		
		return true;
	} //updatePass



	
	
	
	
	
	
	

}
