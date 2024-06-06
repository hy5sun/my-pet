package com.example.pet.item.repository;

import com.example.pet.item.domain.Item;
import com.example.pet.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ItemRepository extends JpaRepository<Item, UUID> {
    Optional<List<Item>> findAllByCategory(String category);
}
