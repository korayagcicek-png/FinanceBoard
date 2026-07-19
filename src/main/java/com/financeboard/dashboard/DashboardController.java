package com.financeboard.dashboard;

import com.financeboard.dashboard.dto.DashboardData;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Controller
@RequestMapping({"/", "/dashboard"})
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping
    public String dashboard(
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month,
            Model model) {

        int selectedYear = year != null ? year : LocalDate.now().getYear();
        int selectedMonth = month != null ? month : LocalDate.now().getMonthValue();

        DashboardData data = dashboardService.buildDashboard(selectedYear, selectedMonth);

        model.addAttribute("data", data);
        model.addAttribute("currentYear", LocalDate.now().getYear());
        model.addAttribute("activePage", "dashboard");

        return "dashboard/index";
    }
}
