package com.example.demo.controllers;

import java.util.Map;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    public SuccessResponse index(@RequestParam Map<String, Object> paramsQuery) {
        // Map<String, Object> conditions = new HashMap<String, Object>();
        // conditions.put("username", {"<operator[<>,>,<,=]>", "<value>"}); // array of operator and value
        // conditions.put("email", {"<value>"}); // array of value
        // conditions.put("email", "<value>""); // only as value
        return this.successResponse(this.service.findBy(null));
    }

    @GetMapping("/{id}")
    public SuccessResponse show(@PathVariable int id, @RequestParam Map<String, Object> paramsQuery) {
        return this.successResponse(this.service.find(id));
    }

    @PostMapping("")
    public SuccessResponse store(@RequestBody Map<String, Object> params, @RequestParam Map<String, Object> paramsQuery) {
        return this.successResponse(this.service.store(params));
    }

    @PutMapping("/{id}")
    public SuccessResponse update(@PathVariable int id, @RequestBody Map<String, Object> params, @RequestParam Map<String, Object> paramsQuery) {
        this.service.update(id, params);
        return this.successResponse();
    }

    @PatchMapping("/{id}")
    public SuccessResponse updatePartial(@PathVariable int id, @RequestBody Map<String, Object> params, @RequestParam Map<String, Object> paramsQuery) {
        this.service.update(id, params);
        return this.successResponse();
    }

    @DeleteMapping("/{id}")
    public SuccessResponse destroy(@PathVariable int id, @RequestParam Map<String, Object> paramsQuery) {
        this.service.destroy(id);
        return this.successResponse();
    }
}
