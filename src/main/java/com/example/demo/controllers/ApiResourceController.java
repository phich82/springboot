package com.example.demo.controllers;

import java.util.Map;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.services.SuccessResponse;
import com.example.demo.services.contracts.ServiceContract;

public abstract class ApiResourceController extends ApiController {

    protected ServiceContract service;

    /**
     * Constructor
     * @param ServiceContract service
     * @return void
     */
    public ApiResourceController(ServiceContract service) {
        this.service = service;
    }

    @GetMapping("")
    public SuccessResponse index() {
        return this.successResponse(this.service.get());
    }

    @GetMapping("/{id}")
    public SuccessResponse show(@PathVariable int id) {
        return this.successResponse(this.service.getBy(id));
    }

    // @GetMapping("/search")
    // public SuccessResponse search(@RequestParam(name="keyword", required=false, defaultValue="") String name) {
    //     return this.successResponse(this.service.search(name));
    // }

    @PostMapping("")
    public SuccessResponse store(@RequestParam Map<String, String> req) {
        return this.successResponse(this.service.store(req));
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
