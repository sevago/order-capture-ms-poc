package com.example.oc.mspoc.aspect;

import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.example.oc.mspoc.listener.AssignedProductMessageListener;

@Aspect
@Component
public class MessageListenerAspect {
	
	private static final Logger log = LoggerFactory.getLogger(AssignedProductMessageListener.class);
	
	@Around("execution(* com.example.oc.mspoc.listener.*.receiveMessage(java.util.Map)) && args(message)")
    public void aroundReceiveMessageAdvice(ProceedingJoinPoint proceedingJoinPoint, Map<String, String> message) throws Throwable {
		log.debug("ASPECT: Received {} message", message);
		proceedingJoinPoint.proceed(new Object[] {message});
		log.debug("ASPECT: Message {} processed", message);
    }

}
