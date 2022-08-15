package com.example.demo.services.wrappers;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;


@Configuration
public class MyValidationAdapterConfigurer {
    @Autowired
    private RequestMappingHandlerAdapter requestMappingHandlerAdapter;

    // Injecting the custom resolver
    @Autowired
    private MyRequestResponseBodyMethodProcessor requestResponseBodyMethodProcessor;


    @PostConstruct
    public void init() {

        // Don't know why but, removing the target resolver and adding the injected one to the end does not work!
        // Must be something related with the resolvers ordering. So just replacing the target in the same position.
        final List<HandlerMethodArgumentResolver> mangledResolvers = requestMappingHandlerAdapter.getArgumentResolvers().stream()
            .map(resolver -> resolver.getClass().equals(MyRequestResponseBodyMethodProcessor.class) ?
                requestResponseBodyMethodProcessor: resolver)
            .collect(Collectors.toList());

        requestMappingHandlerAdapter.setArgumentResolvers(mangledResolvers);
    }
}
