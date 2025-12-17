package com.e_commerce.dto.auth.accountDTO;

import com.e_commerce.enums.AccountRole;
import com.e_commerce.enums.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class AccountWithUserInfoUpdateDTO {
    @NotBlank
    private String accountName;

    private AccountRole role;

    // UserInformation
    @NotBlank
    private String fullName;

    @NotBlank
    @Pattern(
            regexp = "^(0|\\+84)[0-9]{9}$",
            message = "Phone number is invalid"
    )
    private String phoneNumber;

    @NotBlank
    private String address;

    private Gender gender;
}
