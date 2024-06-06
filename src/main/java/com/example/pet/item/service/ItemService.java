package com.example.pet.item.service;

import com.example.pet.item.domain.Item;
import com.example.pet.item.repository.ItemRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.example.pet.common.type.ItemCategoryType.fromCategoryType;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional
    public List<Item> findAllByCategory(String category) {
        validateCategory(category);
        return itemRepository.findAllByCategory(category)
                .orElse(new ArrayList<>());
    }

    private void validateCategory(String category) {
        fromCategoryType(category);
    }
}
