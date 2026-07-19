package com.financeboard.projects;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("projects", projectService.findAll());
        model.addAttribute("pageTitle", "Financial Projects");
        return "projects/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("project", new FinancialProject());
        model.addAttribute("statuses", FinancialProject.ProjectStatus.values());
        model.addAttribute("pageTitle", "New Project");
        return "projects/form";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("project", projectService.findById(id));
        model.addAttribute("statuses", FinancialProject.ProjectStatus.values());
        model.addAttribute("pageTitle", "Edit Project");
        return "projects/form";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute FinancialProject project,
                       BindingResult result,
                       Model model,
                       RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("statuses", FinancialProject.ProjectStatus.values());
            model.addAttribute("pageTitle", project.getId() == null ? "New Project" : "Edit Project");
            return "projects/form";
        }
        projectService.save(project);
        redirectAttributes.addFlashAttribute("successMessage", "Project saved successfully.");
        return "redirect:/projects";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        projectService.delete(id);
        redirectAttributes.addFlashAttribute("successMessage", "Project deleted.");
        return "redirect:/projects";
    }
}
