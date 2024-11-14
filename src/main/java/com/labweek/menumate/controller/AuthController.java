package com.labweek.menumate.controller;

import com.labweek.menumate.config.UserAuthProvider;
import com.labweek.menumate.dto.CredentialsDto;
import com.labweek.menumate.dto.NewProductDto;
import com.labweek.menumate.dto.SignUpDto;
import com.labweek.menumate.dto.UserDto;
import com.labweek.menumate.services.EmailService;
import com.labweek.menumate.services.OtpService;
import com.labweek.menumate.services.ProductService;
import org.springframework.web.bind.annotation.RequestParam;
import com.labweek.menumate.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final OtpService otpService;
    private final EmailService emailService;

    private final UserAuthProvider userAuthProvider;


    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody CredentialsDto credentialsDto) {

        UserDto user = userService.login(credentialsDto);
        user.setToken(userAuthProvider.createToken(user.getEmail()));

        return ResponseEntity.ok(user);
    }

//    @PostMapping("/register")
//    public ResponseEntity<UserDto> register(@RequestBody SignUpDto signUpDto) {
//        UserDto user = userService.register(signUpDto);
//        user.setToken(userAuthProvider.createToken(user.getEmail()));
//
//        return ResponseEntity.created(URI.create("/users/" + user.getNtId()))
//                .body(user);
//
//        }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody SignUpDto signUpDto) {
        // Generate a 4-digit OTP
        int otp = (int)(Math.random() * 9000) + 1000;

        // Save OTP for the provided email in memory
        otpService.saveOtp(signUpDto.getEmail(), otp);

        // Send OTP to the user's email
        emailService.sendOtpEmail(signUpDto.getEmail(), otp);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body("OTP sent to your registered email. Please confirm to complete registration.");
    }

    @PostMapping("/confirm-otp")
    public ResponseEntity<String> confirmOtp(@RequestParam String email, @RequestParam int otp, @RequestBody SignUpDto signUpDto) {
        boolean isValid = otpService.validateOtp(email, otp);

        if (!isValid) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid or expired OTP. Please try again.");
        }

        // OTP is valid; proceed to register the user in the database
        UserDto user = userService.register(signUpDto);
        otpService.clearOtp(email); // Clean up OTP from memory after successful registration

        return ResponseEntity.ok("Registration confirmed. You can now log in.");
    }


    @Autowired
    private ProductService productService;


    @GetMapping("/user")
    public ResponseEntity<List<NewProductDto>> getProductsForLoggedInUser() {
        // Get the current authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDto userDto = (UserDto) authentication.getPrincipal();

        String loggedInNtId = userDto.getNtId();// Retrieve the ntId of the currently logged-in user
        System.out.println("Logged-in ntId: " + loggedInNtId);

        // Fetch the products for the logged-in user by ntId
        List<NewProductDto> products = userService.getProductsByUser(loggedInNtId);

        if (products.isEmpty()) {
            return ResponseEntity.noContent().build(); // Return 204 No Content if no products are found
        }
        return ResponseEntity.ok(products); // Return the products
    }

    }



