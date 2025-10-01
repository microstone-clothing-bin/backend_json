// 클라우디너리 연결 설정

package com.example.clothing_backend.global;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {

    // application-prod.properties에 설정한 cloudinary.url 값을 주입받음
    @Value("${cloudinary.url}")
    private String cloudinaryUrl;

    @Bean
    public Cloudinary cloudinary() {
        // 주입받은 URL로 Cloudinary 객체를 생성하여 Bean으로 등록
        return new Cloudinary(cloudinaryUrl);
    }
}