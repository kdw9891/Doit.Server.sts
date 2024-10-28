package com.uni.doit.upload.service;

import com.uni.doit.framework.file.ImageUploadUtil;
import com.uni.doit.framework.utils.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class ImageService extends BaseService {

    @Autowired
    private ImageUploadUtil imageUploadUtil;

    public ResponseEntity<?> uploadItemImage(Long itemId, String imageDescription, MultipartFile file) {
        Map<String, Object> param = new HashMap<>();
        
        try {
            // 파일을 원본 이름으로 저장하고 URL 또는 메시지 반환
            String result = imageUploadUtil.saveFileWithOriginalName(itemId, file);

            // 동일한 파일 이름이 있을 경우 메시지 반환
            if (result.startsWith("동일한 파일 이름")) {
                return ResponseEntity.status(409).body(result);
            }

            // 파라미터 설정
            param.put("item_id", itemId);
            param.put("image_url", result);
            param.put("image_description", imageDescription);

            return ResponseEntity.ok(jsonResultUtils.getJsonResult(getSession(), "ImageInsert.InsertItemImage", param, "Result"));
        } catch (IOException e) {
            return handleDatabaseError(e, "uploadItemImage");
        } catch (Exception e) {
            return handleDatabaseError(e, "uploadItemImage");
        }
    }
}
