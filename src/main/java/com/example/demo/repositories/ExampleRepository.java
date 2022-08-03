package com.example.demo.repositories;

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

@Component
public class ExampleRepository extends Repository {
    private static ArrayList<Test> data = new ArrayList<Test>();

    static {
        data.add(new Test(1, "phich", "phich@gmail.com", "0123456789", "http://localhost:8081/avatar/phich.png", hash("p12345678")));
        data.add(new Test(2, "jhp phich", "jhphich@gmail.com", "1123456789", "http://localhost:8081/avatar/jhphich.png",hash("p12345678")));
        data.add(new Test(3, "phat", "phat@gmail.com", "2123456789", "http://localhost:8081/avatar/phat.png", hash("p12345678")));
    }

    private static String hash(String password) {
        return (new BCryptPasswordEncoder()).encode(password);
    }

    public TestResource find(int id) {
        for (Test test: data) {
            if (test.getId() == id) {
                return TestMapper.toResource(test);
            }
        }
        return null;
    }

    public TestResource findOrFailed(int id) {
        for (Test test: data) {
            if (test.getId() == id) {
                return TestMapper.toResource(test);
            }
        }
        throw new NotFoundException();
    }

    public List<TestResource> get() {
        List<TestResource> records = new ArrayList<TestResource>();

        for (Test test: data) {
            records.add(TestMapper.toResource(test));
        }
        return records;
    }

    public TestResource getBy(int id) {
        return this.findOrFailed(id);
    }

    public List<TestResource> search(String keyword) {
        List<TestResource> records = new ArrayList<>();
        for (Test test: data) {
            if (test.getName().contains(keyword)) {
                records.add(TestMapper.toResource(test));
            }
        }
        return records;
    }

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
        Test test = new Test(
            data.size() + 1,
            params.get("name"),
            params.get("email"),
            params.get("phone"),
            params.get("avatar"),
            hash(params.get("password"))
        );
        data.add(test);
        return TestMapper.toResource(test);
    }

    public boolean update(int id, Map<String, String> params) {
        // Validate user (exists)
        this.findOrFailed(id);

        for (int i=0; i < data.size(); i++) {
            Test record = data.get(i);
            if (record.getId() == id) {
                if (params.containsKey("name")) {
                    record.setName(params.get("name"));
                }
                if (params.containsKey("email")) {
                    record.setEmail(params.get("email"));
                }
                if (params.containsKey("phone")) {
                    record.setPhone(params.get("phone"));
                }
                if (params.containsKey("avatar")) {
                    record.setAvatar(params.get("avatar"));
                }
                data.set(i, record);
                return true;
            }
        }

        return false;
    }

    public boolean destroy(int id) {
        // Validate user (exists)
        this.findOrFailed(id);

        for (int i=0; i < data.size(); i++) {
            if (data.get(i).getId() == id) {
                data.remove(i);
                return true;
            }
        }

        return false;
    }
}
