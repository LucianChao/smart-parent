package com.javaweb.interceptor;

import com.javaweb.entity.EmpLoginInfo;
import com.javaweb.utils.JWTUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Arrays;
import java.util.List;

/**
 * 登录校验拦截器
 */
@Slf4j
@Component
public class LoginCheckInterceptor implements HandlerInterceptor {
    // 白名单路径 - 无需登录即可访问
    private static final List<String> WHITE_LIST = Arrays.asList("/login", "/autoAnalysis", "/detailedAnalysis");

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1. 获取请求url。
        String uri = request.getRequestURI();
        log.info("拦截到请求: {}", uri);

        // 2. 判断请求url是否在白名单中，如果包含，放行。
        for (String whitePath : WHITE_LIST) {
            if (uri.contains(whitePath)) {
                log.info("路径 {} 在白名单中，放行", whitePath);
                return true;
            }
        }

        // 3. 获取请求头中的令牌（token）。
        String token = request.getHeader("token");

        // 4. 判断令牌是否存在，如果不存在，响应401。
        if (token == null || token.isEmpty()) {
            log.warn("请求头中缺少token");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write("{\"code\": 0, \"msg\": \"未提供认证令牌\"}");
            return false;
        }

        // 5. 解析token，如果解析失败，响应401。
        try {
            Claims claims = JWTUtils.parseJwt(token);
            log.info("令牌解析成功: {}", claims);

            // 将用户信息存储到request中，供后续使用
            request.setAttribute("claims", claims);

            // 检查用户角色（如果有需要的话）
            String role = (String) claims.get("role");
            if (!checkPermission(uri, role)) {
                log.warn("用户角色 {} 无权访问路径 {}", role, uri);
                response.setStatus(HttpStatus.FORBIDDEN.value());
                response.getWriter().write("{\"code\": 0, \"msg\": \"权限不足\"}");
                return false;
            }
        } catch (Exception e) {
            log.error("令牌解析失败: {}", e.getMessage());
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write("{\"code\": 0, \"msg\": \"令牌无效或已过期\"}");
            return false;
        }

        // 6. 放行。
        return true;
    }

    /**
     * 检查用户权限
     * @param uri 请求路径
     * @param role 用户角色
     * @return 是否有权限
     */
    private boolean checkPermission(String uri, String role) {
        // 对于AI分析接口，允许所有已登录用户访问
        if (uri.contains("/api/autoAnalysis") || uri.contains("/api/detailedAnalysis")) {
            return true;
        }

        // 其他接口的权限控制逻辑可以在这里添加
        // 例如：管理员才能访问某些敏感接口
        // if (uri.startsWith("/admin") && !"ADMIN".equals(role)) {
        //     return false;
        // }

        return true; // 默认允许访问
    }
}