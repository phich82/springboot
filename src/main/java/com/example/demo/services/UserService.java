package com.example.demo.services;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.example.demo.entities.User;
import com.example.demo.models.resources.UserResource;

@Service
// @Component
public interface UserService {
    public List<UserResource> getUserList();
    public UserResource getUser(int id);
    public List<UserResource> search(String keyword);
    public UserResource createUser(Map<String, String> params);
    public boolean updateUser(int id, Map<String, String> params);
    public boolean deleteUser(int id);
}
