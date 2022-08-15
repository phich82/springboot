package com.example.demo.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.stereotype.Component;

import com.example.demo.entities.mappers.UserDto;

import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "users")
@Getter
@Setter
@Component
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(name="username", nullable = false, unique = true)
    String username;

    @Column(nullable = false, unique = true)
    String email;

    @Column(name="first_name")
    String first_name;

    @Column(name="last_name")
    String last_name;

    @Column(name="phone")
    String phone;

    @Column(name="password")
    String password;

    public static User fromDto(UserDto dto) {
        User _this = new User();
        _this.id = dto.getId();
        _this.first_name = dto.getFirst_name();
        _this.last_name = dto.getLast_name();
        _this.username = dto.getUsername();
        _this.email = dto.getEmail();
        _this.phone = dto.getPhone();

        return _this;
    }
}
