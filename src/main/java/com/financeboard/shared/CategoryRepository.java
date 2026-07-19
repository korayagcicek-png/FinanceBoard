package com.financeboard.shared;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByTypeIn(List<CategoryType> types);
    Optional<Category> findByName(String name);
    List<Category> findByType(CategoryType type);
}
