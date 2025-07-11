package com.mycoffeemap.common;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {
	
	private final JavaMailSender mailSender;
	
	
	//회원가입 본인 인증 이메일
	public void sendVerificationEmail(String toEmail, String verificationUrl) {		
		
		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");
			
			helper.setTo(toEmail);		
			
			helper.setSubject("【MY COFFEE MAP】メール認証のお願い");	
			
			//helper.setFrom(new InternetAddress("your_email@gmail.com", "MY COFFEE MAP TEAM", "UTF-8"));
			helper.setFrom(new InternetAddress("gpal7356@gmail.com", "MY COFFEE MAP TEAM", "UTF-8"));
					
			String htmlContent = "<div style='width: 500px; font-family: sans-serif; font-size: 14px; margin: 50px 0; padding: 10px;'>"
							   + "<p>MY COFFEE MAPにご登録いただきありがとうございます！</p><br>"
							   + "<p>下のリンクをクリックして、メールアドレスの認証を完了してください👇</p><br>"
							   + "<p style='display: flex; justify-content: center;'><a href='" + verificationUrl + "' style='display:inline-block; padding:10px 20px; "
							   + "color: #ff6c57; background-color:#fdeae0; text-decoration:none; border-radius:5px;'>"							   
							   + "▶ メール認証リンクはこちら</a></p>"
							   + "<p style='display: flex; justify-content: center;'>※ リンクの有効期限は <strong>24時間</strong> です。</p><br><hr>"
							   + "<p>ご不明な点がありましたら、気軽にご連絡ください ☕</p>"
							   + "<a href='http://localhost:8070/mycoffeemap' style='text-decoration: none; color: #ff6c57;'>▶ MY COFFEE MAP サイト リンク</a>"
							   + "</div>";
		    		 
			helper.setText(htmlContent, true);
		    	
			mailSender.send(message);	
		} catch (Exception e) {
			log.error("⚠ 이메일 생성 실패");
			e.printStackTrace();
		}

	} //sendVerificationEmail
	
	
	//비밀번호 재설정 이메일
	public void sendUpdatePassEmail(String toEmail, String verificationUrl) {	
		
		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");
			
			helper.setTo(toEmail);		
			
			helper.setSubject("【MY COFFEE MAP】パスワード再設定のご案内");	
			
			//helper.setFrom(new InternetAddress("your_email@gmail.com", "MY COFFEE MAP TEAM", "UTF-8"));
			helper.setFrom(new InternetAddress("gpal7356@gmail.com", "MY COFFEE MAP TEAM", "UTF-8"));
			
			String htmlContent = "<div style='width: 500px; font-family: sans-serif; font-size: 14px; margin: 50px 0; padding: 10px;'>"
							   + "<p>MY COFFEE MAPをご利用いただきありがとうございます。</p><br>"
							   + "<p>以下のリンクをクリックして、パスワードの再設定を行ってください👇</p><br>"
							   + "<p style='display: flex; justify-content: center;'><a href='" + verificationUrl + "' style='display:inline-block; padding:10px 20px; "
							   + "color: #ff6c57; background-color:#fdeae0; text-decoration:none; border-radius:5px;'>"
							   + "▶ パスワードを再設定する</a></p>"
							   + "<p style='display: flex; justify-content: center;'>※ このリンクの有効期限は <strong>1時間</strong> です。</p><br><hr>"
							   + "<p>ご不明な点がありましたら、お気軽にお問い合わせください ☕</p>"
							   + "<a href='http://localhost:8070/mycoffeemap' style='text-decoration: none; color: #ff6c57;'>▶ MY COFFEE MAP サイトリンク</a>"
							   + "</div>";
			
			helper.setText(htmlContent, true);
			
			mailSender.send(message);	
		} catch (Exception e) {
			log.error("⚠ 이메일 생성 실패");
			e.printStackTrace();
		}		
	
	} //sendUpdatePassEmail
	
	
	
	

}
