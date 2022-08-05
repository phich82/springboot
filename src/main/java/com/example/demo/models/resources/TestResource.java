package com.example.demo.models.resources;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * Domain Model
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TestResource {
    private int id;
    private String name;
    private String email;
    private String phone;
    private String avatar;
}
