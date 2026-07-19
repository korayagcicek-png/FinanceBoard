package com.financeboard.ai;

import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * No-op implementation of AiInsightService.
 * Replace this with an Ollama/LLM-backed implementation to enable AI features.
 *
 * Future integration points:
 * - Use Ollama REST API (http://localhost:11434) for local LLM inference
 * - Compatible with llama2, mistral, phi, and other open-source models
 * - No external cloud API keys required
 */
@Service
public class NoOpAiInsightService implements AiInsightService {

    @Override
    public String explainSpendingPatterns(int year, int month) {
        return "";
    }

    @Override
    public List<String> suggestBudgetImprovements() {
        return Collections.emptyList();
    }

    @Override
    public String summarizeMonthlyActivity(int year, int month) {
        return "";
    }

    @Override
    public String suggestCategory(String description) {
        return null;
    }
}
