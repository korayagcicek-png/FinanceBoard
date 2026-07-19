package com.financeboard.goals;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;

@Controller
@RequestMapping("/goals")
@RequiredArgsConstructor
public class GoalController {

    private final GoalService goalService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("goals", goalService.findAll());
        model.addAttribute("pageTitle", "Savings Goals");
        return "goals/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("goal", new Goal());
        model.addAttribute("statuses", Goal.GoalStatus.values());
        model.addAttribute("pageTitle", "New Goal");
        return "goals/form";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("goal", goalService.findById(id));
        model.addAttribute("statuses", Goal.GoalStatus.values());
        model.addAttribute("pageTitle", "Edit Goal");
        return "goals/form";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute Goal goal,
                       BindingResult result,
                       Model model,
                       RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("statuses", Goal.GoalStatus.values());
            model.addAttribute("pageTitle", goal.getId() == null ? "New Goal" : "Edit Goal");
            return "goals/form";
        }
        goalService.save(goal);
        redirectAttributes.addFlashAttribute("successMessage", "Goal saved successfully.");
        return "redirect:/goals";
    }

    @PostMapping("/{id}/contribute")
    public String addContribution(@PathVariable Long id,
                                  @RequestParam BigDecimal amount,
                                  RedirectAttributes redirectAttributes) {
        goalService.addContribution(id, amount);
        redirectAttributes.addFlashAttribute("successMessage", "Contribution added successfully.");
        return "redirect:/goals";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        goalService.delete(id);
        redirectAttributes.addFlashAttribute("successMessage", "Goal deleted.");
        return "redirect:/goals";
    }
}
