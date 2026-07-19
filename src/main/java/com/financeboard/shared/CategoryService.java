package com.financeboard.shared;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public List<Category> findByType(CategoryType type) {
        return categoryRepository.findByType(type);
    }

    public List<Category> findExpenseCategories() {
        return categoryRepository.findByTypeIn(List.of(CategoryType.EXPENSE, CategoryType.BOTH));
    }

    public List<Category> findIncomeCategories() {
        return categoryRepository.findByTypeIn(List.of(CategoryType.INCOME, CategoryType.BOTH));
    }

    public Optional<Category> findById(Long id) {
        return categoryRepository.findById(id);
    }

    public Optional<Category> findByName(String name) {
        return categoryRepository.findByName(name);
    }

    @Transactional
    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    @Transactional
    public void deleteById(Long id) {
        categoryRepository.deleteById(id);
    }
}
