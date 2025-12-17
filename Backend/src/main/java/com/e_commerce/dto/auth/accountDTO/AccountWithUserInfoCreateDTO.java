package com.e_commerce.dto.auth.accountDTO;

import com.e_commerce.enums.AccountRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class AccountWithUserInfoCreateDTO {
    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String accountName;

    @NotBlank
    private String password;

    private AccountRole role;

    // UserInformation
    @NotBlank
    private String fullName;

    @NotBlank(message = "Phone number must not be blank")
    @Pattern(
            regexp = "^(0|\\+84)[0-9]{9}$",
            message = "Phone number is invalid"
    )
    private String phoneNumber;

    @NotBlank(message = "Address must not be blank")
    private String address;
    private String gender;
}
