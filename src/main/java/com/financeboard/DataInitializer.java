package com.financeboard;

import com.financeboard.budgeting.Budget;
import com.financeboard.budgeting.BudgetRepository;
import com.financeboard.goals.Goal;
import com.financeboard.goals.GoalRepository;
import com.financeboard.projects.FinancialProject;
import com.financeboard.projects.ProjectRepository;
import com.financeboard.shared.Category;
import com.financeboard.shared.CategoryRepository;
import com.financeboard.transactions.Transaction;
import com.financeboard.transactions.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
@RequiredArgsConstructor
@Slf4j
@Profile("!test")
public class DataInitializer implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final TransactionRepository transactionRepository;
    private final BudgetRepository budgetRepository;
    private final GoalRepository goalRepository;
    private final ProjectRepository projectRepository;

    @Override
    public void run(String... args) {
        if (categoryRepository.count() > 0) {
            log.info("Database already contains data. Skipping initialization.");
            return;
        }

        log.info("Initializing sample data...");

        // Categories
        Category salary = save(new Category("Salary", Category.CategoryType.INCOME, "#28a745", "bi-cash-coin"));
        Category freelance = save(new Category("Freelance", Category.CategoryType.INCOME, "#20c997", "bi-laptop"));
        Category groceries = save(new Category("Groceries", Category.CategoryType.EXPENSE, "#fd7e14", "bi-cart"));
        Category rent = save(new Category("Rent", Category.CategoryType.EXPENSE, "#dc3545", "bi-house"));
        Category utilities = save(new Category("Utilities", Category.CategoryType.EXPENSE, "#6610f2", "bi-lightning"));
        Category transport = save(new Category("Transport", Category.CategoryType.EXPENSE, "#0dcaf0", "bi-car-front"));
        Category dining = save(new Category("Dining Out", Category.CategoryType.EXPENSE, "#ffc107", "bi-cup-hot"));
        Category entertainment = save(new Category("Entertainment", Category.CategoryType.EXPENSE, "#e83e8c", "bi-film"));
        Category health = save(new Category("Health", Category.CategoryType.EXPENSE, "#198754", "bi-heart-pulse"));
        Category savings = save(new Category("Savings", Category.CategoryType.BOTH, "#0d6efd", "bi-piggy-bank"));
        Category shopping = save(new Category("Shopping", Category.CategoryType.EXPENSE, "#6c757d", "bi-bag"));
        Category education = save(new Category("Education", Category.CategoryType.EXPENSE, "#0dcaf0", "bi-book"));

        // Transactions (current and previous months)
        LocalDate today = LocalDate.now();
        int year = today.getYear();
        int month = today.getMonthValue();

        // Current month transactions
        transactionRepository.save(new Transaction(new BigDecimal("3500.00"), "Monthly Salary", today.withDayOfMonth(1), Transaction.TransactionType.INCOME, salary));
        transactionRepository.save(new Transaction(new BigDecimal("750.00"), "Freelance Project - Website", today.withDayOfMonth(5), Transaction.TransactionType.INCOME, freelance));
        transactionRepository.save(new Transaction(new BigDecimal("1200.00"), "Apartment Rent", today.withDayOfMonth(3), Transaction.TransactionType.EXPENSE, rent));
        transactionRepository.save(new Transaction(new BigDecimal("280.00"), "Weekly Groceries x4", today.withDayOfMonth(7), Transaction.TransactionType.EXPENSE, groceries));
        transactionRepository.save(new Transaction(new BigDecimal("95.00"), "Electricity Bill", today.withDayOfMonth(10), Transaction.TransactionType.EXPENSE, utilities));
        transactionRepository.save(new Transaction(new BigDecimal("45.00"), "Internet Bill", today.withDayOfMonth(10), Transaction.TransactionType.EXPENSE, utilities));
        transactionRepository.save(new Transaction(new BigDecimal("120.00"), "Monthly Transit Pass", today.withDayOfMonth(2), Transaction.TransactionType.EXPENSE, transport));
        transactionRepository.save(new Transaction(new BigDecimal("180.00"), "Restaurant visits", today.withDayOfMonth(12), Transaction.TransactionType.EXPENSE, dining));
        transactionRepository.save(new Transaction(new BigDecimal("35.00"), "Cinema tickets", today.withDayOfMonth(14), Transaction.TransactionType.EXPENSE, entertainment));
        transactionRepository.save(new Transaction(new BigDecimal("500.00"), "Monthly savings transfer", today.withDayOfMonth(5), Transaction.TransactionType.EXPENSE, savings));
        transactionRepository.save(new Transaction(new BigDecimal("65.00"), "Pharmacy", today.withDayOfMonth(8), Transaction.TransactionType.EXPENSE, health));
        transactionRepository.save(new Transaction(new BigDecimal("230.00"), "Online Shopping", today.withDayOfMonth(15), Transaction.TransactionType.EXPENSE, shopping));

        // Previous month transactions
        LocalDate prevMonth = today.minusMonths(1);
        transactionRepository.save(new Transaction(new BigDecimal("3500.00"), "Monthly Salary", prevMonth.withDayOfMonth(1), Transaction.TransactionType.INCOME, salary));
        transactionRepository.save(new Transaction(new BigDecimal("500.00"), "Side Project Income", prevMonth.withDayOfMonth(15), Transaction.TransactionType.INCOME, freelance));
        transactionRepository.save(new Transaction(new BigDecimal("1200.00"), "Apartment Rent", prevMonth.withDayOfMonth(3), Transaction.TransactionType.EXPENSE, rent));
        transactionRepository.save(new Transaction(new BigDecimal("310.00"), "Weekly Groceries x4", prevMonth.withDayOfMonth(7), Transaction.TransactionType.EXPENSE, groceries));
        transactionRepository.save(new Transaction(new BigDecimal("95.00"), "Electricity Bill", prevMonth.withDayOfMonth(10), Transaction.TransactionType.EXPENSE, utilities));
        transactionRepository.save(new Transaction(new BigDecimal("45.00"), "Internet Bill", prevMonth.withDayOfMonth(10), Transaction.TransactionType.EXPENSE, utilities));
        transactionRepository.save(new Transaction(new BigDecimal("120.00"), "Monthly Transit Pass", prevMonth.withDayOfMonth(2), Transaction.TransactionType.EXPENSE, transport));
        transactionRepository.save(new Transaction(new BigDecimal("95.00"), "Restaurant visits", prevMonth.withDayOfMonth(20), Transaction.TransactionType.EXPENSE, dining));
        transactionRepository.save(new Transaction(new BigDecimal("500.00"), "Monthly savings transfer", prevMonth.withDayOfMonth(5), Transaction.TransactionType.EXPENSE, savings));
        transactionRepository.save(new Transaction(new BigDecimal("150.00"), "Online course", prevMonth.withDayOfMonth(12), Transaction.TransactionType.EXPENSE, education));

        // 2 months ago
        LocalDate twoMonthsAgo = today.minusMonths(2);
        transactionRepository.save(new Transaction(new BigDecimal("3500.00"), "Monthly Salary", twoMonthsAgo.withDayOfMonth(1), Transaction.TransactionType.INCOME, salary));
        transactionRepository.save(new Transaction(new BigDecimal("1200.00"), "Apartment Rent", twoMonthsAgo.withDayOfMonth(3), Transaction.TransactionType.EXPENSE, rent));
        transactionRepository.save(new Transaction(new BigDecimal("260.00"), "Groceries", twoMonthsAgo.withDayOfMonth(10), Transaction.TransactionType.EXPENSE, groceries));
        transactionRepository.save(new Transaction(new BigDecimal("140.00"), "Electricity + Gas", twoMonthsAgo.withDayOfMonth(10), Transaction.TransactionType.EXPENSE, utilities));
        transactionRepository.save(new Transaction(new BigDecimal("120.00"), "Monthly Transit Pass", twoMonthsAgo.withDayOfMonth(2), Transaction.TransactionType.EXPENSE, transport));
        transactionRepository.save(new Transaction(new BigDecimal("200.00"), "Clothes shopping", twoMonthsAgo.withDayOfMonth(20), Transaction.TransactionType.EXPENSE, shopping));
        transactionRepository.save(new Transaction(new BigDecimal("500.00"), "Monthly savings transfer", twoMonthsAgo.withDayOfMonth(5), Transaction.TransactionType.EXPENSE, savings));

        // Budgets for current month
        budgetRepository.save(new Budget(groceries, month, year, new BigDecimal("300.00")));
        budgetRepository.save(new Budget(rent, month, year, new BigDecimal("1200.00")));
        budgetRepository.save(new Budget(utilities, month, year, new BigDecimal("200.00")));
        budgetRepository.save(new Budget(transport, month, year, new BigDecimal("150.00")));
        budgetRepository.save(new Budget(dining, month, year, new BigDecimal("150.00")));
        budgetRepository.save(new Budget(entertainment, month, year, new BigDecimal("80.00")));
        budgetRepository.save(new Budget(health, month, year, new BigDecimal("100.00")));
        budgetRepository.save(new Budget(shopping, month, year, new BigDecimal("200.00")));
        budgetRepository.save(new Budget(savings, month, year, new BigDecimal("500.00")));

        // Goals
        Goal emergencyFund = new Goal();
        emergencyFund.setName("Emergency Fund");
        emergencyFund.setDescription("6 months of living expenses as emergency reserve");
        emergencyFund.setTargetAmount(new BigDecimal("15000.00"));
        emergencyFund.setCurrentAmount(new BigDecimal("6500.00"));
        emergencyFund.setTargetDate(LocalDate.now().plusYears(1));
        emergencyFund.setStatus(Goal.GoalStatus.ACTIVE);
        goalRepository.save(emergencyFund);

        Goal vacation = new Goal();
        vacation.setName("Summer Vacation");
        vacation.setDescription("Trip to Europe - flights, hotels, and activities");
        vacation.setTargetAmount(new BigDecimal("3500.00"));
        vacation.setCurrentAmount(new BigDecimal("1200.00"));
        vacation.setTargetDate(LocalDate.now().plusMonths(8));
        vacation.setStatus(Goal.GoalStatus.ACTIVE);
        goalRepository.save(vacation);

        Goal laptop = new Goal();
        laptop.setName("New Laptop");
        laptop.setDescription("MacBook Pro for work and side projects");
        laptop.setTargetAmount(new BigDecimal("2500.00"));
        laptop.setCurrentAmount(new BigDecimal("2500.00"));
        laptop.setTargetDate(LocalDate.now().minusMonths(1));
        laptop.setStatus(Goal.GoalStatus.COMPLETED);
        goalRepository.save(laptop);

        // Projects
        FinancialProject homeRenovation = new FinancialProject();
        homeRenovation.setName("Home Office Renovation");
        homeRenovation.setDescription("Convert spare room into a productive home office with proper desk, lighting, and storage");
        homeRenovation.setEstimatedCost(new BigDecimal("4000.00"));
        homeRenovation.setActualCost(new BigDecimal("1500.00"));
        homeRenovation.setDeadline(LocalDate.now().plusMonths(3));
        homeRenovation.setStatus(FinancialProject.ProjectStatus.IN_PROGRESS);
        homeRenovation.setProgressPercent(40);
        projectRepository.save(homeRenovation);

        FinancialProject investmentAccount = new FinancialProject();
        investmentAccount.setName("Start Investment Portfolio");
        investmentAccount.setDescription("Open brokerage account and invest first €5,000 in index funds");
        investmentAccount.setEstimatedCost(new BigDecimal("5000.00"));
        investmentAccount.setActualCost(new BigDecimal("0.00"));
        investmentAccount.setDeadline(LocalDate.now().plusMonths(6));
        investmentAccount.setStatus(FinancialProject.ProjectStatus.PLANNED);
        investmentAccount.setProgressPercent(10);
        projectRepository.save(investmentAccount);

        FinancialProject carService = new FinancialProject();
        carService.setName("Car Major Service");
        carService.setDescription("Annual car service including tires and brake pads replacement");
        carService.setEstimatedCost(new BigDecimal("1200.00"));
        carService.setActualCost(new BigDecimal("1350.00"));
        carService.setDeadline(LocalDate.now().minusMonths(1));
        carService.setStatus(FinancialProject.ProjectStatus.COMPLETED);
        carService.setProgressPercent(100);
        projectRepository.save(carService);

        log.info("Sample data initialized successfully.");
    }

    private Category save(Category category) {
        return categoryRepository.save(category);
    }
}
