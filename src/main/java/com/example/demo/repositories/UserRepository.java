package com.example.demo.repositories;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.User;


@Repository
@Component
public class UserRepository extends DB {

    @Override
    protected Class<?> model() {
        return User.class;
    }

    public String primaryKey() {
        return "id";
    }

}
