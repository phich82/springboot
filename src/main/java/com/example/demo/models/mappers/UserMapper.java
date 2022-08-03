package com.example.demo.models.mappers;

import com.example.demo.entities.User;
import com.example.demo.models.resources.UserResource;

public class UserMapper {

    public static UserResource toUserResource(User user) {
        UserResource tmp = new UserResource();

        tmp.setId(user.getId());
        tmp.setName(user.getName());
        tmp.setEmail(user.getEmail());
        tmp.setPhone(user.getPhone());
        tmp.setAvatar(user.getAvatar());

        return tmp;
    }
}
