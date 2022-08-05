package com.example.demo.models.resources;

import com.example.demo.models.mappers.TestDto;

import lombok.Getter;
import lombok.Setter;

/**
 * Domain Model
 */
@Getter
@Setter
public class TestModel {
    int id;
    String name;
    String email;
    String phone;
    String avatar;
    String password;

    public void loadFromDto(TestDto dto) {
        this.id = dto.getId();
        this.name = dto.getName();
        this.email = dto.getEmail();
        this.phone = dto.getPhone();
        this.avatar = dto.getAvatar();
    }

    public static TestModel fromDto(TestDto dto) {
        TestModel _this = new TestModel();
        _this.id = dto.getId();
        _this.name = dto.getName();
        _this.email = dto.getEmail();
        _this.phone = dto.getPhone();
        _this.avatar = dto.getAvatar();

        return _this;
    }
}
