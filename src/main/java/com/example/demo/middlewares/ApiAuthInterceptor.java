package com.example.demo.middlewares;

import java.util.Base64;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.exceptions.BadRequestException;
import com.example.demo.services.Logger;

public class ApiAuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) throws Exception {

        // Validate authorizarion for APIs

        System.out.println("getRequestURI => " + req.getRequestURI());
        System.out.println("getQueryString => " + req.getQueryString());
        System.out.println("getMethod => " + req.getMethod());

        String authHeader = req.getHeader("Authorization");

        if (authHeader == null) {
            Logger.info(String.format("/%s %s => %s", req.getMethod(), req.getRequestURI(), "Missing header `Authorization` key."), this);

            throw new BadRequestException("Missing header `Authorization` key.");
        }

        if (authHeader != null) {
            String[] partsAuthHeader = authHeader.split(" ");
            String authType = partsAuthHeader[0];
            String authValue = partsAuthHeader.length > 1 ? partsAuthHeader[1] : null;
            if (authValue == null) {
                throw new BadRequestException("Unauthorized.");
            }
            switch (authType.toLowerCase()) {
                case "basic":
                    String userpass = new String(Base64.getDecoder().decode(authValue));
                    String username = userpass.split(":")[0];
                    String password = userpass.split(":")[1];
                    if (!username.equals("admin") || !password.equals("123456")) {
                        Logger.info(String.format("/%s %s => %s", req.getMethod(), req.getRequestURI(), "Unauthorized."), this);

                        throw new BadRequestException("Unauthorized.");
                    }
                    break;
                case "bearer":
                    break;
                default:
                    Logger.info(String.format("/%s %s => %s", req.getMethod(), req.getRequestURI(), "Only support authorization types: basic or bearer."), this);

                    throw new BadRequestException("Only support authorization types: basic or bearer.");
            }
        }

        return true;
        // return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // TODO Auto-generated method stub
        System.out.println("[postHandle] getRequestURI => " + request.getRequestURI());
        System.out.println("[postHandle] getQueryString => " + request.getQueryString());
        System.out.println("[postHandle] getMethod => " + request.getMethod());

        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // TODO Auto-generated method stub
        System.out.println("[afterCompletion] getRequestURI => " + request.getRequestURI());
        System.out.println("[afterCompletion] getQueryString => " + request.getQueryString());
        System.out.println("[afterCompletion] getMethod => " + request.getMethod());

        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
