package com.uni.doit.framework.file;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

@Service
public class FileUploadUtil {

    // 파일 저장 경로
    @Value("${file.upload-dir}")
    private String uploadDir;
    
    // 환경에 맞는 서버 URL
    @Value("${server.url}")
    private String serverUrl;

    // 파일 확장자 검증 (이미지와 그 외 파일 구분 없이 일반 파일 형식도 지원)
    public boolean isValidFile(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && !file.isEmpty();
    }

    // 무작위 문자열로 고유한 파일 이름 생성
    public String generateRandomFileName(String originalFileName) {
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String randomString = generateRandomString(16);  // 16자리의 무작위 문자열 생성
        return randomString + extension;
    }

    // 무작위 문자열 생성 (SecureRandom을 사용한 방법)
    private String generateRandomString(int length) {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[length];
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);  // URL-safe Base64로 인코딩
    }

    // 날짜별로 폴더 생성 후 파일 저장
    public String saveFileWithDate(Long itemId, MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IOException("빈 파일입니다.");
        }

        if (!isValidFile(file)) {
            throw new IOException("지원하지 않는 파일 형식입니다.");
        }

        // 오늘 날짜를 기준으로 년-월-일 폴더 경로 설정
        String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String directoryPath = uploadDir + "/" + currentDate;
        Path uploadPath = Paths.get(directoryPath);

        // 디렉토리가 존재하지 않으면 생성
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // 무작위 문자열로 고유한 파일 이름 생성
        String fileName = generateRandomFileName(file.getOriginalFilename());
        Path filePath = uploadPath.resolve(fileName);

        // 파일 저장
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return serverUrl + "/files/" + currentDate + "/" + fileName;
    }

    // 원본 파일 이름으로 파일 저장 (날짜별 폴더)
    public String saveFileWithOriginalNameAndDate(Long itemId, MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IOException("빈 파일입니다.");
        }

        if (!isValidFile(file)) {
            throw new IOException("지원하지 않는 파일 형식입니다.");
        }

        // 오늘 날짜를 기준으로 년-월-일 폴더 경로 설정
        String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String directoryPath = uploadDir + "/" + currentDate;
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

        return serverUrl + "/files/" + currentDate + "/" + fileName;
    }

    // 파일 삭제 메서드 (선택 사항)
    public void deleteFile(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        Files.deleteIfExists(path);
    }
}
