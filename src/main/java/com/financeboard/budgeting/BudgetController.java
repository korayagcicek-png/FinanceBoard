package com.financeboard.budgeting;

import com.financeboard.budgeting.dto.BudgetForm;
import com.financeboard.budgeting.dto.BudgetStatus;
import com.financeboard.shared.CategoryService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/budgets")
public class BudgetController {

    private final BudgetService budgetService;
    private final CategoryService categoryService;

    public BudgetController(BudgetService budgetService, CategoryService categoryService) {
        this.budgetService = budgetService;
        this.categoryService = categoryService;
    }

    @GetMapping
    public String list(
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month,
            Model model) {

        int selectedYear = year != null ? year : LocalDate.now().getYear();
        int selectedMonth = month != null ? month : LocalDate.now().getMonthValue();

        List<BudgetStatus> budgetStatuses = budgetService.getBudgetStatusForMonth(selectedYear, selectedMonth);

        model.addAttribute("budgetStatuses", budgetStatuses);
        model.addAttribute("selectedYear", selectedYear);
        model.addAttribute("selectedMonth", selectedMonth);
        model.addAttribute("currentYear", LocalDate.now().getYear());
        model.addAttribute("activePage", "budgets");

        return "budgets/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        BudgetForm form = new BudgetForm();
        form.setYear(LocalDate.now().getYear());
        form.setMonth(LocalDate.now().getMonthValue());
        addFormAttributes(model, form);
        return "budgets/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("form") BudgetForm form,
                         BindingResult result,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            addFormAttributes(model, form);
            return "budgets/form";
        }
        budgetService.save(form);
        redirectAttributes.addFlashAttribute("successMessage", "Budget saved successfully.");
        return "redirect:/budgets";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Budget budget = budgetService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Budget not found: " + id));
        BudgetForm form = budgetService.toForm(budget);
        addFormAttributes(model, form);
        model.addAttribute("editMode", true);
        return "budgets/form";
    }

    @PostMapping("/{id}/edit")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("form") BudgetForm form,
                         BindingResult result,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            addFormAttributes(model, form);
            model.addAttribute("editMode", true);
            return "budgets/form";
        }
        form.setId(id);
        budgetService.save(form);
        redirectAttributes.addFlashAttribute("successMessage", "Budget updated successfully.");
        return "redirect:/budgets";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        budgetService.deleteById(id);
        redirectAttributes.addFlashAttribute("successMessage", "Budget deleted.");
        return "redirect:/budgets";
    }

    private void addFormAttributes(Model model, BudgetForm form) {
        model.addAttribute("form", form);
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("activePage", "budgets");
    }
}
