package com.hospital.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.Map;

@Aspect
@Component
@Slf4j
public class ServiceMonitorAspect {

    // Pointcut definition (NO body logic)
    @Pointcut("execution(* com.hospital.service..*(..))") // methods define which class is targeted
    public void serviceLayer() {
    }

    @Before("serviceLayer()")
    public void logMethodEntry(JoinPoint jp) {
        String methodName = jp.getSignature().toShortString();
        Object[] args = jp.getArgs();
        log.info("[ENTER] {} with arguments {}", methodName, args);
    }

    @After("serviceLayer()")
    public void logMethodExit(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().toShortString();
        log.info("[EXIT] {}", methodName);
    }

    @Around("serviceLayer()")
    public Object monitorExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {

        long start = System.currentTimeMillis();

        Object result = joinPoint.proceed();

        long duration = System.currentTimeMillis() - start;
        int count = extractCount(result);

        String methodName = joinPoint.getSignature().toShortString();
        log.info("[PERFORMANCE] {} executed in {} ms | resultCount={}", methodName, duration, count);

        return result;
    }

    private int extractCount(Object result) {

        if (result == null) {
            return 0;
        }

        // Spring Data Page
        if (result instanceof org.springframework.data.domain.Page<?> page) {
            return Math.toIntExact(page.getTotalElements());
        }

        // Spring Data Slice
        if (result instanceof org.springframework.data.domain.Slice<?> slice) {
            return slice.getNumberOfElements();
        }

        // Collection
        if (result instanceof Collection<?> collection) {
            return collection.size();
        }

        // Map
        if (result instanceof Map<?, ?> map) {
            return map.size();
        }

        // Array
        if (result.getClass().isArray()) {
            return java.lang.reflect.Array.getLength(result);
        }

        // Single object
        return 1;
    }

    @AfterReturning(pointcut = "execution(* com.hospital.service.PatientService.updatePatient(..))", returning = "result")
    public void logAfter(JoinPoint joinPoint, Object result) {
        System.out.println("[RETURN] " + result);
    }

}
