package com.example.demo.services;

import java.lang.reflect.InvocationTargetException;

import javax.annotation.PostConstruct;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public class App {

    private static App instance;

    @Autowired
    private ApplicationContext applicationContext;

    @PostConstruct
    private void registerInstance() {
        instance = this;
    }

    public static Object make(Class<?> clazz) throws BeansException {
        return getBean(clazz);
    }

    public static Object getBean(Class<?> clazz) throws BeansException {
        return instance.applicationContext.getBean(clazz);
    }

    public static Class<?> forName(String clazz) throws ClassNotFoundException {
        return Class.forName(clazz);
    }

    public static Object getInstance(String clazz, Object... initargs) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        return forName(clazz).getConstructor().newInstance(initargs);
    }

}
