package com.financeboard.projects;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectService {

    private final ProjectRepository projectRepository;

    public List<FinancialProject> findAll() {
        return projectRepository.findAllByOrderByStatusAscDeadlineAsc();
    }

    public List<FinancialProject> findActive() {
        return projectRepository.findByStatusOrderByDeadlineAsc(FinancialProject.ProjectStatus.IN_PROGRESS);
    }

    public FinancialProject findById(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Project not found: " + id));
    }

    @Transactional
    public FinancialProject save(FinancialProject project) {
        if (project.getActualCost() == null) {
            project.setActualCost(java.math.BigDecimal.ZERO);
        }
        if (project.getStatus() == null) {
            project.setStatus(FinancialProject.ProjectStatus.PLANNED);
        }
        if (project.getProgressPercent() >= 100) {
            project.setStatus(FinancialProject.ProjectStatus.COMPLETED);
        }
        return projectRepository.save(project);
    }

    @Transactional
    public void delete(Long id) {
        projectRepository.deleteById(id);
    }
}
