package com.example.demo.models.mappers;

import com.example.demo.entities.Test;
import com.example.demo.models.resources.TestResource;

public class TestMapper {

    public static TestResource toResource(Test test) {
        TestResource tmp = new TestResource();

        tmp.setId(test.getId());
        tmp.setName(test.getName());
        tmp.setEmail(test.getEmail());
        tmp.setPhone(test.getPhone());
        tmp.setAvatar(test.getAvatar());

        return tmp;
    }
}
