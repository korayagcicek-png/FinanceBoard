package com.financeboard.shared;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("pageTitle", "Categories");
        return "categories/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("category", new Category());
        model.addAttribute("types", Category.CategoryType.values());
        model.addAttribute("pageTitle", "New Category");
        return "categories/form";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("category", categoryService.findById(id));
        model.addAttribute("types", Category.CategoryType.values());
        model.addAttribute("pageTitle", "Edit Category");
        return "categories/form";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute Category category,
                       BindingResult result,
                       Model model,
                       RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("types", Category.CategoryType.values());
            model.addAttribute("pageTitle", category.getId() == null ? "New Category" : "Edit Category");
            return "categories/form";
        }
        categoryService.save(category);
        redirectAttributes.addFlashAttribute("successMessage", "Category saved successfully.");
        return "redirect:/categories";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        categoryService.delete(id);
        redirectAttributes.addFlashAttribute("successMessage", "Category deleted.");
        return "redirect:/categories";
    }
}
