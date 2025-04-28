package com.ecommerce.services.cloudinary;

import java.io.IOException;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.ecommerce.exceptionhandler.EntityDeletionException;
import com.ecommerce.exceptionhandler.EntityPushException;

@Service
public class CloudinaryService {
    private final Cloudinary cloudinary;

    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }
    
    public String uploadImage(MultipartFile file) throws IOException {
        System.out.println("Cloudinary: " + cloudinary.getUserAgent());

        @SuppressWarnings("unchecked")
		Map<String, Object> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
        if(!uploadResult.isEmpty()){
            return uploadResult.get("url").toString();
        }
        throw new EntityPushException("Failed to upload images to db");
    }
    public void deleteImageFromCloudinary(String publicId) {
        try {

            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            System.out.println("deleted in cloudinary");
        } catch (Exception e) {
            throw new EntityDeletionException("Failed to delete image from db");
        }
    }

    public String extractPublicIdFromUrl(String url) {
        try {
            String uploadSegment = "/upload/";
            int uploadIndex = url.indexOf(uploadSegment);
            if (uploadIndex == -1) throw new RuntimeException("Invalid Cloudinary URL format");
            String path = url.substring(uploadIndex + uploadSegment.length());
            return path.substring(path.indexOf("/") + 1, path.lastIndexOf('.'));
        } catch (Exception e) {
            throw new RuntimeException("Failed to extract publicId from URL: " + url);
        }
    }


}
