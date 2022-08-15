package com.example.demo.entities.mappers;

import java.util.Map;

import com.example.demo.entities.User;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO (Data Transfer Object)
 */
@Getter
@Setter
public class UserDto {
    int id;
    String first_name;
    String last_name;
    String username;
    String email;
    String phone;

    public static UserDto fromData(Map<String, ?> params) {
        UserDto _this = new UserDto();
        if (params.containsKey("id")) {
            _this.id = (int) params.get("id");
        }
        if (params.containsKey("first_name")) {
            _this.first_name = (String) params.get("first_name");
        }
        if (params.containsKey("last_name")) {
            _this.last_name = (String) params.get("last_name");
        }
        if (params.containsKey("username")) {
            _this.username = (String) params.get("username");
        }
        if (params.containsKey("email")) {
            _this.email = (String) params.get("email");
        }
        if (params.containsKey("phone")) {
            _this.phone = (String) params.get("phone");
        }
        return _this;
    }

    public static UserDto fromEntity(User entity) {
        UserDto _this = new UserDto();
        _this.id = entity.getId();
        _this.first_name = entity.getFirst_name();
        _this.last_name = entity.getLast_name();
        _this.username = entity.getUsername();
        _this.email = entity.getEmail();
        _this.phone = entity.getPhone();

        return _this;
    }
}
