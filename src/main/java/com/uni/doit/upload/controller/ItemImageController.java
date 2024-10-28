package com.uni.doit.upload.controller;

import com.uni.doit.upload.service.ImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "upload")
@RestController
@RequestMapping("/upload")
public class ItemImageController {

    @Autowired
    private ImageService imageService;

    @Operation(
        summary = "아이템 이미지 업로드",
        description = "아이템별 이미지를 원본 파일 이름으로 업로드하고, DB에 정보를 저장합니다."
    )
    @PostMapping(value = "/itemimage", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> uploadItemImage(
        @RequestParam("item_id") Long itemId,
        @RequestParam("image_description") String imageDescription,
        @RequestParam("file") MultipartFile file
    ) {
        // ImageService의 메서드 호출
        return imageService.uploadItemImage(itemId, imageDescription, file);
    }
}
