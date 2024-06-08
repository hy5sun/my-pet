package com.example.pet.auth.controller;

import com.example.pet.auth.dto.JoinRequest;
import com.example.pet.auth.dto.JoinResponse;
import com.example.pet.auth.dto.LoginRequest;
import com.example.pet.auth.dto.LoginResponse;
import com.example.pet.auth.service.AuthService;
import com.example.pet.common.response.CustomResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/join")
    @ResponseStatus(HttpStatus.CREATED)
    public CustomResponse join(@Validated @RequestBody JoinRequest req) {
        JoinResponse savedMember = authService.join(req);
        return CustomResponse.response(HttpStatus.CREATED, "회원가입에 성공하셨습니다.", savedMember);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public CustomResponse login (@Validated @RequestBody LoginRequest req) {
        LoginResponse tokens = authService.signIn(req);
        return CustomResponse.response(HttpStatus.OK, "로그인에 성공하셨습니다.", tokens);
    }
}
