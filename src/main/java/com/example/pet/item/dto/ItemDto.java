package com.example.pet.item.dto;

import com.example.pet.item.domain.Item;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ItemDto {
    private String name;
    private String url ;
    private String image_url ;
    private String price;
    private String category;

    public static ItemDto toDto(Item item) {
        return new ItemDto(item.getName(), item.getUrl(), item.getImage_url(), item.getPrice(), item.getCategory());
    }
}
