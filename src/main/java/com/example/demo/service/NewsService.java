package com.example.demo.service;

import com.example.demo.entity.News;
import com.example.demo.mapper.NewsMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

@Service
public class NewsService {

    @Autowired
    private NewsMapper newsMapper;

    @Value("${upload.path}")
    private String uploadPath;

    @Value("${upload.url.prefix}")
    private String urlPrefix;

    @Autowired
    private ObjectMapper objectMapper;

    private String absoluteUploadPath;

    @PostConstruct
    public void init() {
        // 将相对路径解析为绝对路径
        absoluteUploadPath = Paths.get(uploadPath).toAbsolutePath().toString();
        // 检查并创建上传目录
        File uploadDir = new File(absoluteUploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
    }

    public Optional<News> getNewsById(Long id) {
        return Optional.ofNullable(newsMapper.getNewsById(id));
    }

    public List<News> getAllNews() {
        return newsMapper.getAllNews();
    }

    public void insertNews(News news) {
        newsMapper.insertNews(news);
    }

    public void updateNews(News news) {
        newsMapper.updateNews(news);
    }

    public void deleteNews(Long id) {
        newsMapper.deleteNews(id);
    }

    public String[] uploadNewsImages(MultipartFile[] files) throws IOException {
        String[] fileUrls = new String[files.length];
        for (int i = 0; i < files.length; i++) {
            MultipartFile file = files[i];
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String filename = UUID.randomUUID().toString() + extension;

            // 使用绝对路径进行文件存储
            File dest = new File(absoluteUploadPath, filename);
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();
            }

            file.transferTo(dest);

            // 构建文件的访问路径
            fileUrls[i] = urlPrefix + filename;
        }
        return fileUrls;
    }

    public boolean deleteNewsImage(Long newsId, String imageUrl) {
        Optional<News> newsOptional = getNewsById(newsId);
        if (newsOptional.isPresent()) {
            News news = newsOptional.get();
            List<String> imageList = news.getImageUrls() != null ? news.getImageUrls() : new ArrayList<>();
            if (imageList.remove(imageUrl)) {
                news.setImageUrls(imageList);
                newsMapper.updateNews(news);
                String filePath = absoluteUploadPath + File.separator + imageUrl.replace(urlPrefix, "");
                File file = new File(filePath);
                return file.delete();
            }
        }
        return false;
    }

    public News updateNewsField(News news, Map<String, Object> updates) {
        updates.forEach((key, value) -> {
            switch (key) {
                case "title":
                    news.setTitle((String) value);
                    break;
                case "content":
                    news.setContent((String) value);
                    break;
                case "type":
                    news.setType((String) value);
                    break;
                case "imageUrls":
                    List<String> imageUrls = objectMapper.convertValue(value, List.class);
                    news.setImageUrls(imageUrls);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid field: " + key);
            }
        });
        newsMapper.updateNews(news);
        return news;
    }

    public News processNewsUpdate(Long id, Map<String, Object> updates, MultipartFile[] uploadFiles, String[] deleteImages) throws IOException {
        Optional<News> newsOptional = getNewsById(id);
        if (newsOptional.isPresent()) {
            News news = newsOptional.get();

            // 更新新闻字段
            if (updates != null) {
                updateNewsField(news, updates);
            }

            // 上传新图片
            if (uploadFiles != null && uploadFiles.length > 0) {
                String[] newImages = uploadNewsImages(uploadFiles);
                List<String> allImages = news.getImageUrls() != null ? new ArrayList<>(news.getImageUrls()) : new ArrayList<>();
                allImages.addAll(Arrays.asList(newImages));
                news.setImageUrls(allImages);
                newsMapper.updateNews(news);
            }

            // 删除指定图片
            if (deleteImages != null && deleteImages.length > 0) {
                for (String imageUrl : deleteImages) {
                    deleteNewsImage(id, imageUrl);
                }
            }

            return news;
        } else {
            throw new IllegalArgumentException("News not found with id: " + id);
        }
    }
}
