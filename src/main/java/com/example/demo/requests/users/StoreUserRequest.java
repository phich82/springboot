package com.example.demo.requests.users;

import java.util.HashMap;
import java.util.Map;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import org.springframework.stereotype.Component;

import com.example.demo.requests.Request;
import com.example.demo.validations.rules.FieldsValueMatch;
import com.example.demo.validations.rules.Max;
import com.example.demo.validations.rules.Min;
import com.example.demo.validations.rules.Unique;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@FieldsValueMatch(
    field="password",
    fieldMatch="confirmation_password",
    message="Password and confirmation password don't mismatch."
)
@Unique.List({
    // @Unique(fields="email:email", table="users", pk="user_id:id"),
    // @Unique(fields="username:username", table="users")
    @Unique(fields="email", table="users"),
    @Unique(fields="username", table="users")
})
@Component("storeUserRequest")
public class StoreUserRequest extends Request {

    private int id;

    @NotEmpty(message="First name is required.")
    private String first_name;

    @NotEmpty(message="Last name is required.")
    private String last_name;

    @NotEmpty(message="Usename is required.")
    private String username;

    @NotEmpty(message="Email is required.")
    @Email(message="Email is incorrect.")
    private String email;

    @Max(value=10, message="The phone field contains maximum 255 characters.", onlyfor={"string"})
    private String phone;

    @NotEmpty(message="Password is required.")
    @Min(value=8, message="Password must contain at least 8 characters.")
    private String password;

    @Min(value=8, message="Confirmation password must contain at least 8 characters.")
    private String confirmation_password;

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", this.id);
        params.put("first_name", this.first_name);
        params.put("last_name", this.last_name);
        params.put("username", this.username);
        params.put("email", this.email);
        params.put("phone", this.phone);
        params.put("password", this.password);
        params.put("confirmation_password", this.confirmation_password);
        return params;
    }
}
