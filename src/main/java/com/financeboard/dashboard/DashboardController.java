package com.financeboard.dashboard;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;
    private final ObjectMapper objectMapper;

    @GetMapping
    public String dashboard(@RequestParam(required = false) Integer year,
                            @RequestParam(required = false) Integer month,
                            Model model) {
        YearMonth current = YearMonth.now();
        int selectedYear = (year != null) ? year : current.getYear();
        int selectedMonth = (month != null) ? month : current.getMonthValue();

        DashboardData data = dashboardService.getDashboardData(selectedYear, selectedMonth);
        model.addAttribute("data", data);
        model.addAttribute("selectedYear", selectedYear);
        model.addAttribute("selectedMonth", selectedMonth);
        model.addAttribute("pageTitle", "Dashboard");

        // Build chart JSON for Thymeleaf
        try {
            // Pie chart: expenses by category
            List<String> categoryLabels = new ArrayList<>(data.getExpensesByCategory().keySet());
            List<Number> categoryValues = new ArrayList<>(data.getExpensesByCategory().values());
            model.addAttribute("categoryLabels", objectMapper.writeValueAsString(categoryLabels));
            model.addAttribute("categoryValues", objectMapper.writeValueAsString(categoryValues));

            // Bar chart: monthly income vs expenses
            List<Number> monthlyIncomeValues = new ArrayList<>(data.getMonthlyIncomes().values());
            List<Number> monthlyExpenseValues = new ArrayList<>(data.getMonthlyExpenses().values());
            model.addAttribute("monthlyIncomeValues", objectMapper.writeValueAsString(monthlyIncomeValues));
            model.addAttribute("monthlyExpenseValues", objectMapper.writeValueAsString(monthlyExpenseValues));
        } catch (JsonProcessingException e) {
            model.addAttribute("categoryLabels", "[]");
            model.addAttribute("categoryValues", "[]");
            model.addAttribute("monthlyIncomeValues", "[]");
            model.addAttribute("monthlyExpenseValues", "[]");
        }

        return "dashboard/index";
    }
}
