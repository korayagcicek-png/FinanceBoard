package com.financeboard.budgeting;

import com.financeboard.shared.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.Map;

@Controller
@RequestMapping("/budgets")
@RequiredArgsConstructor
public class BudgetController {

    private final BudgetService budgetService;
    private final CategoryService categoryService;

    @GetMapping
    public String list(@RequestParam(required = false) Integer year,
                       @RequestParam(required = false) Integer month,
                       Model model) {
        YearMonth current = YearMonth.now();
        int selectedYear = (year != null) ? year : current.getYear();
        int selectedMonth = (month != null) ? month : current.getMonthValue();

        Map<Budget, BigDecimal> budgetWithSpending = budgetService.getBudgetWithSpending(selectedYear, selectedMonth);
        model.addAttribute("budgetWithSpending", budgetWithSpending);
        model.addAttribute("selectedYear", selectedYear);
        model.addAttribute("selectedMonth", selectedMonth);
        model.addAttribute("pageTitle", "Budgets");
        return "budgets/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        YearMonth current = YearMonth.now();
        Budget budget = new Budget();
        budget.setMonth(current.getMonthValue());
        budget.setYear(current.getYear());
        model.addAttribute("budget", budget);
        model.addAttribute("categories", categoryService.findExpenseCategories());
        model.addAttribute("pageTitle", "New Budget");
        return "budgets/form";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("budget", budgetService.findById(id));
        model.addAttribute("categories", categoryService.findExpenseCategories());
        model.addAttribute("pageTitle", "Edit Budget");
        return "budgets/form";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute Budget budget,
                       BindingResult result,
                       Model model,
                       RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("categories", categoryService.findExpenseCategories());
            model.addAttribute("pageTitle", budget.getId() == null ? "New Budget" : "Edit Budget");
            return "budgets/form";
        }
        budgetService.save(budget);
        redirectAttributes.addFlashAttribute("successMessage", "Budget saved successfully.");
        return "redirect:/budgets";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        budgetService.delete(id);
        redirectAttributes.addFlashAttribute("successMessage", "Budget deleted.");
        return "redirect:/budgets";
    }
}
