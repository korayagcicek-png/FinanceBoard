package com.financeboard.shared;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findByTypeIn(List<Category.CategoryType> types);

    List<Category> findByType(Category.CategoryType type);

    boolean existsByName(String name);
}
