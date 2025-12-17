package com.e_commerce.mapper.account;

import com.e_commerce.dto.auth.accountDTO.AccountDTO;
import com.e_commerce.dto.auth.accountDTO.AccountWithUserInfoCreateDTO;
import com.e_commerce.dto.auth.accountDTO.RegistrationForm;
import com.e_commerce.entity.account.Account;
import com.e_commerce.entity.account.UserInformation;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class AccountMapper {
    private final UserInformationMapper userInformationMapper;

    public AccountDTO convertEntityToDTO(Account account) {

        UserInformation userInfo = null;
        if (account.getUserInformation() != null && !account.getUserInformation().isEmpty()) {
            userInfo = account.getUserInformation().getFirst();
        }

        return AccountDTO.builder()
                .id(account.getId())
                .email(account.getUsername())
                .accountName(account.getAccountName())
                .createAt(account.getCreatedAt())
                .active(account.getActive().toString())
                .status(account.getStatus())
                .role(account.getRole().name())
                .userInformation(
                        userInfo == null ? null : userInformationMapper.convertEntityToDTO(userInfo)
                )
                .build();
    }


    public Account convertCreateDTOToEntity(RegistrationForm registrationForm) {
        return Account.builder()
                .email(registrationForm.getEmail())
                .accountName(registrationForm.getAccountName())
                .role(registrationForm.getRole())
                .status(true)
                .active(true)
                .build();
    }

    public List<AccountDTO> convertListEntityToListDTO(List<Account> accounts) {
        return accounts.stream()
                .map(this::convertEntityToDTO)
                .collect(Collectors.toList());
    }

    public Account convertAccountWithUserInfoCreateDTOToEntity(AccountWithUserInfoCreateDTO dto) {
        Account account = Account.builder()
                .email(dto.getEmail())
                .accountName(dto.getAccountName())
                .role(dto.getRole())
                .status(true)
                .active(true)
                .password(dto.getPassword())
                .build();

        account.setUserInformation(new ArrayList<>());

        return account;
    }

}

