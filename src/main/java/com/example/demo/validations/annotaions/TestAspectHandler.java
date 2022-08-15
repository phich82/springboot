package com.example.demo.validations.annotaions;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.example.demo.services.App;

/**
 * Hook for method (Before and after method executed)
 */
@Aspect
@Component
public class TestAspectHandler {

    // TestAspect in @annotation(TestAspect) as an User-defined interface
    @Around("@annotation(TestAspect)")
    public Object testAspect(ProceedingJoinPoint joinPoint) throws Throwable {

        // BEFORE METHOD EXECUTION
        System.out.println("[TestAspect] => Pre Processing");
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        Map<String, String[]> paramsRequest = request.getParameterMap();
        Object[] argsMethodTarget = joinPoint.getArgs();

        System.out.println("[TestAspect][argsMethodTarget] => " + argsMethodTarget.toString());
        System.out.println("[TestAspect][paramsRequest]=> " + paramsRequest);

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Class<?> clazz = methodSignature.getDeclaringType();
        Method method = clazz.getDeclaredMethod(methodSignature.getName(), methodSignature.getParameterTypes());
        System.out.println("[TestAspect][class:method] => " + clazz.getSimpleName() + ":" + method.getName());

        try {
            // Load Validation Request Class
            Class<?> ValidationRequest = App.forName("com.example.demo.requests.StoreTestRequest");
            Field[] fields = ValidationRequest.getDeclaredFields();
            Object validationRequest = ValidationRequest.getConstructor().newInstance();
            // Loading data into validation request class
            Map<String, Object> paramsRequestMock = new HashMap<String, Object>() {{
                put("name", "");
                put("email", "username@gmail.com");
                put("phone", "");
                put("avatar", "");
                put("password", "p12345678");
                put("confirmation_password", "p123456789");
            }};

            for (Field f: fields) {
                String fieldname = f.getName();
                if (paramsRequestMock.containsKey(fieldname)) {
                    f.setAccessible(true);
                    f.set(validationRequest, paramsRequestMock.get(fieldname));
                }
            }

            System.out.println("[TestAspect][ValidationRequest][getEmail] => " + validationRequest.getClass().getMethod("getEmail").invoke(validationRequest));
        } catch (Exception e) {
            System.out.println("[TestAspect][ValidationRequest][error] => " + e);
        }

        // ACTUAL METHOD INVOKED
        Object result = joinPoint.proceed();
        System.out.println("[TestAspect] => Processing");

        // AFTER METHOD EXECUTION
        System.out.println("[TestAspect] => Post Processing");

        return result;
    }
}
