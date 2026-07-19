package com.financeboard.projects;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<FinancialProject, Long> {

    List<FinancialProject> findByStatusOrderByDeadlineAsc(FinancialProject.ProjectStatus status);

    List<FinancialProject> findAllByOrderByStatusAscDeadlineAsc();
}
