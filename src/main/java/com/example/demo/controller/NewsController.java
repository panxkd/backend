package com.example.demo.controller;

import com.example.demo.entity.News;
import com.example.demo.service.NewsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/news")
public class NewsController {
    @Autowired
    private NewsService newsService;

    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping
    public ResponseEntity<List<News>> getAllNews() {
        List<News> newsList = this.newsService.getAllNews();
        return ResponseEntity.ok(newsList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<News> getNewsById(@PathVariable Long id) {
        Optional<News> newsOptional = this.newsService.getNewsById(id);
        return newsOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<News> insertNews(@RequestBody News news) {
        this.newsService.insertNews(news);
        return ResponseEntity.ok(news);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNews(@PathVariable Long id) {
        this.newsService.deleteNews(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<News> updateNewsField(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        Optional<News> newsOptional = newsService.getNewsById(id);
        if (newsOptional.isPresent()) {
            News updatedNews = newsService.updateNewsField(newsOptional.get(), updates);
            return ResponseEntity.ok(updatedNews);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/upload")
    public ResponseEntity<String[]> uploadNewsImages(@PathVariable Long id, @RequestParam("files") MultipartFile[] files) {
        try {
            Optional<News> newsOptional = newsService.getNewsById(id);
            if (newsOptional.isPresent()) {
                News news = newsOptional.get();

                List<String> existingImages = news.getImageUrls() != null ? news.getImageUrls() : new ArrayList<>();
                String[] newImages = newsService.uploadNewsImages(files);
                for (String imageUrl : newImages) {
                    existingImages.add(imageUrl);
                }

                news.setImageUrls(existingImages);
                newsService.updateNews(news);

                return ResponseEntity.ok(newImages);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(new String[]{"上传失败"});
        }
    }

    @PostMapping("/{id}/updateFieldsAndImages")
    public ResponseEntity<News> updateNewsFieldsAndImages(
            @PathVariable Long id,
            @RequestParam(required = false) String updates,
            @RequestParam(value = "uploadFiles", required = false) MultipartFile[] uploadFiles,
            @RequestParam(value = "deleteImages", required = false) String[] deleteImages) {
        try {
            Map<String, Object> updatesMap = updates != null ? objectMapper.readValue(updates, Map.class) : null;
            News updatedNews = newsService.processNewsUpdate(id, updatesMap, uploadFiles, deleteImages);
            return ResponseEntity.ok(updatedNews);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }
}
