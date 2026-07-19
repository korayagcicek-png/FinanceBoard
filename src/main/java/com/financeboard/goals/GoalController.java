package com.financeboard.goals;

import com.financeboard.goals.dto.GoalForm;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/goals")
public class GoalController {

    private final GoalService goalService;

    public GoalController(GoalService goalService) {
        this.goalService = goalService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("goals", goalService.findAll());
        model.addAttribute("activePage", "goals");
        return "goals/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        GoalForm form = new GoalForm();
        addFormAttributes(model, form);
        return "goals/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("form") GoalForm form,
                         BindingResult result,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            addFormAttributes(model, form);
            return "goals/form";
        }
        goalService.save(form);
        redirectAttributes.addFlashAttribute("successMessage", "Goal created successfully.");
        return "redirect:/goals";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Goal goal = goalService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Goal not found: " + id));
        GoalForm form = goalService.toForm(goal);
        addFormAttributes(model, form);
        model.addAttribute("editMode", true);
        return "goals/form";
    }

    @PostMapping("/{id}/edit")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("form") GoalForm form,
                         BindingResult result,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            addFormAttributes(model, form);
            model.addAttribute("editMode", true);
            return "goals/form";
        }
        form.setId(id);
        goalService.save(form);
        redirectAttributes.addFlashAttribute("successMessage", "Goal updated successfully.");
        return "redirect:/goals";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        goalService.deleteById(id);
        redirectAttributes.addFlashAttribute("successMessage", "Goal deleted.");
        return "redirect:/goals";
    }

    private void addFormAttributes(Model model, GoalForm form) {
        model.addAttribute("form", form);
        model.addAttribute("statuses", GoalStatus.values());
        model.addAttribute("activePage", "goals");
    }
}
