package com.e_commerce.dto.auth.accountDTO;

import com.e_commerce.dto.auth.userInfoDTO.UserInfoDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountDTO {
    private Integer id;
    private String email;
    private String accountName;
    private LocalDateTime createAt;
    private Boolean status;
    private String role;
    private String active;
    private UserInfoDTO userInformation;
}
