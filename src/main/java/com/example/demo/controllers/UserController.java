package com.example.demo.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.models.resources.UserResource;
import com.example.demo.quickstart.GirlFriend;
import com.example.demo.services.ApiResponse;
import com.example.demo.services.SuccessResponse;
import com.example.demo.services.UserService;

@RestController
@RequestMapping("/users") /* Prefix url: /users */
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("")
    public SuccessResponse index() {
        List<UserResource> users = userService.getUserList();
        return new SuccessResponse(users);
    }
    // @GetMapping("")
    // public ResponseEntity<?> getUserList() {
    //     List<UserResource> users = userService.getUserList();
    //     return ApiResponse.success(users);
    // }

    @GetMapping("/{id}")
    public SuccessResponse show(@PathVariable int id) {
        UserResource user = userService.getUser(id);
        return new SuccessResponse(user);
    }
    // @GetMapping("/{id}")
    // public ResponseEntity<?> getUserDetail(@PathVariable int id) {
    //     UserResource user = userService.getUser(id);
    //     return ApiResponse.success(user);
    // }

    // @GetMapping("/search")
    // public ResponseEntity<?> search(@RequestParam(name="keyword", required=false, defaultValue="") String name) {
    //     List<UserResource> users = userService.search(name);
    //     return ApiResponse.success(users);
    // }
    @GetMapping("/search")
    public SuccessResponse search(@RequestParam(name="keyword", required=false, defaultValue="") String name) {
        List<UserResource> users = userService.search(name);
        return new SuccessResponse(users);
    }

    @PostMapping("")
    public SuccessResponse create(@RequestParam Map<String, String> req) {
        UserResource user = userService.createUser(req);
        return new SuccessResponse(user);
    }
    // @PostMapping("")
    // public ResponseEntity<?> createUser(@RequestParam Map<String, String> req) {
    //     UserResource user = userService.createUser(req);
    //     return ApiResponse.success(user);
    // }

    // @PutMapping("/{id}")
    // public ResponseEntity<?> updateUser(@PathVariable int id, @RequestParam Map<String, String> req) {
    //     userService.updateUser(id, req);
    //     return ApiResponse.success();
    // }
    @PutMapping("/{id}")
    public SuccessResponse update(@PathVariable int id, @RequestParam Map<String, String> req) {
        userService.updateUser(id, req);
        return new SuccessResponse();
    }

    // @DeleteMapping("/{id}")
    // public ResponseEntity<?> deleteUser(@PathVariable int id) {
    //     userService.deleteUser(id);
    //     return ApiResponse.success();
    // }
    @DeleteMapping("/{id}")
    public SuccessResponse destroy(@PathVariable int id) {
        userService.deleteUser(id);
        return new SuccessResponse();
    }
}
