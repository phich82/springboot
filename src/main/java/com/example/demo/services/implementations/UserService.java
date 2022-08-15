package com.example.demo.services.implementations;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.entities.User;
import com.example.demo.entities.mappers.UserDto;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.contracts.ServiceContract;


@Component
public class UserService implements ServiceContract {

    @Autowired
    protected UserRepository userRepository;

    public List<UserDto> findAll() {
        List<UserDto> records = new ArrayList<UserDto>();
        List<Map<String, ?>> users = this.userRepository.findAll();
        users.forEach(user -> records.add(UserDto.fromData(user)));
        return records;
    }

    @Override
    public UserDto find(Object id) {
        return UserDto.fromData(this.userRepository.findOrFailed(id));
    }

    @Override
    public List<UserDto> findBy(Map<String, ?> params) {
        List<UserDto> records = new ArrayList<UserDto>();
        List<Map<String, ?>> users = this.userRepository.findBy(params);
        users.forEach(user -> records.add(UserDto.fromData(user)));
        return records;
    }

    @Override
    public UserDto store(Object user) {
        if (user instanceof Map) {
            Map<String, ?> params = (Map<String, ?>) user;
            User _user = User.fromDto(UserDto.fromData(params));
            _user.setPassword((String) params.get("password"));
            user = _user;
        }
        return UserDto.fromEntity((User) this.userRepository.save(user));
    }

    @Override
    public boolean update(Object id, Map<String, Object> params) {
        // Check exists
        this.userRepository.findOrFailed(id);
        params.put("id", id);
        System.out.println("ddd => " + params);
        return this.userRepository.update(id, params);
    }

    @Override
    public boolean update(Object user) {
        // Check exists
        this.userRepository.findOrFailed(((User) user).getId());
        return this.userRepository.update(user);
    }

    @Override
    public boolean destroy(Object id) {
        // Check exists
        this.userRepository.findOrFailed(id);
        return this.userRepository.delete(id);
    }

}
