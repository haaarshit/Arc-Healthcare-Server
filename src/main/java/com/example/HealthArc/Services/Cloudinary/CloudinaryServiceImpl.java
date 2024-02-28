package com.example.HealthArc.Services.Cloudinary;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryServiceImpl implements CloudinaryService{

    @Autowired
    private Cloudinary cloudinary;
    @Override
    public Map upload(String base64) throws IOException {
      return  this.cloudinary.uploader().upload(base64, ObjectUtils.emptyMap());
    }
}
