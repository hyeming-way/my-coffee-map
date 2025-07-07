package com.mycoffeemap;

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
	
	public void sendVerificationEmail(String toEmail, String verificationUrl) {		
		
		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");
			
			helper.setTo(toEmail);		
			
			helper.setSubject("【MY COFFEE MAP】メール認証のお願い");	
			
			//helper.setFrom(new InternetAddress("your_email@gmail.com", "MY COFFEE MAP TEAM", "UTF-8"));
			helper.setFrom(new InternetAddress("gpal7356@gmail.com", "MY COFFEE MAP TEAM", "UTF-8"));
					
			String htmlContent = "<div style='font-family:sans-serif; font-size:14px;'>"
							   + "<p>MY COFFEE MAPにご登録いただきありがとうございます！</p><br>"
							   + "<p>下のリンクをクリックして、メールアドレスの認証を完了してください👇</p>"
							   + "<p><a href='" + verificationUrl + "' style='color:#0080ff;'>▶ メール認証リンクはこちら</a></p>"
							   + "<p>※ リンクの有効期限は <strong>24時間</strong> です。</p><br><hr>"
							   + "<p>ご不明な点がありましたら、気軽にご連絡ください ☕</p>"
							   + "<p>MY COFFEE MAP TEAM<br>"
							   + "<a href='http://localhost:8070/mycoffeemap'>▶ MY COFFEE MAP サイト リンク</a></p>"
							   + "</div>";

			helper.setText(htmlContent, true);
			
			mailSender.send(message);	
		} catch (Exception e) {
			log.error("⚠ 이메일 생성 실패");
			e.printStackTrace();
		}
		
	} //sendVerificationEmail
	

}
