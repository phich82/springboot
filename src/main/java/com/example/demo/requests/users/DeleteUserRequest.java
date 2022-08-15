package com.example.demo.requests.users;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.example.demo.requests.Request;
import com.example.demo.validations.rules.Exist;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Component
public class DeleteUserRequest extends Request {
    @Exist(table="users")
    private int id;

    // @Override
    public Map<String, Object> toMap() {
        // Map<String, Object> params = new HashMap<String, Object>();
        // params.put("id", this.id);
        return new HashMap<String, Object>(){{
            put("id", id);
        }};
    }
}
