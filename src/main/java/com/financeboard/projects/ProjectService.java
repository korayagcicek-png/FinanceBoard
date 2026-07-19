package com.financeboard.projects;

import com.financeboard.projects.dto.ProjectForm;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class ProjectService {

    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public List<FinancialProject> findAll() {
        return projectRepository.findAllByOrderByStatusAscDeadlineAsc();
    }

    public Optional<FinancialProject> findById(Long id) {
        return projectRepository.findById(id);
    }

    @Transactional
    public FinancialProject save(ProjectForm form) {
        FinancialProject project = new FinancialProject();
        if (form.getId() != null) {
            project = projectRepository.findById(form.getId()).orElse(new FinancialProject());
        }
        project.setName(form.getName());
        project.setEstimatedCost(form.getEstimatedCost());
        project.setDeadline(form.getDeadline());
        project.setProgress(form.getProgress() != null ? form.getProgress() : 0);
        project.setNotes(form.getNotes());
        project.setStatus(ProjectStatus.valueOf(form.getStatus()));
        if (project.getCreatedAt() == null) {
            project.setCreatedAt(LocalDate.now());
        }
        return projectRepository.save(project);
    }

    @Transactional
    public void deleteById(Long id) {
        projectRepository.deleteById(id);
    }

    public ProjectForm toForm(FinancialProject project) {
        ProjectForm form = new ProjectForm();
        form.setId(project.getId());
        form.setName(project.getName());
        form.setEstimatedCost(project.getEstimatedCost());
        form.setDeadline(project.getDeadline());
        form.setProgress(project.getProgress());
        form.setNotes(project.getNotes());
        form.setStatus(project.getStatus().name());
        return form;
    }
}
