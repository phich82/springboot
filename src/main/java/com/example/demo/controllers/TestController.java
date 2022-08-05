package com.example.demo.controllers;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.models.resources.TestResource;
import com.example.demo.requests.StoreTestRequest;
import com.example.demo.services.Logger;
import com.example.demo.services.SuccessResponse;
import com.example.demo.services.implementations.TestService;


@RestController
@RequestMapping("/tests")
public class TestController extends ApiController {

    @Autowired
    protected TestService service;

    @GetMapping("")
    public SuccessResponse index() {
        Logger.info("This is error message for test.", this);
        List<TestResource> records = this.service.get();
        return this.successResponse(records);
    }

    @GetMapping("/{id}")
    public SuccessResponse show(@PathVariable int id) {
        TestResource record = this.service.getBy(id);
        return this.successResponse(record);
    }

    @GetMapping("/search")
    public SuccessResponse search(@RequestParam(name="keyword", required=false, defaultValue="") String name) {
        List<TestResource> records = this.service.search(name);
        return this.successResponse(records);
    }

    @PostMapping("")
    public SuccessResponse store(@RequestParam Map<String, String> req, @Valid StoreTestRequest storeTestRequest) {
        TestResource record = this.service.store(req);
        return this.successResponse(record);
    }

    @PutMapping("/{id}")
    public SuccessResponse update(@PathVariable int id, @RequestParam Map<String, String> req) {
        this.service.update(id, req);
        return this.successResponse();
    }

    @DeleteMapping("/{id}")
    public SuccessResponse destroy(@PathVariable int id) {
        this.service.destroy(id);
        return this.successResponse();
    }
}
