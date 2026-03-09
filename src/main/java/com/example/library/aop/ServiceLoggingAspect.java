package com.example.library.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

@Aspect
@Component
public class ServiceLoggingAspect {

    private static final Logger log = Logger.getLogger(ServiceLoggingAspect.class.getName());

    @Around("within(com.example.library.service..*)")
    public Object logServiceCall(ProceedingJoinPoint pjp) throws Throwable {

        MethodSignature signature = (MethodSignature) pjp.getSignature();
        String methodName = signature.getDeclaringType().getSimpleName() + "." + signature.getName();

        Object[] args = pjp.getArgs();

        log.info(() -> "CALL " + methodName + " args=" + Arrays.deepToString(args));

        try {
            Object result = pjp.proceed();

            log.info(() -> "RETURN " + methodName + " -> " + result);

            return result;
        } catch (Throwable ex) {
            log.log(Level.WARNING, "THROW " + methodName + " ex=" + ex.getMessage(), ex);
            throw ex;
        }
    }
}