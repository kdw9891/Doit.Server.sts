package com.uni.doit.framework.file;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * 이미지 파일 업로드를 위한 유틸리티 서비스 클래스
 * 다양한 방식으로 이미지 파일을 저장하고 관리하는 메서드 제공
 */
@Service
public class ImageUploadUtil {

    // 파일 업로드 디렉토리 경로 (application.properties에서 주입)
    @Value("${file.upload-dir}")
    private String uploadDir;
    
    // 서버 URL (환경별 설정 지원, application.properties에서 주입)
    @Value("${server.url}")
    private String serverUrl;

    /**
     * 업로드된 파일의 MIME 타입을 검증하여 이미지 파일인지 확인
     * 
     * @param file 검증할 멀티파트 파일 객체
     * @return 지원되는 이미지 형식이면 true, 그렇지 않으면 false
     */
    public boolean isValidImageFile(MultipartFile file) {
        // 지원되는 이미지 MIME 타입 목록 (JPEG, PNG, GIF, BMP, TIFF, WebP)
        String contentType = file.getContentType();
        return contentType != null && (contentType.equals("image/jpeg") ||
                                       contentType.equals("image/png") ||
                                       contentType.equals("image/gif") ||
                                       contentType.equals("image/bmp") ||
                                       contentType.equals("image/tiff") ||
                                       contentType.equals("image/webp"));
    }

    /**
     * 원본 파일 이름을 기반으로 UUID를 포함한 고유한 파일 이름 생성
     * 
     * @param originalFileName 원본 파일 이름
     * @return UUID가 추가된 고유한 파일 이름
     */
    public String generateUUIDFileName(String originalFileName) {
        // 원본 파일의 확장자 추출
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        // UUID와 원본 확장자를 결합하여 고유한 파일 이름 생성
        return UUID.randomUUID().toString() + extension;
    }

    /**
     * UUID 방식으로 이미지 파일 저장
     * 고유한 파일 이름 생성, 파일 형식 검증, 디렉토리 생성 등 수행
     * 
     * @param itemId 아이템 식별자 (하위 디렉토리 생성에 사용)
     * @param file 업로드할 멀티파트 파일 객체
     * @return 저장된 파일의 전체 URL
     * @throws IOException 파일 저장 중 오류 발생 시
     */
    public String saveFileWithUUID(Long itemId, MultipartFile file) throws IOException {
        // 빈 파일 체크
        if (file.isEmpty()) {
            throw new IOException("빈 파일입니다.");
        }

        // 이미지 파일 형식 검증
        if (!isValidImageFile(file)) {
            throw new IOException("지원하지 않는 파일 형식입니다.");
        }

        // 아이템 ID 기반 디렉토리 경로 설정
        String directoryPath = uploadDir + itemId;
        Path uploadPath = Paths.get(directoryPath);

        // 디렉토리 존재하지 않을 경우 생성
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // UUID로 고유한 파일 이름 생성
        String fileName = generateUUIDFileName(file.getOriginalFilename());
        Path filePath = uploadPath.resolve(fileName);

        // 파일 저장 (이미 존재하는 파일은 대체)
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // 저장된 파일의 전체 URL 반환
        return serverUrl + "/images/" + itemId + "/" + fileName;
    }

    /**
     * 원본 파일 이름 그대로 이미지 파일 저장
     * UUID 대신 원본 파일 이름 사용
     * 
     * @param itemId 아이템 식별자 (하위 디렉토리 생성에 사용)
     * @param file 업로드할 멀티파트 파일 객체
     * @return 저장된 파일의 전체 URL
     * @throws IOException 파일 저장 중 오류 발생 시
     */
    public String saveFileWithOriginalName(Long itemId, MultipartFile file) throws IOException {
        // 빈 파일 체크
        if (file.isEmpty()) {
            throw new IOException("빈 파일입니다.");
        }

        // 이미지 파일 형식 검증
        if (!isValidImageFile(file)) {
            throw new IOException("지원하지 않는 파일 형식입니다.");
        }

        // 아이템 ID 기반 디렉토리 경로 설정
        String directoryPath = uploadDir + itemId;
        Path uploadPath = Paths.get(directoryPath);

        // 디렉토리 존재하지 않을 경우 생성
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // 원본 파일 이름 그대로 사용
        String fileName = file.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);

        // 파일 저장 (이미 존재하는 파일은 대체)
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // 저장된 파일의 전체 URL 반환
        return serverUrl + "/images/" + itemId + "/" + fileName;
    }

    /**
     * 날짜별 폴더에 이미지 파일 저장
     * 현재 날짜를 기준으로 하위 디렉토리 생성
     * 
     * @param file 업로드할 멀티파트 파일 객체
     * @return 저장된 파일의 전체 URL
     * @throws IOException 파일 저장 중 오류 발생 시
     */
    public String saveFileWithDate(MultipartFile file) throws IOException {
        // 빈 파일 체크
        if (file.isEmpty()) {
            throw new IOException("빈 파일입니다.");
        }

        // 이미지 파일 형식 검증
        if (!isValidImageFile(file)) {
            throw new IOException("지원하지 않는 파일 형식입니다.");
        }

        // 현재 날짜 기준 'yyyy-MM-dd' 형식의 폴더 경로 설정
        String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String directoryPath = uploadDir + "/" + currentDate;
        Path uploadPath = Paths.get(directoryPath);

        // 디렉토리 존재하지 않을 경우 생성
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // UUID로 고유한 파일 이름 생성
        String fileName = generateUUIDFileName(file.getOriginalFilename());
        Path filePath = uploadPath.resolve(fileName);

        // 파일 저장 (이미 존재하는 파일은 대체)
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // 저장된 파일의 전체 URL 반환
        return serverUrl + "/images/" + currentDate + "/" + fileName;
    }

    /**
     * 지정된 파일 경로의 파일 삭제
     * 파일이 존재하지 않아도 예외 발생하지 않음
     * 
     * @param filePath 삭제할 파일의 전체 경로
     * @throws IOException 파일 삭제 중 오류 발생 시
     */
    public void deleteFile(String filePath) throws IOException {
        // 주어진 파일 경로를 Path 객체로 변환
        Path path = Paths.get(filePath);
        // 파일 존재 시 삭제 (존재하지 않아도 예외 발생하지 않음)
        Files.deleteIfExists(path);
    }
}