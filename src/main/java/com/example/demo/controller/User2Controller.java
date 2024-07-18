package com.example.demo.controller;

import com.example.demo.entity.User2;
import com.example.demo.service.User2Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/user")
public class User2Controller {

    private static final Logger logger = LoggerFactory.getLogger(User2Controller.class);

    @Autowired
    private User2Service userService;

    // 微信小程序登录接口
    @PostMapping("/login")
    public User2 wxLogin(@RequestBody Map<String, String> request) {
        String code = request.get("code");
        logger.info("接收到的code: {}", code);
        return userService.wxLogin(code);
    }
}
