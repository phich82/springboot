package com.example.demo.models.mappers;

import com.example.demo.entities.Test;
import com.example.demo.models.resources.TestResource;

/**
 * DTO (Data Transfer Object)
 */
public class TestMapper {

    public static TestResource toResource(Test entity) {
        TestResource tmp = new TestResource();

        tmp.setId(entity.getId());
        tmp.setName(entity.getName());
        tmp.setEmail(entity.getEmail());
        tmp.setPhone(entity.getPhone());
        tmp.setAvatar(entity.getAvatar());

        return tmp;
    }
}
