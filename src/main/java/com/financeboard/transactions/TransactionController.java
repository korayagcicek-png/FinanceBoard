package com.financeboard.transactions;

import com.financeboard.shared.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Controller
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;
    private final CategoryService categoryService;

    @GetMapping
    public String list(@RequestParam(required = false) Integer year,
                       @RequestParam(required = false) Integer month,
                       @RequestParam(required = false) Long categoryId,
                       @RequestParam(required = false) String type,
                       Model model) {
        YearMonth current = YearMonth.now();
        int selectedYear = (year != null) ? year : current.getYear();
        int selectedMonth = (month != null) ? month : current.getMonthValue();

        List<Transaction> transactions;
        if (categoryId != null || (type != null && !type.isBlank())) {
            transactions = transactionService.findFiltered(categoryId, type, selectedYear, selectedMonth);
        } else {
            transactions = transactionService.findByMonth(selectedYear, selectedMonth);
        }

        model.addAttribute("transactions", transactions);
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("selectedYear", selectedYear);
        model.addAttribute("selectedMonth", selectedMonth);
        model.addAttribute("selectedCategoryId", categoryId);
        model.addAttribute("selectedType", type);
        model.addAttribute("totalIncome", transactionService.getTotalIncome(selectedYear, selectedMonth));
        model.addAttribute("totalExpenses", transactionService.getTotalExpenses(selectedYear, selectedMonth));
        model.addAttribute("pageTitle", "Transactions");
        return "transactions/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        Transaction transaction = new Transaction();
        transaction.setDate(LocalDate.now());
        model.addAttribute("transaction", transaction);
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("types", Transaction.TransactionType.values());
        model.addAttribute("pageTitle", "New Transaction");
        return "transactions/form";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("transaction", transactionService.findById(id));
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("types", Transaction.TransactionType.values());
        model.addAttribute("pageTitle", "Edit Transaction");
        return "transactions/form";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute Transaction transaction,
                       BindingResult result,
                       Model model,
                       RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("categories", categoryService.findAll());
            model.addAttribute("types", Transaction.TransactionType.values());
            model.addAttribute("pageTitle", transaction.getId() == null ? "New Transaction" : "Edit Transaction");
            return "transactions/form";
        }
        transactionService.save(transaction);
        redirectAttributes.addFlashAttribute("successMessage", "Transaction saved successfully.");
        return "redirect:/transactions";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        transactionService.delete(id);
        redirectAttributes.addFlashAttribute("successMessage", "Transaction deleted.");
        return "redirect:/transactions";
    }
}
