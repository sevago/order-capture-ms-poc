package com.example.oc.mspoc.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class OrderCaptureRepositoryAspect {
	
	@Before("execution(* com.example.oc.mspoc.repository.*.save(java.lang.String,java.lang.String)) || execution(* com.example.oc.mspoc.repository.*.find(java.lang.String))")
    public void beforeFindAdvice(JoinPoint joinPoint) {
		System.out.println(">>> BEFORE ASPECT - LOGGING FOR REDIS REPO <<< [" + joinPoint.toString() + "] method is executed with input [" + joinPoint.getArgs() + "]");   
    }

}
