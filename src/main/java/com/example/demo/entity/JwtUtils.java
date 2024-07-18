package com.example.demo.entity;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.xml.bind.DatatypeConverter;
import java.util.Date;

public class JwtUtils {

    private static final String SECRET_KEY = "fac2bb25af29e95a3805e363a07657e3";  // 请替换为你的秘钥
    private static final long EXPIRATION_TIME = 864_000_000;  // 10 days

    public static String generateToken(String openid) {
        return Jwts.builder()
                .setSubject(openid)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, DatatypeConverter.parseBase64Binary(SECRET_KEY))
                .compact();
    }

    public static Claims getClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(SECRET_KEY))
                .parseClaimsJws(token)
                .getBody();
    }

    public static boolean isTokenExpired(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.getExpiration().before(new Date());
    }
}
