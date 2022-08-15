package com.example.demo.middlewares;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.util.MultiValueMap;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.demo.exceptions.AutoValidationException;
import com.example.demo.services.wrappers.BodyReaderHttpServletRequestWrapper;
import com.example.demo.validations.ValidationRequest;

public class AutoValidationInterceptor implements HandlerInterceptor {

    @Autowired
    private Validator validator;

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) throws Exception {

        // Automatically validate request params
        if (handler instanceof HandlerMethod) {
            // Route parameters
            final Map<String, String> paramsRoute = (Map<String, String>) req.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
            System.out.println("[autovalidation][preHandle] paramsRoute => " + paramsRoute);
            // Body parameters
            BodyReaderHttpServletRequestWrapper requestWrapper = new BodyReaderHttpServletRequestWrapper(req);
            String bodyString = requestWrapper.getBodyString(req);
            Map<String, Object> paramsBody = new JacksonJsonParser().parseMap(bodyString == null || bodyString.equals("") ? "{}" : bodyString);
            System.out.println("[autovalidation][preHandle] paramsBody => " + paramsBody);
            // Query parameters
            MultiValueMap<String, String> paramsQueryStandard = UriComponentsBuilder.fromUriString("/?"+req.getQueryString()).build().getQueryParams();
            Map<String, ?> paramsQuery = paramsQueryStandard.entrySet().stream()
                .collect(Collectors.toMap(
                    eS -> eS.getKey(),
                    eS -> {
                        Object value = eS.getValue().get(eS.getValue().size() - 1);
                        return value == null ? "" : value;
                    }
                ));
            System.out.println("[autovalidation][preHandle] paramsQuery => " + paramsQuery);

            // there are cases where this handler isn't an instance of HandlerMethod, so the cast fails.
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			//controllerName = handlerMethod.getBean().getClass().getSimpleName().replace("Controller", "");
			String controllerName  = handlerMethod.getBeanType().getName();
			String actionName = handlerMethod.getMethod().getName();

            System.out.println("[autovalidation][preHandle] <controller:method> => " + controllerName + " - " + actionName);

            Class<?> validationRequest = new ValidationRequest().get(controllerName, actionName, null);
            System.out.println("[autovalidation][preHandle] validationRequest => " + validationRequest);

            // Check exists of vaidation request class.
            // If found, validate it. Otherwise, ignore it.
            if (validationRequest == null) {
                System.out.println("[autovalidation][preHandle] Validation Request Class does not exist. Validation has been ignored.");
                return true;
            }

            // Loading data into validation request class
            Field[] fields = validationRequest.getDeclaredFields();
            Object validationRequestInstance = validationRequest.getConstructor().newInstance();

            for (Field f: fields) {
                String fieldname = f.getName();
                if (paramsBody.containsKey(fieldname)) {
                    f.setAccessible(true);
                    f.set(validationRequestInstance, paramsBody.get(fieldname));
                }
            }
            // validationRequestInstance.getClass().getMethod("getLast_name").invoke(validationRequestInstance));

            // Validation
            Set<ConstraintViolation<Object>> violations = this.validator.validate(validationRequestInstance);
            if (!violations.isEmpty()) {
                Map<String, List<String>> errors = new HashMap<String, List<String>>();
                for (ConstraintViolation<Object> violation: violations) {
                    if (violation != null) {
                        List<String> messages = new ArrayList<String>();
                        String fieldname = violation.getPropertyPath().toString();
                        String msgerror = violation.getMessage();
                        if (errors.containsKey(fieldname)) {
                            messages = errors.get(fieldname);
                        }
                        messages.add(msgerror);
                        errors.put(fieldname, messages);
                    }
                }
                System.out.println("[autovalidation][preHandle] errors => " + errors);

                String messagesError = violations.stream()
                    .map(cv -> cv == null ? "null" : cv.getPropertyPath() + ":" + "[" + cv.getMessage() + "]")
                    .collect(Collectors.joining( ", " ) );

                throw new AutoValidationException(messagesError, errors);
            }
		}
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest req, HttpServletResponse res, Object handler, ModelAndView modelAndView) throws Exception {
        // Handle here
        System.out.println("[autovalidation][postHandle] => done");

        HandlerInterceptor.super.postHandle(req, res, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest req, HttpServletResponse res, Object handler, Exception e) throws Exception {
        // Handle here
        System.out.println("[autovalidation][afterCompletion] => done");

        HandlerInterceptor.super.afterCompletion(req, res, handler, e);
    }

}
