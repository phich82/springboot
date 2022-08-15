package com.example.demo.services.wrappers;

import java.util.List;

import org.springframework.core.MethodParameter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

@Component
public class MyRequestResponseBodyMethodProcessor extends RequestResponseBodyMethodProcessor {

    public MyRequestResponseBodyMethodProcessor(List<HttpMessageConverter<?>> converters) {
        super(converters);
        System.out.println("[MyRequestResponseBodyMethodProcessor] => " + converters);
    }

    @Override
    protected void validateIfApplicable(WebDataBinder binder, MethodParameter parameter) {
        System.out.println("[MyRequestResponseBodyMethodProcessor][validateIfApplicable] binder => " + binder);
        System.out.println("[MyRequestResponseBodyMethodProcessor][validateIfApplicable] parameter => " + parameter);

        super.validateIfApplicable(binder, parameter);
    }

}
