package com.uni.doit.framework.file;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class ImageUploadUtil {

	// 파일 저장 경로
    @Value("${file.upload-dir}")
    private String uploadDir;
    
    // 환경에 맞는 서버 URL
    @Value("${server.url}")
    private String serverUrl;

    // 파일 확장자 검증
    public boolean isValidImageFile(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && (contentType.equals("image/jpeg") ||
                                       contentType.equals("image/png") ||
                                       contentType.equals("image/gif") ||
                                       contentType.equals("image/bmp") ||
                                       contentType.equals("image/tiff") ||
                                       contentType.equals("image/webp"));
    }

    // UUID로 고유한 파일 이름 생성
    public String generateUUIDFileName(String originalFileName) {
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        return UUID.randomUUID().toString() + extension;
    }

    // UUID 방식으로 파일 저장
    public String saveFileWithUUID(Long itemId, MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IOException("빈 파일입니다.");
        }

        if (!isValidImageFile(file)) {
            throw new IOException("지원하지 않는 파일 형식입니다.");
        }

        // 파일 저장 경로 설정
        String directoryPath = uploadDir + itemId;
        Path uploadPath = Paths.get(directoryPath);

        // 디렉토리가 존재하지 않으면 생성
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // UUID로 고유한 파일 이름 생성
        String fileName = generateUUIDFileName(file.getOriginalFilename());
        Path filePath = uploadPath.resolve(fileName);

        // 파일 저장
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return serverUrl + "/images/" + itemId + "/" + fileName;
    }

    // 원본 파일 이름으로 파일 저장
    public String saveFileWithOriginalName(Long itemId, MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IOException("빈 파일입니다.");
        }

        if (!isValidImageFile(file)) {
            throw new IOException("지원하지 않는 파일 형식입니다.");
        }

        // 파일 저장 경로 설정
        String directoryPath = uploadDir + itemId;
        Path uploadPath = Paths.get(directoryPath);

        // 디렉토리가 존재하지 않으면 생성
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // 원본 파일 이름 사용
        String fileName = file.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);

        // 파일 저장
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return "/images/" + itemId + "/" + fileName;
    }

    // 파일 삭제 메서드 (선택 사항)
    public void deleteFile(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        Files.deleteIfExists(path);
    }
}

