package com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.services.implementations.ExampleService;


@RestController
@RequestMapping("/examples")
public class ExampleController extends ApiResourceController {

    @Autowired
    public ExampleController(ExampleService exampleService) {
        super(exampleService);
    }
}
