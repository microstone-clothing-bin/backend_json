package com.example.clothing_backend.global;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ImageUploadService {

    private final Cloudinary cloudinary;

    public String uploadImage(MultipartFile imageFile) throws IOException {
        if (imageFile == null || imageFile.isEmpty()) {
            return null; // 이미지가 없으면 null 반환
        }

        Map uploadResult = cloudinary.uploader().upload(imageFile.getBytes(), ObjectUtils.emptyMap());
        return uploadResult.get("url").toString();
    }

    public List<String> uploadImages(List<MultipartFile> imageFiles) throws IOException {

        List<String> imageUrls = new ArrayList<>();

        // 이미지가 없거나 비어있으면 빈 리스트 반환
        if (imageFiles == null || imageFiles.isEmpty()) {
            return imageUrls;
        }

        // 반복문 돌면서 기존 uploadImage 메소드 호출
        for (MultipartFile file : imageFiles) {
            String imageUrl = uploadImage(file);
            if (imageUrl != null) {
                imageUrls.add(imageUrl);
            }
        }

        return imageUrls;
    }
}