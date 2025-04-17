package com.example.demo.Logging;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.annotation.Before;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Aspect
@Component
public class LoginAttemptAspect {
    private static final int MAX_ATTEMPTS = 3;
    private static final ConcurrentHashMap<String, Integer> attemptsCache = new ConcurrentHashMap<>();

    @Pointcut("execution(* com.example.orders.Security.SecurityConfigurator.customFailureHandler(..))")
    public void loginAttempt() {}

    @Before("loginAttempt() && args(request, response, exception)")
    public void incrementLoginAttempts(HttpServletRequest request, HttpServletResponse response, Exception exception) {
        String username = request.getParameter("username");
        int attempts = attemptsCache.getOrDefault(username, 0) + 1;
        attemptsCache.put(username, attempts);
    }

    @Cacheable(value = "loginAttempts", key = "#username", unless = "#result == null")
    public int getLoginAttempts(String username) {
        return attemptsCache.getOrDefault(username, 0);
    }

    @CacheEvict(value = "loginAttempts", key = "#username")
    public void clearLoginAttempts(String username) {
        attemptsCache.remove(username);
    }

    public boolean isLockedOut(String username) {
        return getLoginAttempts(username) >= MAX_ATTEMPTS;
    }
}