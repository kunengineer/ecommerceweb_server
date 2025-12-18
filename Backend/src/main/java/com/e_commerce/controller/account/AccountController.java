package com.e_commerce.controller.account;

import com.e_commerce.dto.ApiResponse;
import com.e_commerce.dto.auth.accountDTO.*;
import com.e_commerce.service.account.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Account Controller", description = "Manage user accounts and authentication")
@Slf4j
public class AccountController {
    private final AccountService accountService;

    @PostMapping("/login")
    @Operation(
            summary = "Login",
            description = "Authenticate user and return access token",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Login form containing username and password",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = LoginForm.class)
                    )
            ),
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            description = "Successful login",
                            responseCode = "200",
                            content = @Content(
                                    schema = @Schema(implementation = ApiResponse.class)
                            )
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            description = "Invalid credentials",
                            responseCode = "401",
                            content = @Content
                    )
            }
    )
    public ResponseEntity<ApiResponse<AuthenticationDTO>> login(@Valid @RequestBody LoginForm loginForm,
            HttpServletRequest request) {
        AuthenticationDTO login = accountService.signIn(loginForm);
        return ResponseEntity.ok(new ApiResponse<>(true, "Login successfully", login, null, request.getRequestURI()));
    }

    @PostMapping("/register")
    @Operation(
            summary = "Register",
            description = "Create a new user account",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Registration form containing user details",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = RegistrationForm.class)
                    )
            ),
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            description = "Successful registration",
                            responseCode = "200",
                            content = @Content(
                                    schema = @Schema(implementation = ApiResponse.class)
                            )
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            description = "Registration error",
                            responseCode = "400",
                            content = @Content
                    )
            }
    )
    public ResponseEntity<ApiResponse<AccountDTO>> register(@Valid @RequestBody RegistrationForm registrationForm,
            HttpServletRequest request) {
        AccountDTO register = accountService.createAccount(registrationForm);
        return ResponseEntity
                .ok(new ApiResponse<>(true, "Register successfully", register, null, request.getRequestURI()));
    }

    @PostMapping("/logout")
    @Operation(
            summary = "Logout",
            description = "Invalidate user session and token",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            description = "Successful logout",
                            responseCode = "200",
                            content = @Content(
                                    schema = @Schema(implementation = ApiResponse.class)
                            )
                    )
            }
    )
    public ResponseEntity<ApiResponse<Void>> logout(@RequestHeader("Authorization") String token,
            HttpServletRequest request) {
        String jwt = token.startsWith("Bearer ") ? token.substring(7) : token;
        accountService.logout(jwt);
        return ResponseEntity.ok(new ApiResponse<>(
                true,
                "Logged out successfully",
                null,
                null,
                request.getRequestURI()));
    }

     @GetMapping("/all-user")
     @Operation(
             summary = "Get All Users",
             description = "Retrieve a list of all users with role USER",
             responses = {
                     @io.swagger.v3.oas.annotations.responses.ApiResponse(
                             description = "Successful retrieval of users",
                             responseCode = "200",
                             content = @Content(
                                     schema = @Schema(implementation = ApiResponse.class)
                             )
                     )
             }
     )
    public ResponseEntity<ApiResponse<List<AccountDTO>>> getAllUser(HttpServletRequest request) {
        List<AccountDTO> accountDTOList = accountService.getAccountAllByRoleUser();
        return ResponseEntity.ok(new ApiResponse<>(true, "Get all user successfully", accountDTOList, null, request.getRequestURI()));
    }

    @GetMapping
    @Operation(
            summary = "Get Customers",
            description = "Retrieve a list of all customers",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            description = "Successful retrieval of customers",
                            responseCode = "200",
                            content = @Content(
                                    schema = @Schema(implementation = ApiResponse.class)
                            )
                    )
            }
    )
    public ResponseEntity<ApiResponse<List<AccountDTO>>> getCustomers(HttpServletRequest request) {
        List<AccountDTO> customerInfo = accountService.getCustomerInfoList();
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Get customer info successfully", customerInfo, null, request.getRequestURI()));

    }

    @PostMapping("/refresh-token")
    @Operation(
            summary = "Refresh Token",
            description = "Refresh the authentication token using a valid refresh token",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Refresh token data transfer object",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = RefreshTokenDTO.class)
                    )
            ),
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            description = "Successful token refresh",
                            responseCode = "200",
                            content = @Content(
                                    schema = @Schema(implementation = ApiResponse.class)
                            )
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            description = "Invalid refresh token",
                            responseCode = "400",
                            content = @Content
                    )
            }
    )
    public ResponseEntity<ApiResponse<AuthenticationDTO>> refreshToken(@RequestBody @Valid RefreshTokenDTO refreshTokenDTO,
            HttpServletRequest request) {
        AuthenticationDTO authenticationDTO = accountService.refreshToken(refreshTokenDTO);
        return ResponseEntity.ok(new ApiResponse<>(true, "Token refreshed successfully", authenticationDTO, null, request.getRequestURI()));
    }

    @GetMapping("/activate")
    @Operation(
            summary = "Activate Account",
            description = "Activate user account using activation token",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            description = "Successful account activation",
                            responseCode = "200",
                            content = @Content(
                                    schema = @Schema(implementation = ApiResponse.class)
                            )
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            description = "Invalid or expired activation token",
                            responseCode = "400",
                            content = @Content
                    )
            }
    )
    public ResponseEntity<ApiResponse<Void>> activateAccount(@RequestParam("token") String token, HttpServletRequest request) {
        accountService.activeAccount(token);

        return ResponseEntity.ok(new ApiResponse<>(true, "Account activated successfully", null, null, request.getRequestURI()));
    }

    @PostMapping("/forgot-password")
    @Operation(
            summary = "Forgot Password",
            description = "Initiate password reset process by sending a reset email",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Forgot password request data transfer object",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = ForgotPasswordRequestDTO.class)
                    )
            ),
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            description = "Successful forgot password request",
                            responseCode = "200",
                            content = @Content(
                                    schema = @Schema(implementation = ApiResponse.class)
                            )
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            description = "Email not found",
                            responseCode = "404",
                            content = @Content
                    )
            }
    )
    public ResponseEntity<ApiResponse<ForgotPasswordResponseDTO>> forgotPassword(@RequestBody @Valid ForgotPasswordRequestDTO forgotPasswordDTO, HttpServletRequest request) {
        ForgotPasswordResponseDTO responseDTO = accountService.forgotPasswordRequest(forgotPasswordDTO);
        return ResponseEntity.ok(new ApiResponse<>(true, "Forgot password email sent successfully", responseDTO, null, request.getRequestURI()));
    }

    @PostMapping("/reset-password")
    @Operation(
            summary = "Reset Password",
            description = "Reset user password using OTP and new password",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Reset password data transfer object",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = ResetPasswordDTO.class)
                    )
            ),
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            description = "Successful password reset",
                            responseCode = "200",
                            content = @Content(
                                    schema = @Schema(implementation = ApiResponse.class)
                            )
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            description = "Invalid OTP or password reset error",
                            responseCode = "400",
                            content = @Content
                    )
            }
    )
    public ResponseEntity<ApiResponse<Void>> resetPassword(@RequestBody @Valid ResetPasswordDTO resetPasswordDTO, HttpServletRequest request) {
        accountService.resetPassword(resetPasswordDTO);
        return ResponseEntity.ok(new ApiResponse<>(true, "Password reset successfully", null, null, request.getRequestURI()));
    }

    @PostMapping("/verify-otp")
    @Operation(
            summary = "Verify OTP",
            description = "Verify the one-time password (OTP) sent to the user",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "OTP verification data transfer object",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = OtpVerificationRequestDTO.class)
                    )
            ),
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            description = "Successful OTP verification",
                            responseCode = "200",
                            content = @Content(
                                    schema = @Schema(implementation = ApiResponse.class)
                            )
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            description = "Invalid OTP",
                            responseCode = "400",
                            content = @Content
                    )
            }
    )
    public ResponseEntity<ApiResponse<OtpVerificationResponseDTO>> verifyOtp(@RequestBody @Valid OtpVerificationRequestDTO otpVerificationDTO, HttpServletRequest request) {
        OtpVerificationResponseDTO responseDTO = accountService.verifyOtp(otpVerificationDTO);
        return ResponseEntity.ok(new ApiResponse<>(true, "OTP verified successfully", responseDTO, null, request.getRequestURI()));
    }

    @PostMapping("/resend-verification")
    @Operation(
            summary = "Resend Verification Email",
            description = "Resend the account verification email to the user",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            description = "Successful resend of verification email",
                            responseCode = "200",
                            content = @Content(
                                    schema = @Schema(implementation = ApiResponse.class)
                            )
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            description = "Email not found",
                            responseCode = "404",
                            content = @Content
                    )
            }
    )
    public ResponseEntity<ApiResponse<Void>> resendVerificationEmail(@RequestParam("email") String email, HttpServletRequest request) {
        accountService.resendVerificationEmail(email);
        return ResponseEntity.ok(new ApiResponse<>(true, "Verification email resent successfully", null, null, request.getRequestURI()));
    }

    @PutMapping("/{id}/lock")
    @Operation(
            summary = "Lock Account",
            description = "Lock the user account to prevent access",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            description = "Successful account lock",
                            responseCode = "200",
                            content = @Content(
                                    schema = @Schema(implementation = ApiResponse.class)
                            )
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            description = "Account not found",
                            responseCode = "404",
                            content = @Content
                    )
            }
    )
    public ResponseEntity<ApiResponse<Void>> lockAccount(@PathVariable int id, HttpServletRequest request) {
        accountService.lockAccount(id);
        return ResponseEntity.ok(new ApiResponse<>(true,"Account locked successfully", null, null, request.getRequestURI()));
    }

    @PutMapping("/{id}/unlock")
    @Operation(
            summary = "Unlock Account",
            description = "Unlock the user account to restore access",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            description = "Successful account unlock",
                            responseCode = "200",
                            content = @Content(
                                    schema = @Schema(implementation = ApiResponse.class)
                            )
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            description = "Account not found",
                            responseCode = "404",
                            content = @Content
                    )
            }
    )
    public ResponseEntity<ApiResponse<Void>> unlockAccount(@PathVariable int id, HttpServletRequest request) {
        accountService.unlockAccount(id);
        return ResponseEntity.ok(new ApiResponse<>(true,"Account unlocked successfully", null, null, request.getRequestURI()));
    }

    @GetMapping("users/detail")
    @Operation(
            summary = "Get Account by ID",
            description = "Retrieve account details by account ID",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            description = "Successful retrieval of account",
                            responseCode = "200",
                            content = @Content(
                                    schema = @Schema(implementation = ApiResponse.class)
                            )
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            description = "Account not found",
                            responseCode = "404",
                            content = @Content
                    )
            }
    )
    public ResponseEntity<ApiResponse<AccountDTO>> getAccountById( @RequestParam("id") int id, HttpServletRequest request) {
        AccountDTO accountDTO = accountService.getAccountDTOById(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Get account by ID successfully", accountDTO, null, request.getRequestURI()));
    }

    @PostMapping("/users")
    @Operation(
            summary = "Create Account with User Info",
            description = "Create a new account along with associated user information",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Account with user information data transfer object",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = AccountWithUserInfoCreateDTO.class)
                    )
            ),
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            description = "Successful account creation",
                            responseCode = "201",
                            content = @Content(
                                    schema = @Schema(implementation = ApiResponse.class)
                            )
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            description = "Account creation error",
                            responseCode = "400",
                            content = @Content
                    )
            }
    )
    public ResponseEntity<ApiResponse<AccountDTO>> createAccountWithUserInfo(
            @RequestBody @Valid AccountWithUserInfoCreateDTO dto,
            HttpServletRequest request) {

        AccountDTO result = accountService.createAccountWithUserInfo(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Create account with user information successfully", result, null, request.getRequestURI()));
    }

    @PutMapping("/users")
    @Operation(
            summary = "Update Account with User Info",
            description = "Update an existing account along with associated user information",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Account with user information update data transfer object",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = AccountWithUserInfoUpdateDTO.class)
                    )
            ),
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            description = "Successful account update",
                            responseCode = "200",
                            content = @Content(
                                    schema = @Schema(implementation = ApiResponse.class)
                            )
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            description = "Account update error",
                            responseCode = "400",
                            content = @Content
                    )
            }
    )
    public ResponseEntity<ApiResponse<AccountDTO>> updateAccountWithUserInfo(
            @RequestParam("id") int accountId,
            @RequestBody @Valid AccountWithUserInfoUpdateDTO dto,
            HttpServletRequest request) {

        AccountDTO result = accountService.updateAccountWithUserInfo(accountId, dto);

        return ResponseEntity.ok(new ApiResponse<>(true, "Update account with user information successfully", result, null, request.getRequestURI()));
    }

    @DeleteMapping("/users")
    @Operation(
            summary = "Delete Account by ID",
            description = "Delete an account using its ID",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            description = "Successful account deletion",
                            responseCode = "200",
                            content = @Content(
                                    schema = @Schema(implementation = ApiResponse.class)
                            )
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            description = "Account deletion error",
                            responseCode = "400",
                            content = @Content
                    )
            }
    )
    public ResponseEntity<ApiResponse<Void>> deleteAccountById(
            @RequestParam("id") Integer accountId,
            HttpServletRequest request) {
        accountService.deleteByAccountId(accountId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Delete account successfully", null, null, request.getRequestURI()));
    }

}
