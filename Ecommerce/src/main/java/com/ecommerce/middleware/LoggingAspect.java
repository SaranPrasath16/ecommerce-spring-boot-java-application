package com.ecommerce.middleware;

import java.util.Arrays;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Order(1)
public class LoggingAspect {
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Around("execution(* com.ecommerce.controller.*.*(..))")
    public Object logController(ProceedingJoinPoint joinPoint) throws Throwable {
        return logMethodExecution(joinPoint, "Controller");
    }

    @Around("execution(* com.ecommerce.service.*.*(..))")
    public Object logService(ProceedingJoinPoint joinPoint) throws Throwable {
        return logMethodExecution(joinPoint, "Service");
    }

    @Around("execution(* com.ecommerce.repository.*.*(..))")
    public Object logRepository(ProceedingJoinPoint joinPoint) throws Throwable {
        return logMethodExecution(joinPoint, "Repository");
    }

    @Around("securityPointcut()")
    public Object logSecurity(ProceedingJoinPoint joinPoint) throws Throwable {
        return logMethodExecution(joinPoint, "Security");
    }

    @Pointcut("execution(* com.ecommerce.config.SecurityConfig.*(..))")
    public void securityConfigMethods() {}

    @Pointcut("execution(* com.ecommerce.util.JwtUtil.*(..))")
    public void jwtUtilMethods() {}

    @Pointcut("execution(* com.ecommerce.middleware.JwtAspect.*(..))")
    public void jwtAspectMethods() {}

    @Pointcut("securityConfigMethods() || jwtUtilMethods() || jwtAspectMethods()")
    public void securityPointcut() {}

    private Object logMethodExecution(ProceedingJoinPoint joinPoint, String layer) throws Throwable {
        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();
        Object[] arguments = joinPoint.getArgs();

        log.info("[{}] {}.{} - Started - Arguments: {}",
                layer, className, methodName, Arrays.toString(arguments));

        long startTime = System.currentTimeMillis();
        try {
            Object result = joinPoint.proceed();
            long endTime = System.currentTimeMillis();

            log.info("[{}] {}.{} - Completed - Duration: {}ms - Result: {}",
                    layer, className, methodName, (endTime - startTime), result);

            return result;
        } catch (Exception e) {
            log.error("[{}] {}.{} - Failed - Error: {}",
                    layer, className, methodName, e.getMessage(), e);
            throw e;
        }
    }

}
