package com.example.demo.service;

import com.example.demo.entity.Product;
import com.example.demo.mapper.ProductMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Paths;
import java.util.*;

@Service
public class ProductService {

    @Autowired
    private ProductMapper productMapper;

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

    // 根据ID获取产品
    public Optional<Product> getProductById(Long id) {
        return Optional.ofNullable(productMapper.findById(id));
    }

    // 获取所有产品
    public List<Product> getAllProducts() {
        return productMapper.findAll();
    }

    // 根据分类名称获取产品
    public List<Product> getProductsByCategoryName(String categoryName) {
        return productMapper.findByCategoryName(categoryName);
    }

    // 插入新产品
    public void saveProduct(Product product) {
        productMapper.insert(product);
    }

    // 更新产品
    public void updateProduct(Product product) {
        productMapper.update(product);
    }

    // 删除产品
    public void deleteProduct(Long id) {
        productMapper.delete(id);
    }

    // 获取产品图片
    public String[] getProductImages(Long id) {
        Optional<Product> product = getProductById(id);
        if (product.isPresent()) {
            Product productData = product.get();
            String imagesJson = productData.getImages();
            try {
                return objectMapper.readValue(imagesJson, String[].class);
            } catch (Exception e) {
                e.printStackTrace();
                return new String[]{};
            }
        }
        return new String[]{};
    }

    // 上传产品图片
    public String[] uploadProductImages(MultipartFile[] files) throws IOException {
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

    // 删除产品图片
    public boolean deleteProductImage(Long productId, String imageUrl) {
        Optional<Product> productOptional = getProductById(productId);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            try {
                String[] existingImages = objectMapper.readValue(product.getImages(), String[].class);
                List<String> imageList = new ArrayList<>(Arrays.asList(existingImages));
                if (imageList.remove(imageUrl)) {
                    product.setImages(objectMapper.writeValueAsString(imageList.toArray(new String[0])));
                    productMapper.update(product);
                    // 删除文件
                    String filePath = absoluteUploadPath + File.separator + imageUrl.replace(urlPrefix, "");
                    File file = new File(filePath);
                    return file.delete();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    // 更新产品任意字段
    public Product updateProductField(Product product, Map<String, Object> updates) {
        if (updates != null) {
            updates.forEach((key, value) -> {

                switch (key) {
                    case "categoryId":
                        product.setCategoryId(Long.valueOf(value.toString()));
                        break;
                    case "name":
                        product.setName((String) value);
                        break;
                    case "description":
                        product.setDescription((String) value);
                        break;
                    case "price":
                        product.setPrice(new BigDecimal(value.toString()));
                        break;
                    case "images":
                        product.setImages((String) value);
                        break;
                    case "type":
                        product.setType((String) value);
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid field: " + key);
                }
            });
            productMapper.update(product);
        }
        return product;
    }

    // 处理产品图片上传、删除和更新任意字段
    public Product processProductUpdate(Long id, Map<String, Object> updates, MultipartFile[] uploadFiles, String[] deleteImages) throws IOException {
        Optional<Product> productOptional = getProductById(id);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();

            // 更新产品字段
            updateProductField(product, updates);

            // 上传新图片
            if (uploadFiles != null && uploadFiles.length > 0) {
                String[] newImages = uploadProductImages(uploadFiles);
                String[] existingImages = getProductImages(id);
                List<String> allImages = new ArrayList<>(Arrays.asList(existingImages));
                allImages.addAll(Arrays.asList(newImages));
                product.setImages(objectMapper.writeValueAsString(allImages.toArray(new String[0])));
                productMapper.update(product);
            }

            // 删除指定图片
            if (deleteImages != null && deleteImages.length > 0) {
                for (String imageUrl : deleteImages) {
                    deleteProductImage(id, imageUrl);
                }
            }

            return product;
        } else {
            throw new IllegalArgumentException("Product not found with id: " + id);
        }
    }
}
