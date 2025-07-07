package com.mycoffeemap.common;

import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

//파일 저장용 컴포넌트
@Component
public class FileStorageProperties {

	@Value("${file.upload-dir}")
	private String uploadDir;
	
	public Path getUploadPath() {
		return Paths.get(uploadDir).toAbsolutePath().normalize();
	}
	
}
