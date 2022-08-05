package com.example.demo.requests;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import com.example.demo.validations.rules.FieldsValueMatch;
import com.example.demo.validations.rules.Max;
import com.example.demo.validations.rules.Min;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@FieldsValueMatch.List({
    @FieldsValueMatch(
        field="password",
        fieldMatch="confirmation_password",
        message="Password and confirmation password is mismatch."
    )
})
public class StoreTestRequest {
    @NotEmpty(message="Name is required.")
    private String name;

    @Email(message="Email is incorrect.")
    private String email;

    @Max(value=10, message="The phone field contains maximum 255 characters.", onlyfor={"string"})
    private String phone;

    @Max(value=10, message="The avatar field contains maximum 255 characters.")
    private String avatar;

    @NotEmpty(message="Password is required.")
    @Min(value=8, message="Password must contain at least 8 characters.")
    private String password;

    @Min(value=8, message="Confirmation password must contain at least 8 characters.")
    private String confirmation_password;
}
