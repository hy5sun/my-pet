package com.example.pet.item.controller;

import com.example.pet.common.annotation.Login;
import com.example.pet.common.response.CustomResponse;
import com.example.pet.item.dto.ItemDto;
import com.example.pet.item.service.ItemService;
import com.example.pet.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/{category}")
    @ResponseStatus(HttpStatus.OK)
    public CustomResponse findAllByCategory(@PathVariable("category") String category, @Login Member member) {
        List<ItemDto> items = itemService.findByAllCategory(category);
        return CustomResponse.response(HttpStatus.OK, category + " 조회를 정상적으로 했습니다.", items);
    }
}
