package com.example.demo.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import com.example.demo.middlewares.ApiAuthInterceptor;
import com.example.demo.middlewares.AutoValidationInterceptor;
import com.example.demo.services.wrappers.MyRequestResponseBodyMethodProcessor;

/**
 * Configuration for Spring automatically loading
 */
@Configuration
@EnableWebMvc
public class AppConfiguration implements WebMvcConfigurer {

	/**
	 * Load locale messages files
	 *
	 * @return MessageSource
	 */
	@Bean
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		// Load `validation.properties` file
		messageSource.setBasename("classpath:validation");
		messageSource.setDefaultEncoding("UTF-8");
		return messageSource;
	}

	@Bean
    // @Autowired
    public RequestResponseBodyMethodProcessor requestResponseBodyMethodProcessor(List<HttpMessageConverter<?>> converters) {
        return new MyRequestResponseBodyMethodProcessor(converters);
    }

	@Bean
	public AutoValidationInterceptor autoValidationInterceptor() {
		return new AutoValidationInterceptor();
	}

	/**
	 * Middleware configuration
	 */
	@Override
    public void addInterceptors(InterceptorRegistry registry) {
        // Register middlewares/interceptors here
        registry.addInterceptor(new ApiAuthInterceptor()).order(1);
        registry.addInterceptor(autoValidationInterceptor()).order(2);
    }

}
