package com.hospital.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
public class ExecutionTimeLogger {

    //all controller methods both Graphql and RestAPi

    @Around("execution(* com.hospital.controller..*(..)) || execution(* com.hospital.graphql..*(..))")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable{
        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed(); // call the original method
        long end = System.currentTimeMillis();
        long duration = end -start;
        String methodName = joinPoint.getSignature().toShortString();
        log.info("[executionTime] {} took {} ms",methodName,duration);
        return result;}

}
