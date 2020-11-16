package com.andrii.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.andrii.demo.entity.Category;
import com.andrii.demo.entity.Item;

public interface ItemRepository extends JpaRepository<Item, Long> {
	@Query("SELECT u FROM Item u WHERE u.category = :category")
	List<Item> findItemsByCategory(@Param("category") Category category);
}
