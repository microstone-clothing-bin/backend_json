package com.example.clothing_backend.global;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ImageUploadService {

    private final Cloudinary cloudinary;

    public String uploadImage(MultipartFile imageFile) throws IOException {
        if (imageFile == null || imageFile.isEmpty()) {
            return null;
        }

        // Cloudinary에 이미지 업로드
        Map uploadResult = cloudinary.uploader().upload(imageFile.getBytes(), ObjectUtils.emptyMap());

        // 업로드된 이미지의 URL 반환 (보안 URL 사용)
        return uploadResult.get("secure_url").toString();
    }
}