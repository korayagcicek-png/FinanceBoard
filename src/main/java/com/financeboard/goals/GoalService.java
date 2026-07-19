package com.financeboard.goals;

import com.financeboard.goals.dto.GoalForm;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class GoalService {

    private final GoalRepository goalRepository;

    public GoalService(GoalRepository goalRepository) {
        this.goalRepository = goalRepository;
    }

    public List<Goal> findAll() {
        return goalRepository.findAllByOrderByStatusAscTargetDateAsc();
    }

    public List<Goal> findActive() {
        return goalRepository.findByStatusOrderByTargetDateAsc(GoalStatus.ACTIVE);
    }

    public Optional<Goal> findById(Long id) {
        return goalRepository.findById(id);
    }

    @Transactional
    public Goal save(GoalForm form) {
        Goal goal = new Goal();
        if (form.getId() != null) {
            goal = goalRepository.findById(form.getId()).orElse(new Goal());
        }
        goal.setName(form.getName());
        goal.setTargetAmount(form.getTargetAmount());
        goal.setCurrentAmount(form.getCurrentAmount() != null ? form.getCurrentAmount() : BigDecimal.ZERO);
        goal.setTargetDate(form.getTargetDate());
        goal.setDescription(form.getDescription());
        goal.setStatus(GoalStatus.valueOf(form.getStatus()));
        return goalRepository.save(goal);
    }

    @Transactional
    public void deleteById(Long id) {
        goalRepository.deleteById(id);
    }

    public GoalForm toForm(Goal goal) {
        GoalForm form = new GoalForm();
        form.setId(goal.getId());
        form.setName(goal.getName());
        form.setTargetAmount(goal.getTargetAmount());
        form.setCurrentAmount(goal.getCurrentAmount());
        form.setTargetDate(goal.getTargetDate());
        form.setDescription(goal.getDescription());
        form.setStatus(goal.getStatus().name());
        return form;
    }
}
