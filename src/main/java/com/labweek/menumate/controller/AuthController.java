package com.labweek.menumate.controller;

import com.labweek.menumate.config.UserAuthProvider;
import com.labweek.menumate.dto.CredentialsDto;
import com.labweek.menumate.dto.SignUpDto;
import com.labweek.menumate.dto.UserDto;
import com.labweek.menumate.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    private final UserAuthProvider userAuthProvider;
    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody CredentialsDto credentialsDto){

        UserDto user = userService.login(credentialsDto);

        user.setToken(userAuthProvider.createToken(user.getEmail()));

        return ResponseEntity.ok(user);
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody SignUpDto signUpDto){
        UserDto user = userService.register(signUpDto);
        user.setToken(userAuthProvider.createToken(user.getEmail()));

        return ResponseEntity.created(URI.create("/users/" + user.getNtId()))
                .body(user);

    }

}
