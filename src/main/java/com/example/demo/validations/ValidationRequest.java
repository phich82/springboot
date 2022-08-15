package com.example.demo.validations;

import java.util.HashMap;
import java.util.Map;

import com.example.demo.controllers.UserController;
import com.example.demo.controllers.UserResourceController;
import com.example.demo.requests.users.DeleteUserRequest;
import com.example.demo.requests.users.PatchUserRequest;
import com.example.demo.requests.users.PutUserRequest;
import com.example.demo.requests.users.StoreUserRequest;

/**
 * Mapping validation rules into controller and its methods
 * {
 *  "<controller1[package + controller name]>": {
 *      "<http_method1>": "<vaidation_request_class1>",
 *      "<http_method2>": "<vaidation_request_class2>",
 *      "<http_method3>": "<vaidation_request_class3>",
 * },
 *  "<controller2[package + controller name]>": {
 *      "<http_method1>": "<vaidation_request_class2>",
 *      "<http_method2>": "<vaidation_request_class2>",
 *      "<http_method3>": "<vaidation_request_class2>",
 * },
 * }
 */
public class ValidationRequest {

    public Class<?> get(String controller, String method, Map<String, Object> paramsRequest) {
        System.out.println("[ValidationRequest][get] <controller:method> => " + controller + ":" + method);
        System.out.println("[ValidationRequest][get] <request params> => " + paramsRequest);
        try {
            return this.getRules(paramsRequest).get(controller).get(method);
        } catch (Exception e) {
            // Controller not exists or method on controller not exists
            return null;
        }
    }

    private Map<String, Map<String, Class<?>>>getRules(Map<String, Object> paramsRequest) {
        return new HashMap<String, Map<String, Class<?>>>() {{
            // Validation rules for UserController
            put(UserController.class.getName(), new HashMap<String, Class<?>>() {{
                put("store", StoreUserRequest.class);
                put("update", PutUserRequest.class);
                put("updatePartial", PatchUserRequest.class);
                put("delete", DeleteUserRequest.class);
            }});
            // Validation rules for UserResourceController
            put(UserResourceController.class.getName(), new HashMap<String, Class<?>>() {{
                put("store", StoreUserRequest.class);
                put("update", PutUserRequest.class);
                put("updatePartial", PatchUserRequest.class);
                put("delete", DeleteUserRequest.class);
            }});
            // Validation rules for TestController
            put("c2", new HashMap<String, Class<?>>() {{
                put("m1", null);
                put("m2", null);
            }});
        }};
    }

}
