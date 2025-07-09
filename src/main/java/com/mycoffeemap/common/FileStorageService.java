	package com.mycoffeemap.common;
	
	import java.nio.file.Files;
	import java.nio.file.Path;
	import java.util.UUID;
	import org.apache.commons.io.FilenameUtils;
	import org.springframework.stereotype.Service;
	import org.springframework.web.multipart.MultipartFile;
	import lombok.RequiredArgsConstructor;
	import lombok.extern.slf4j.Slf4j;
	
	@Slf4j
	@Service
	@RequiredArgsConstructor
	public class FileStorageService {
		
		private final FileStorageProperties props;
		
		public String storeFile(MultipartFile file, String uploadPath) {
			
			try {
				//원본 파일명에서 확장자 추출
				String ext = FilenameUtils.getExtension(file.getOriginalFilename());
				
				//UUID 기반으로 유니크 이름 생성
				String filename;		
				filename = UUID.randomUUID() + "." + ext;
				
				Path target = null;
				if(uploadPath.equals("profile")) {
					target = props.getProfileUploadPath().resolve(filename);
				}
				
				Files.createDirectories(target.getParent());
				file.transferTo(target);
				
				return filename;
				
			} catch (Exception e) {
				log.error("⚠ 파일 저장 오류 발생");
				e.printStackTrace();
				return null;
			}
			
	    }
		
	}
