package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/api/endpoint")
    public String getEndpoint() {
        return "Hello from backend!";
    }
}
