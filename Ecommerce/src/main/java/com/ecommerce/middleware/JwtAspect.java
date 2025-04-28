package com.ecommerce.middleware;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.ecommerce.exceptionhandler.UnauthorizedException;
import com.ecommerce.util.JwtUtil;

import jakarta.servlet.http.HttpServletRequest;

@Aspect
@Component
@Order(0)
public class JwtAspect {
    private JwtUtil jwtUtil;
    
    public JwtAspect(JwtUtil jwtUtil) {
		this.jwtUtil = jwtUtil;
	}

	private static final ThreadLocal<String> currentUserId = new ThreadLocal<>();
    @Before("@annotation(AuthRequired)")
    public void validateToken(JoinPoint joinPoint) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = jwtUtil.resolveToken(request);

        if (token != null && jwtUtil.validateToken(token)) {
            String userId = jwtUtil.extractUserId(token);
            System.out.println("USERID: "+userId);
            currentUserId.set(userId);
        } else {
            throw new UnauthorizedException("Invalid JWT Token");
        }
    }

    public static String getCurrentUserId() {
        return currentUserId.get();
    }

    @After("@annotation(AuthRequired)")
    public void clear() {
        currentUserId.remove();
    }

}
