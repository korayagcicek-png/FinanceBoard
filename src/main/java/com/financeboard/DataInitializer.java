package com.financeboard;

import com.financeboard.budgeting.Budget;
import com.financeboard.budgeting.BudgetRepository;
import com.financeboard.goals.Goal;
import com.financeboard.goals.GoalRepository;
import com.financeboard.goals.GoalStatus;
import com.financeboard.projects.FinancialProject;
import com.financeboard.projects.ProjectRepository;
import com.financeboard.projects.ProjectStatus;
import com.financeboard.shared.Category;
import com.financeboard.shared.CategoryRepository;
import com.financeboard.shared.CategoryType;
import com.financeboard.transactions.Transaction;
import com.financeboard.transactions.TransactionRepository;
import com.financeboard.transactions.TransactionType;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final TransactionRepository transactionRepository;
    private final BudgetRepository budgetRepository;
    private final GoalRepository goalRepository;
    private final ProjectRepository projectRepository;

    public DataInitializer(CategoryRepository categoryRepository,
                           TransactionRepository transactionRepository,
                           BudgetRepository budgetRepository,
                           GoalRepository goalRepository,
                           ProjectRepository projectRepository) {
        this.categoryRepository = categoryRepository;
        this.transactionRepository = transactionRepository;
        this.budgetRepository = budgetRepository;
        this.goalRepository = goalRepository;
        this.projectRepository = projectRepository;
    }

    @Override
    public void run(String... args) {
        if (categoryRepository.count() > 0) {
            return; // Already seeded
        }

        // --- Categories ---
        Category salary    = cat("Salary",          CategoryType.INCOME,   "#28a745", "💼");
        Category freelance = cat("Freelance",        CategoryType.INCOME,   "#20c997", "💻");
        Category other     = cat("Other Income",     CategoryType.INCOME,   "#17a2b8", "💰");
        Category food      = cat("Food & Dining",    CategoryType.EXPENSE,  "#fd7e14", "🍔");
        Category transport = cat("Transport",        CategoryType.EXPENSE,  "#6f42c1", "🚗");
        Category housing   = cat("Housing",          CategoryType.EXPENSE,  "#e83e8c", "🏠");
        Category utilities = cat("Utilities",        CategoryType.EXPENSE,  "#ffc107", "⚡");
        Category health    = cat("Health",           CategoryType.EXPENSE,  "#dc3545", "🏥");
        Category entertain = cat("Entertainment",    CategoryType.EXPENSE,  "#007bff", "🎬");
        Category shopping  = cat("Shopping",         CategoryType.EXPENSE,  "#6c757d", "🛍️");
        Category education = cat("Education",        CategoryType.EXPENSE,  "#795548", "📚");
        Category savings   = cat("Savings",          CategoryType.EXPENSE,  "#28a745", "💵");

        categoryRepository.saveAll(List.of(salary, freelance, other, food, transport,
                housing, utilities, health, entertain, shopping, education, savings));

        // --- Transactions (last 3 months) ---
        LocalDate today = LocalDate.now();
        int y = today.getYear();
        int m = today.getMonthValue();

        // Current month
        tx(y, m, 1,  "Monthly Salary",        new BigDecimal("3800.00"), TransactionType.INCOME,   salary);
        tx(y, m, 2,  "Rent",                   new BigDecimal("1200.00"), TransactionType.EXPENSE,  housing);
        tx(y, m, 3,  "Groceries",              new BigDecimal("185.50"),  TransactionType.EXPENSE,  food);
        tx(y, m, 5,  "Electricity bill",       new BigDecimal("82.00"),   TransactionType.EXPENSE,  utilities);
        tx(y, m, 7,  "Bus pass",               new BigDecimal("55.00"),   TransactionType.EXPENSE,  transport);
        tx(y, m, 8,  "Restaurant dinner",      new BigDecimal("67.30"),   TransactionType.EXPENSE,  food);
        tx(y, m, 10, "Netflix subscription",   new BigDecimal("15.99"),   TransactionType.EXPENSE,  entertain);
        tx(y, m, 12, "Freelance project",      new BigDecimal("500.00"),  TransactionType.INCOME,   freelance);
        tx(y, m, 14, "Online course",          new BigDecimal("49.00"),   TransactionType.EXPENSE,  education);
        tx(y, m, 15, "Pharmacy",               new BigDecimal("35.20"),   TransactionType.EXPENSE,  health);
        tx(y, m, 16, "Clothes",                new BigDecimal("120.00"),  TransactionType.EXPENSE,  shopping);
        tx(y, m, 18, "Savings transfer",       new BigDecimal("300.00"),  TransactionType.EXPENSE,  savings);
        tx(y, m, 20, "Coffee & snacks",        new BigDecimal("28.80"),   TransactionType.EXPENSE,  food);
        tx(y, m, 22, "Uber",                   new BigDecimal("22.50"),   TransactionType.EXPENSE,  transport);
        tx(y, m, 25, "Cinema",                 new BigDecimal("32.00"),   TransactionType.EXPENSE,  entertain);

        // Previous month
        int pm = m == 1 ? 12 : m - 1;
        int py = m == 1 ? y - 1 : y;
        tx(py, pm, 1,  "Monthly Salary",       new BigDecimal("3800.00"), TransactionType.INCOME,   salary);
        tx(py, pm, 2,  "Rent",                  new BigDecimal("1200.00"), TransactionType.EXPENSE,  housing);
        tx(py, pm, 3,  "Groceries",             new BigDecimal("210.40"),  TransactionType.EXPENSE,  food);
        tx(py, pm, 5,  "Electricity bill",      new BigDecimal("91.50"),   TransactionType.EXPENSE,  utilities);
        tx(py, pm, 7,  "Bus pass",              new BigDecimal("55.00"),   TransactionType.EXPENSE,  transport);
        tx(py, pm, 9,  "Restaurant",            new BigDecimal("54.00"),   TransactionType.EXPENSE,  food);
        tx(py, pm, 10, "Spotify",               new BigDecimal("9.99"),    TransactionType.EXPENSE,  entertain);
        tx(py, pm, 11, "Netflix",               new BigDecimal("15.99"),   TransactionType.EXPENSE,  entertain);
        tx(py, pm, 13, "Freelance",             new BigDecimal("350.00"),  TransactionType.INCOME,   freelance);
        tx(py, pm, 16, "Doctor visit",          new BigDecimal("50.00"),   TransactionType.EXPENSE,  health);
        tx(py, pm, 18, "Books",                 new BigDecimal("35.00"),   TransactionType.EXPENSE,  education);
        tx(py, pm, 20, "Savings transfer",      new BigDecimal("300.00"),  TransactionType.EXPENSE,  savings);
        tx(py, pm, 22, "Shopping mall",         new BigDecimal("145.00"),  TransactionType.EXPENSE,  shopping);
        tx(py, pm, 25, "Coffee",                new BigDecimal("18.50"),   TransactionType.EXPENSE,  food);

        // Two months ago
        int pm2 = pm == 1 ? 12 : pm - 1;
        int py2 = pm == 1 ? py - 1 : py;
        tx(py2, pm2, 1,  "Monthly Salary",      new BigDecimal("3800.00"), TransactionType.INCOME,   salary);
        tx(py2, pm2, 2,  "Rent",                 new BigDecimal("1200.00"), TransactionType.EXPENSE,  housing);
        tx(py2, pm2, 3,  "Groceries",            new BigDecimal("195.00"),  TransactionType.EXPENSE,  food);
        tx(py2, pm2, 5,  "Electricity bill",     new BigDecimal("78.50"),   TransactionType.EXPENSE,  utilities);
        tx(py2, pm2, 6,  "Bus pass",             new BigDecimal("55.00"),   TransactionType.EXPENSE,  transport);
        tx(py2, pm2, 8,  "Birthday dinner",      new BigDecimal("89.00"),   TransactionType.EXPENSE,  food);
        tx(py2, pm2, 12, "Streaming services",   new BigDecimal("25.98"),   TransactionType.EXPENSE,  entertain);
        tx(py2, pm2, 15, "Side project income",  new BigDecimal("600.00"),  TransactionType.INCOME,   other);
        tx(py2, pm2, 17, "Gym membership",       new BigDecimal("40.00"),   TransactionType.EXPENSE,  health);
        tx(py2, pm2, 19, "New shoes",            new BigDecimal("89.00"),   TransactionType.EXPENSE,  shopping);
        tx(py2, pm2, 20, "Savings transfer",     new BigDecimal("400.00"),  TransactionType.EXPENSE,  savings);
        tx(py2, pm2, 24, "Taxi",                 new BigDecimal("18.00"),   TransactionType.EXPENSE,  transport);

        // --- Budgets (current month) ---
        budget(housing,   y, m, new BigDecimal("1200.00"));
        budget(food,      y, m, new BigDecimal("400.00"));
        budget(transport, y, m, new BigDecimal("100.00"));
        budget(utilities, y, m, new BigDecimal("120.00"));
        budget(entertain, y, m, new BigDecimal("80.00"));
        budget(health,    y, m, new BigDecimal("100.00"));
        budget(shopping,  y, m, new BigDecimal("150.00"));
        budget(education, y, m, new BigDecimal("100.00"));
        budget(null,      y, m, new BigDecimal("3500.00")); // overall

        // --- Savings Goals ---
        goal("Emergency Fund",       new BigDecimal("10000.00"), new BigDecimal("4500.00"),
             today.plusMonths(12), "Build 3-month emergency reserve", GoalStatus.ACTIVE);
        goal("Vacation to Japan",    new BigDecimal("3500.00"),  new BigDecimal("1200.00"),
             today.plusMonths(8),  "Summer trip to Japan",           GoalStatus.ACTIVE);
        goal("New Laptop",           new BigDecimal("1500.00"),  new BigDecimal("750.00"),
             today.plusMonths(4),  "MacBook Pro upgrade",            GoalStatus.ACTIVE);
        goal("Investment Portfolio", new BigDecimal("5000.00"),  new BigDecimal("5000.00"),
             today.minusMonths(1), "Initial investment portfolio",   GoalStatus.COMPLETED);
        goal("Home Down Payment",    new BigDecimal("30000.00"), new BigDecimal("8000.00"),
             today.plusMonths(36), "Long-term savings for mortgage", GoalStatus.ACTIVE);

        // --- Financial Projects ---
        project("Kitchen Renovation",     new BigDecimal("8000.00"),  today.plusMonths(6),  40, ProjectStatus.IN_PROGRESS,
                "Renovate kitchen with new cabinets and countertops");
        project("Home Office Setup",      new BigDecimal("2500.00"),  today.plusMonths(2),  70, ProjectStatus.IN_PROGRESS,
                "Standing desk, monitor, ergonomic chair");
        project("Car Purchase",           new BigDecimal("15000.00"), today.plusMonths(18), 10, ProjectStatus.PLANNED,
                "Save up for a used car");
        project("Website Portfolio",      new BigDecimal("500.00"),   today.plusMonths(1),  90, ProjectStatus.IN_PROGRESS,
                "Personal website to showcase projects");
        project("Study Abroad Program",   new BigDecimal("6000.00"),  today.plusMonths(10), 0,  ProjectStatus.PLANNED,
                "Language course in Europe");
        project("Old iPhone Sale",        new BigDecimal("300.00"),   today.minusMonths(1), 100, ProjectStatus.COMPLETED,
                "Sold old phone on marketplace");
    }

    // --- Helpers ---
    private Category cat(String name, CategoryType type, String color, String icon) {
        return new Category(name, type, color, icon);
    }

    private void tx(int year, int month, int day, String desc, BigDecimal amount,
                    TransactionType type, Category category) {
        Transaction t = new Transaction();
        t.setDescription(desc);
        t.setAmount(amount);
        t.setType(type);
        t.setCategory(category);
        int lastDay = LocalDate.of(year, month, 1).lengthOfMonth();
        t.setDate(LocalDate.of(year, month, Math.min(day, lastDay)));
        transactionRepository.save(t);
    }

    private void budget(Category category, int year, int month, BigDecimal limit) {
        Budget b = new Budget();
        b.setCategory(category);
        b.setYear(year);
        b.setMonth(month);
        b.setLimitAmount(limit);
        budgetRepository.save(b);
    }

    private void goal(String name, BigDecimal target, BigDecimal current,
                      LocalDate targetDate, String desc, GoalStatus status) {
        Goal g = new Goal();
        g.setName(name);
        g.setTargetAmount(target);
        g.setCurrentAmount(current);
        g.setTargetDate(targetDate);
        g.setDescription(desc);
        g.setStatus(status);
        goalRepository.save(g);
    }

    private void project(String name, BigDecimal cost, LocalDate deadline,
                         int progress, ProjectStatus status, String notes) {
        FinancialProject p = new FinancialProject();
        p.setName(name);
        p.setEstimatedCost(cost);
        p.setDeadline(deadline);
        p.setProgress(progress);
        p.setStatus(status);
        p.setNotes(notes);
        p.setCreatedAt(LocalDate.now().minusMonths(1));
        projectRepository.save(p);
    }
}
