package com.example.demo.models.mappers;

import com.example.demo.entities.Test;
import com.example.demo.models.resources.TestModel;

import lombok.Getter;

/**
 * DTO (Data Transfer Object)
 */
@Getter
public class TestDto {
    int id;
    String name;
    String email;
    String phone;
    String avatar;

    public void loadFromEntity(Test entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.email = entity.getEmail();
        this.phone = entity.getPhone();
        this.avatar = entity.getAvatar();
    }

    public static TestDto fromEntity(Test entity) {
        TestDto _this = new TestDto();
        _this.id = entity.getId();
        _this.name = entity.getName();
        _this.email = entity.getEmail();
        _this.phone = entity.getPhone();
        _this.avatar = entity.getAvatar();

        return _this;
    }
}


// TestDto testDto = TestDto.fromEntity(new Test()); // From Entity -> Dto
// TestModel testModel = TestModel.fromDto(testDto); // From Dto -> Model (Entity)