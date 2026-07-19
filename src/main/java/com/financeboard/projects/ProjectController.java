package com.financeboard.projects;

import com.financeboard.projects.dto.ProjectForm;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("projects", projectService.findAll());
        model.addAttribute("activePage", "projects");
        return "projects/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        ProjectForm form = new ProjectForm();
        addFormAttributes(model, form);
        return "projects/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("form") ProjectForm form,
                         BindingResult result,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            addFormAttributes(model, form);
            return "projects/form";
        }
        projectService.save(form);
        redirectAttributes.addFlashAttribute("successMessage", "Project created successfully.");
        return "redirect:/projects";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        FinancialProject project = projectService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Project not found: " + id));
        ProjectForm form = projectService.toForm(project);
        addFormAttributes(model, form);
        model.addAttribute("editMode", true);
        return "projects/form";
    }

    @PostMapping("/{id}/edit")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("form") ProjectForm form,
                         BindingResult result,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            addFormAttributes(model, form);
            model.addAttribute("editMode", true);
            return "projects/form";
        }
        form.setId(id);
        projectService.save(form);
        redirectAttributes.addFlashAttribute("successMessage", "Project updated successfully.");
        return "redirect:/projects";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        projectService.deleteById(id);
        redirectAttributes.addFlashAttribute("successMessage", "Project deleted.");
        return "redirect:/projects";
    }

    private void addFormAttributes(Model model, ProjectForm form) {
        model.addAttribute("form", form);
        model.addAttribute("statuses", ProjectStatus.values());
        model.addAttribute("activePage", "projects");
    }
}
