package com.example.demo.services.wrappers;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

import org.springframework.core.annotation.Order;

/**
 *  Rewrite Http Filter (customize Filter for allowing to read the request body more once time)
 *
 */
@Order(1) // order executed. The smaller the value, the more it is executed first
@WebFilter(filterName = "bodyReaderFilter", urlPatterns = "/*") // Configure the addresses to be filtered
public class BodyReaderFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // System.out.println("-------------- Filter initialization ------------");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        // System.out.println("-------------- Perform filtering operations ------------");

        // Prevent stream reading 1. There will be no more after the second time, So you need to continue writing out the stream
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        ServletRequest requestWrapper = new BodyReaderHttpServletRequestWrapper(httpServletRequest);
        filterChain.doFilter(requestWrapper, servletResponse);
    }

    @Override
    public void destroy() {
        // System.out.println("-------------- Filter destruction ------------");
    }
}
