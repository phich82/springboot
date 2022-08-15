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
        System.out.println("[auth][preHandle] Url => " + "/" + req.getMethod() + " " + req.getRequestURI());
        if (req.getQueryString() != null) {
            System.out.println("[auth][preHandle] Query string => " + req.getQueryString());
        }
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
    }

    @Override
    public void postHandle(HttpServletRequest req, HttpServletResponse res, Object handler, ModelAndView modelAndView) throws Exception {
        // Handle here
        System.out.println("[auth][postHandle] => done");

        HandlerInterceptor.super.postHandle(req, res, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest req, HttpServletResponse res, Object handler, Exception e) throws Exception {
        // Handle here
        System.out.println("[auth][afterCompletion] => done");

        HandlerInterceptor.super.afterCompletion(req, res, handler, e);
    }
}
