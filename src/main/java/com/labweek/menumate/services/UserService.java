package com.labweek.menumate.services;

import com.labweek.menumate.dto.CredentialsDto;
import com.labweek.menumate.dto.SignUpDto;
import com.labweek.menumate.dto.UserDto;
import com.labweek.menumate.entity.UserEntity;
import com.labweek.menumate.exceptions.AppException;
import com.labweek.menumate.mappers.UserMapper;
import com.labweek.menumate.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.CharBuffer;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepo userRepo;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    public UserDto findByEmail(String email){

        UserEntity appuser = userRepo.findByEmail(email)
                .orElseThrow(()-> new AppException("Unknown user", HttpStatus.NOT_FOUND));

        return userMapper.toUserDto(appuser);

    }

    public UserDto login(CredentialsDto credentialsDto){

        UserEntity appuser = userRepo.findByEmail(credentialsDto.getEmail())
                .orElseThrow(()-> new AppException("unknown user",HttpStatus.NOT_FOUND));

        if(passwordEncoder.matches(credentialsDto.getPassword(),appuser.getPassword())){
            return userMapper.toUserDto(appuser);
        }
        throw new AppException("INCORRECT PASSWORD!",HttpStatus.BAD_REQUEST);
    }

    public UserDto register(SignUpDto signUpUserDto){

        Optional<UserEntity> optionalUser = userRepo.findByEmail(signUpUserDto.getEmail());

        if(optionalUser.isPresent()){
            throw new AppException("User already exists!", HttpStatus.BAD_REQUEST);
        }

        UserEntity appUser = userMapper.signUpToUser(signUpUserDto);

        appUser.setPassword(passwordEncoder.encode(signUpUserDto.getPassword()));;

        UserEntity savedUser = userRepo.save(appUser);

        return userMapper.toUserDto(appUser);

    }
}
