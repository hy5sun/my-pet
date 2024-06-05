package com.example.pet.pet.controller;

import com.example.pet.common.annotation.Login;
import com.example.pet.common.response.CustomResponse;
import com.example.pet.member.domain.Member;
import com.example.pet.pet.dto.CreatePetRequest;
import com.example.pet.pet.dto.DetailPetResponse;
import com.example.pet.pet.service.PetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pets")
@RequiredArgsConstructor
public class PetController {

    private final PetService petService;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public CustomResponse create(@Validated @RequestBody CreatePetRequest req, @Login Member member) {
        DetailPetResponse pet = petService.createPet(member, req);
        return CustomResponse.response(HttpStatus.CREATED, "동물 정보를 정상적으로 등록했습니다.", pet);
    }
}
