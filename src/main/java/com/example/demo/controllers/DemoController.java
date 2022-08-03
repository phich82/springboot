package com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.services.implementations.DemoService;


@RestController
@RequestMapping("/demos")
@Component
public class DemoController extends ApiResourceController {

    @Autowired
    public DemoController(DemoService demoService) {
        super(demoService);
    }
}
