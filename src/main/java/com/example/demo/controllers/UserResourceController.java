package com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.services.implementations.UserService;


@RestController
@RequestMapping("/users-resource")
public class UserResourceController extends ApiResourceController {

    @Autowired
    public UserResourceController(UserService service) {
        super(service);
    }

}
