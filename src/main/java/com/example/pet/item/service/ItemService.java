package com.example.pet.item.service;

import com.example.pet.item.dto.ItemDto;
import com.example.pet.item.repository.ItemRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.example.pet.common.type.ItemCategoryType.fromCategoryType;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional
    public List<ItemDto> findByAllCategory(String category) {
        validateCategory(category);
        if (category.substring(4, 8).equals("BEST")) {
            return findBestItem(category);
        }
        return findByItemCategory(category);
    }

    private List<ItemDto> findByItemCategory(String category) {
        return itemRepository.findAllByCategory(category)
                .orElse(new ArrayList<>())
                .stream()
                .map(ItemDto::toDto)
                .toList();
    }

    private List<ItemDto> findBestItem(String category) {
        String species = category.substring(0, 3);
        return itemRepository.findAllByIsBestAndCategoryStartsWith(true, species)
                .orElse(new ArrayList<>())
                .stream()
                .map(ItemDto::toDto)
                .toList();
    }

    private void validateCategory(String category) {
        fromCategoryType(category);
    }
}
