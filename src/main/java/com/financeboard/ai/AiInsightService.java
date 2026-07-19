package com.financeboard.ai;

import java.util.List;

/**
 * Interface for AI-powered financial insights.
 * Designed to work with local LLMs (e.g., Ollama) in the future.
 * All methods are no-op by default - safe to enable/disable independently.
 */
public interface AiInsightService {

    /**
     * Explain spending patterns for a given month.
     * @param year  the year
     * @param month the month (1-12)
     * @return a human-readable explanation, or empty string if unavailable
     */
    String explainSpendingPatterns(int year, int month);

    /**
     * Suggest budget improvements based on recent transaction history.
     * @return a list of improvement suggestions
     */
    List<String> suggestBudgetImprovements();

    /**
     * Generate a monthly activity summary.
     * @param year  the year
     * @param month the month (1-12)
     * @return a summary text
     */
    String summarizeMonthlyActivity(int year, int month);

    /**
     * Suggest a category for a transaction based on its description.
     * @param description the transaction description
     * @return the suggested category name, or null if unavailable
     */
    String suggestCategory(String description);
}
