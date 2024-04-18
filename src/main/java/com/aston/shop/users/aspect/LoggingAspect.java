package com.aston.shop.users.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {

	private static final Logger AUTH_LOGGER = LoggerFactory.getLogger("AUTH_LOGGER");
	private static final Logger USER_LOGGER = LoggerFactory.getLogger("USER_LOGGER");
	private static final Logger EXCEPTION_LOGGER = LoggerFactory.getLogger("EXCEPTION_LOGGER");

	// Предположим, что аутентификация происходит в методе authenticate пользователя
	@Around("execution(* com.aston.shop.users.service.impl.AuthenticationServiceImpl.*.*(..))")
	public Object logAuthAttempt(ProceedingJoinPoint joinPoint) throws Throwable {
		AUTH_LOGGER.info("Authentication attempt for user: {}", Arrays.toString(joinPoint.getArgs()));
		try {
			Object result = joinPoint.proceed();
			AUTH_LOGGER.info("Authentication successful for user");
			return result;
		} catch (Exception e) {
			AUTH_LOGGER.error("Authentication failed for user: {}", e.getMessage());
			throw e;
		}
	}

	// Предположим, что пользовательские операции происходят в service слое
	@AfterReturning("execution(* com.aston.shop.users.service.impl.*.*(..))")
	public void logUserServiceOperation(JoinPoint joinPoint) {
		USER_LOGGER.info("User operation performed: {}", joinPoint.getSignature().toShortString());
	}

//	@AfterThrowing(pointcut = "execution(* com.aston.shop.users.controller.*.*(..)) && !execution(* com.aston.shop.users.service.impl.AuthenticationServiceImpl(..))", throwing = "ex")
//	public void logException(JoinPoint joinPoint, Throwable ex) {
//		EXCEPTION_LOGGER.error("Exception in {}.{}() with cause = {}", joinPoint.getSignature().getDeclaringTypeName(),
//				joinPoint.getSignature().getName(), ex.getCause() != null ? ex.getCause() : "NULL");
//	}

}