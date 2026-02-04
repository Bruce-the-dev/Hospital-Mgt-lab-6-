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

        String methodName = joinPoint.getSignature().toShortString();
        log.info("[PERFORMANCE] {} executed in {} ms", methodName, duration);

        return result;
    }

    @AfterReturning(pointcut = "execution(* com.hospital.service.PatientService.updatePatient(..))", returning = "result")
    public void logAfter(JoinPoint joinPoint, Object result) {
        System.out.println("[RETURN] " + result);
    }

}
