package com.financeboard.goals;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GoalService {

    private final GoalRepository goalRepository;

    public List<Goal> findAll() {
        return goalRepository.findAllByOrderByStatusAscTargetDateAsc();
    }

    public List<Goal> findActive() {
        return goalRepository.findByStatusOrderByTargetDateAsc(Goal.GoalStatus.ACTIVE);
    }

    public Goal findById(Long id) {
        return goalRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Goal not found: " + id));
    }

    @Transactional
    public Goal save(Goal goal) {
        if (goal.getCurrentAmount() == null) {
            goal.setCurrentAmount(BigDecimal.ZERO);
        }
        if (goal.getStatus() == null) {
            goal.setStatus(Goal.GoalStatus.ACTIVE);
        }
        // Auto-complete if target reached
        if (goal.getCurrentAmount().compareTo(goal.getTargetAmount()) >= 0) {
            goal.setStatus(Goal.GoalStatus.COMPLETED);
        }
        return goalRepository.save(goal);
    }

    @Transactional
    public Goal addContribution(Long id, BigDecimal amount) {
        Goal goal = findById(id);
        goal.setCurrentAmount(goal.getCurrentAmount().add(amount));
        if (goal.getCurrentAmount().compareTo(goal.getTargetAmount()) >= 0) {
            goal.setStatus(Goal.GoalStatus.COMPLETED);
        }
        return goalRepository.save(goal);
    }

    @Transactional
    public void delete(Long id) {
        goalRepository.deleteById(id);
    }
}
