package com.financeboard.transactions;

import com.financeboard.shared.Category;
import com.financeboard.shared.CategoryService;
import com.financeboard.transactions.dto.TransactionForm;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;
    private final CategoryService categoryService;

    public TransactionController(TransactionService transactionService, CategoryService categoryService) {
        this.transactionService = transactionService;
        this.categoryService = categoryService;
    }

    @GetMapping
    public String list(
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Long categoryId,
            Model model) {

        List<Transaction> transactions = transactionService.findFiltered(year, month, categoryId);
        List<Category> categories = categoryService.findAll();

        model.addAttribute("transactions", transactions);
        model.addAttribute("categories", categories);
        model.addAttribute("selectedYear", year != null ? year : LocalDate.now().getYear());
        model.addAttribute("selectedMonth", month != null ? month : LocalDate.now().getMonthValue());
        model.addAttribute("selectedCategoryId", categoryId);
        model.addAttribute("currentYear", LocalDate.now().getYear());
        model.addAttribute("activePage", "transactions");

        return "transactions/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        TransactionForm form = new TransactionForm();
        form.setDate(LocalDate.now());
        form.setType("EXPENSE");
        addFormAttributes(model, form);
        model.addAttribute("editMode", false);
        return "transactions/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("form") TransactionForm form,
                         BindingResult result,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            addFormAttributes(model, form);
            model.addAttribute("editMode", false);
            return "transactions/form";
        }
        transactionService.save(form);
        redirectAttributes.addFlashAttribute("successMessage", "Transaction added successfully.");
        return "redirect:/transactions";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Transaction transaction = transactionService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Transaction not found: " + id));
        TransactionForm form = transactionService.toForm(transaction);
        addFormAttributes(model, form);
        model.addAttribute("editMode", true);
        return "transactions/form";
    }

    @PostMapping("/{id}/edit")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("form") TransactionForm form,
                         BindingResult result,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            addFormAttributes(model, form);
            model.addAttribute("editMode", true);
            return "transactions/form";
        }
        form.setId(id);
        transactionService.save(form);
        redirectAttributes.addFlashAttribute("successMessage", "Transaction updated successfully.");
        return "redirect:/transactions";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        transactionService.deleteById(id);
        redirectAttributes.addFlashAttribute("successMessage", "Transaction deleted.");
        return "redirect:/transactions";
    }

    private void addFormAttributes(Model model, TransactionForm form) {
        model.addAttribute("form", form);
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("types", TransactionType.values());
        model.addAttribute("activePage", "transactions");
    }
}
