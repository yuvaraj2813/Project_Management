package com.example.demo.Service;

import com.example.demo.DTO.LoginRequest;
import com.example.demo.DTO.LoginResponse;
import com.example.demo.DTO.RegisterRequest;
import com.example.demo.Entity.User;
import com.example.demo.Exceptions.InvalidCredentialsException;
import com.example.demo.Exceptions.ResourceAlreadyExistException;
import com.example.demo.Repository.UserRepository;
import com.example.demo.Security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private  final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public void register(RegisterRequest request){
        log.info("User Registraiton for email: {}",request.email());
        if(userRepository.findByEmail(request.email()).isPresent()){
            log.warn("Registration failed - User already Exist for email:{}",request.email());
            throw new ResourceAlreadyExistException("User already exist for email: "+request.email());
        }
        User user=new User();
        user.setName(request.name());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(request.role());
        log.info("Registration Successful for email:{}",request.email());
        userRepository.save(user);
    }

    public LoginResponse login(LoginRequest request){

        log.info("Login attempt for email:{}",request.email());

        User user=userRepository.findByEmail(request.email())
                .orElseThrow(()->{
                    log.warn("Login failed - email not found :{}",request.email());
                    return new InvalidCredentialsException();
                });

        if(!passwordEncoder.matches(request.password(),user.getPassword())){
            log.warn("Login failed - password mismatch:{}",request.email());
            throw new InvalidCredentialsException();
        }
        String token= jwtService.generateToken(user);
        log.info("Login Successfull for email: {}",request.email());
        return new LoginResponse(token,user.getRole().name());
    }
}
