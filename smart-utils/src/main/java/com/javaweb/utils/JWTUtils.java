package com.javaweb.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Date;
import java.util.Map;

public class JWTUtils {
    //自定义密钥
    private static String secret = "SVRIRUlNQQ";//密钥
    //过期时间
    private static long expire = 43200000L;//12h

    /**
     * 生成JWT令牌
     * @param claims
     * @return
     */
    public static String generateJwt(Map<String, Object> claims) {
        String jwt = Jwts.builder()
                .addClaims(claims)
                .signWith(SignatureAlgorithm.HS256, secret)
                .setExpiration(new Date(System.currentTimeMillis() + expire))
                .compact();
        return jwt;
    }

    /**
     * 解析JWT令牌
     * @param jwt Jwt令牌
     * @return JWT第二部分负载 payload 中的内容
     */
    public static Claims parseJwt(String jwt){
        if (jwt == null || jwt.isEmpty()) {
            return null;
        }
        Claims claims = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(jwt)
                .getBody();
        return claims;
    }
    
    /**
     * 获取当前用户角色
     * @return 当前用户的角色
     */
    public static String getCurrentUserRole() {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpServletRequest request = attributes.getRequest();
            String token = request.getHeader("token");
            if (token != null && !token.isEmpty()) {
                Claims claims = parseJwt(token);
                if (claims != null) {
                    return (String) claims.get("role");
                }
            }
        } catch (Exception e) {
            // 忽略异常，返回null
        }
        return null;
    }
    
    /**
     * 获取当前用户ID
     * @return 当前用户的ID
     */
    public static Integer getCurrentUserId() {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpServletRequest request = attributes.getRequest();
            String token = request.getHeader("token");
            if (token != null && !token.isEmpty()) {
                Claims claims = parseJwt(token);
                if (claims != null) {
                    return (Integer) claims.get("id");
                }
            }
        } catch (Exception e) {
            // 忽略异常，返回null
        }
        return null;
    }
}