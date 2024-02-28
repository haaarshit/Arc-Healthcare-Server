package com.example.HealthArc.Services.Cloudinary;

import java.io.IOException;
import java.util.Map;

public interface CloudinaryService {
    public Map upload(String base64) throws IOException;
}
