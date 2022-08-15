package com.example.demo.controllers;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.requests.users.StoreUserRequest;
import com.example.demo.services.SuccessResponse;
import com.example.demo.services.implementations.UserService;
import com.example.demo.validations.annotaions.TestAspect;


@RestController
@RequestMapping("/users")
public class UserController extends ApiController {

    @Autowired
    UserService service;


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
    // @PostMapping("")
    // public SuccessResponse store(@Valid @RequestBody StoreUserRequest storeUserRequest, @RequestParam Map<String, Object> paramsQuery) {
    //     return this.successResponse(this.service.store(storeUserRequest.toMap()));
    // }

    @PutMapping("/{id}")
    public SuccessResponse update(@PathVariable int id, @RequestBody Map<String, Object> params, @RequestParam Map<String, Object> paramsQuery) {
        this.service.update(id, params);
        return this.successResponse();
    }

    // @PatchMapping("/{id}")
    // public SuccessResponse updatePartial(@PathVariable int id, @Valid @RequestBody PutUserRequest putUserRequest, @RequestParam Map<String, Object> paramsQuery) {
    //     this.service.update(id, putUserRequest.toMap());
    //     return this.successResponse();
    // }

    @PatchMapping("/{id}")
    @TestAspect
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
