package com.example.demo.services.implementations;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.demo.entities.Test;
import com.example.demo.exceptions.BadRequestException;
import com.example.demo.exceptions.NotFoundException;
import com.example.demo.models.mappers.TestMapper;
import com.example.demo.models.resources.TestResource;
import com.example.demo.services.contracts.ServiceContract;


@Component
public class TestService implements ServiceContract {
    private static ArrayList<Test> users = new ArrayList<Test>();

    static {
        users.add(new Test(1, "phich", "phich@gmail.com", "0123456789", "http://localhost:8081/avatar/phich.png", hash("p12345678")));
        users.add(new Test(2, "jhp phich", "jhphich@gmail.com", "1123456789", "http://localhost:8081/avatar/jhphich.png",hash("p12345678")));
        users.add(new Test(3, "phat", "phat@gmail.com", "2123456789", "http://localhost:8081/avatar/phat.png", hash("p12345678")));
    }

    private static String hash(String password) {
        return (new BCryptPasswordEncoder()).encode(password);
    }

    public TestResource find(int id) {
        for (Test user: users) {
            if (user.getId() == id) {
                return TestMapper.toResource(user);
            }
        }
        return null;
    }

    public TestResource findOrFailed(int id) {
        for (Test user: users) {
            if (user.getId() == id) {
                return TestMapper.toResource(user);
            }
        }
        throw new NotFoundException();
    }

    @Override
    public List<TestResource> get() {
        List<TestResource> userList = new ArrayList<TestResource>();

        for (Test user: users) {
            userList.add(TestMapper.toResource(user));
        }
        return userList;
    }

    @Override
    public TestResource getBy(int id) {
        return this.findOrFailed(id);
    }

    public List<TestResource> search(String keyword) {
        List<TestResource> userList = new ArrayList<>();
        for (Test user: users) {
            if (user.getName().contains(keyword)) {
                userList.add(TestMapper.toResource(user));
            }
        }
        return userList;
    }

    @Override
    public TestResource store(Map<String, String> params) {
        String[] keys = {"name", "email", "phone", "avatar", "password", "confirmation_password"};
        // Validate keys
        for (String key : keys) {
            if (!params.containsKey(key)) {
                throw new BadRequestException("Missing `" + key + "` key.");
            }
        }
        // Validate password
        if (!params.get("password").equals(params.get("confirmation_password"))) {
            throw new BadRequestException("Password and confirmation password are mismatch.");
        }
        Test user = new Test(
            users.size() + 1,
            params.get("name"),
            params.get("email"),
            params.get("phone"),
            params.get("avatar"),
            hash(params.get("password"))
        );
        users.add(user);
        return TestMapper.toResource(user);
    }

    @Override
    public boolean update(int id, Map<String, String> params) {
        // Validate user (exists)
        this.findOrFailed(id);

        for (int i=0; i < users.size(); i++) {
            Test user = users.get(i);
            if (user.getId() == id) {
                if (params.containsKey("name")) {
                    user.setName(params.get("name"));
                }
                if (params.containsKey("email")) {
                    user.setEmail(params.get("email"));
                }
                if (params.containsKey("phone")) {
                    user.setPhone(params.get("phone"));
                }
                if (params.containsKey("avatar")) {
                    user.setAvatar(params.get("avatar"));
                }
                users.set(i, user);
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean destroy(int id) {
        // Validate user (exists)
        this.findOrFailed(id);

        for (int i=0; i < users.size(); i++) {
            if (users.get(i).getId() == id) {
                users.remove(i);
                return true;
            }
        }

        return false;
    }
}
