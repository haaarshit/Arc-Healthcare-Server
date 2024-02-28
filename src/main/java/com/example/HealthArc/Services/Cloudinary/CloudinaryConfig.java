package com.example.HealthArc.Services.Cloudinary;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {
    @Value("${cloudinary.cloud_name}")
    private String cloud_name;
    @Value("${cloudinary.api_key}")
    private String api_key;
    @Value("${cloudinary.api_secret}")
    private String api_secret;
    @Value("${cloudinary.CLOUDINARY_URL}")
    private String cloudinaryUrl;


//    public String uploadToCloudinary(String base64) throws IOException {
//        Cloudinary cloudinary = getCloudinary();
//        Map<?, ?> uploadResult = cloudinary.uploader().upload(base64, ObjectUtils.emptyMap());
//        return (String) uploadResult.get("url");
//    }

    @Bean
    public Cloudinary getCloudinary(){
        Map  config = new HashMap<>();
        config.put("cloud_name",cloud_name);
        config.put("api_key",api_key);
        config.put("api_secret",api_secret);
        return  new Cloudinary(config);
    }
}
