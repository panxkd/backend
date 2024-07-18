package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/api/upload")
public class UploadController {

    @Value("${upload.path}")
    private String uploadPath;

    @Value("${upload.url.prefix}")
    private String urlPrefix;

    public UploadController() {}

    @PostMapping("/image")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("File is empty");
        }

        try {
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String filename = UUID.randomUUID().toString() + extension;

            // 使用绝对路径进行文件存储
            File dest = new File(uploadPath, filename);
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();
            }

            file.transferTo(dest);

            // 构建文件的访问路径
            String fileUrl = urlPrefix + filename;
            return ResponseEntity.ok(fileUrl);

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Failed to upload");
        }
    }

    @PostMapping("/images")
    public ResponseEntity<String[]> uploadImages(@RequestParam("files") MultipartFile[] files) {
        if (files.length == 0) {
            return ResponseEntity.badRequest().body(new String[]{"No files provided"});
        }

        try {
            String[] fileUrls = new String[files.length];
            for (int i = 0; i < files.length; i++) {
                MultipartFile file = files[i];
                String originalFilename = file.getOriginalFilename();
                String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
                String filename = UUID.randomUUID().toString() + extension;

                File dest = new File(uploadPath, filename);
                if (!dest.getParentFile().exists()) {
                    dest.getParentFile().mkdirs();
                }

                file.transferTo(dest);

                fileUrls[i] = urlPrefix + filename;
            }
            return ResponseEntity.ok(fileUrls);

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(new String[]{"Failed to upload"});
        }
    }
}
