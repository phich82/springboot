package com.example.demo.repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.demo.entities.User;
import com.example.demo.exceptions.BadRequestException;
import com.example.demo.exceptions.NotFoundException;
import com.example.demo.models.mappers.UserMapper;
import com.example.demo.models.resources.UserResource;

@Component
public class DemoRepository extends Repository {
    private static ArrayList<User> users = new ArrayList<User>();

    static {
        users.add(new User(1, "phich", "phich@gmail.com", "0123456789", "http://localhost:8081/avatar/phich.png", hash("p12345678")));
        users.add(new User(2, "jhp phich", "jhphich@gmail.com", "1123456789", "http://localhost:8081/avatar/jhphich.png",hash("p12345678")));
        users.add(new User(3, "phat", "phat@gmail.com", "2123456789", "http://localhost:8081/avatar/phat.png", hash("p12345678")));
    }

    private static String hash(String password) {
        return (new BCryptPasswordEncoder()).encode(password);
    }

    public UserResource find(int id) {
        for (User user: users) {
            if (user.getId() == id) {
                return UserMapper.toUserResource(user);
            }
        }
        return null;
    }

    public UserResource findOrFailed(int id) {
        for (User user: users) {
            if (user.getId() == id) {
                return UserMapper.toUserResource(user);
            }
        }
        throw new NotFoundException();
    }

    public List<UserResource> get() {
        List<UserResource> userList = new ArrayList<UserResource>();

        for (User user: users) {
            userList.add(UserMapper.toUserResource(user));
        }
        return userList;
    }

    public UserResource getBy(int id) {
        return this.findOrFailed(id);
    }

    public List<UserResource> search(String keyword) {
        List<UserResource> userList = new ArrayList<>();
        for (User user: users) {
            if (user.getName().contains(keyword)) {
                userList.add(UserMapper.toUserResource(user));
            }
        }
        return userList;
    }

    public UserResource store(Map<String, String> params) {
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
        User user = new User(
            users.size() + 1,
            params.get("name"),
            params.get("email"),
            params.get("phone"),
            params.get("avatar"),
            hash(params.get("password"))
        );
        users.add(user);
        return UserMapper.toUserResource(user);
    }

    public boolean update(int id, Map<String, String> params) {
        // Validate user (exists)
        this.findOrFailed(id);

        for (int i=0; i < users.size(); i++) {
            User user = users.get(i);
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
