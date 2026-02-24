package com.example.library.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Aspect
@Component
public class SimpleCacheAspect {

    private static final Object NULL = new Object();
    private final ConcurrentMap<CacheKey, Object> cache = new ConcurrentHashMap<>();

    @Around("@annotation(com.example.library.aop.Cached)")
    public Object cachedCall(ProceedingJoinPoint joinPoint) throws Throwable {
        if(joinPoint.getArgs().length == 0) return joinPoint.proceed();

        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        if(method.getReturnType().equals(Void.TYPE)) return joinPoint.proceed();

        CacheKey cacheKey = new CacheKey(method, joinPoint.getArgs());

        Object hit = cache.get(cacheKey);
        if (hit != null) {
            return hit == NULL ? null : hit;
        }

        Object computed = joinPoint.proceed();
        cache.put(cacheKey, computed == null ? NULL : computed);
        return computed;
    }

    private record CacheKey(Method method, Object... args) {

        CacheKey {
            args = Arrays.copyOf(args, args.length);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof CacheKey other)) return false;
            return method.equals(other.method) && Arrays.deepEquals(args, other.args);
        }

        @Override
        public int hashCode() {
            return 31 * method.hashCode() + Arrays.deepHashCode(args);
        }
    }


}
