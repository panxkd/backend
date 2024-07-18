package com.example.demo.service;

import com.example.demo.entity.User2;
import com.example.demo.mapper.User2Mapper;
import com.example.demo.entity.JwtUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
public class User2Service {

    private static final Logger logger = LoggerFactory.getLogger(User2Service.class);

    @Autowired
    private User2Mapper userMapper;

    // 微信小程序的 AppID 和 AppSecret
    private static final String APPID = "wx99c05300a6cf4ce5";  // 替换为你的AppID
    private static final String SECRET = "fac2bb25af29e95a3805e363a07657e3";  // 替换为你的AppSecret
    // 微信小程序登录 URL
    private static final String WX_LOGIN_URL = "https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code";

    // 微信小程序登录方法
    public User2 wxLogin(String code) {
        try {
            RestTemplate restTemplate = new RestTemplate();

            // 配置RestTemplate接受text/plain类型的响应
            restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));

            String url = String.format(WX_LOGIN_URL, APPID, SECRET, code);
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            if (responseEntity.getStatusCode() != HttpStatus.OK || responseEntity.getBody() == null) {
                logger.error("从微信API获取openid失败，响应: {}", responseEntity);
                throw new RuntimeException("从微信API获取openid失败");
            }

            String responseBody = responseEntity.getBody();
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, String> response = objectMapper.readValue(responseBody, HashMap.class);

            String openid = response.get("openid");
            String sessionKey = response.get("session_key");

            logger.info("获取到的openid: {}", openid);

            User2 user = userMapper.selectUserByOpenid(openid);
            if (user == null) {
                // 如果用户不存在，创建新用户
                user = new User2();
                user.setOpenid(openid);
                user.setSessionKey(sessionKey);
                userMapper.insertUser(user);
                logger.info("新用户创建成功: {}", user);
            } else {
                // 如果用户已存在，更新用户信息
                user.setSessionKey(sessionKey);
                userMapper.updateUser(user);
                logger.info("用户信息更新成功: {}", user);
            }

            // 生成JWT token
            String token = JwtUtils.generateToken(openid);
            user.setToken(token);

            logger.info("生成的JWT token: {}", token);

            return user;
        } catch (Exception e) {
            logger.error("微信登录过程中出错", e);
            throw new RuntimeException("微信登录过程中出错", e);
        }
    }
}
