# FinanceBoard

A **local-first personal finance tracking and budgeting application** built with Spring Boot, Thymeleaf, and Spring Modulith. Designed for single-user, private local use — no cloud, no accounts, no internet required.

---

## ✨ Features

| Module | Functionality |
|---|---|
| **Dashboard** | Monthly KPIs (income, expenses, net), spending-by-category doughnut chart, 6-month trend bar chart, budget status, goal progress |
| **Transactions** | Add/edit/delete transactions, assign categories, filter by month/year/category |
| **Budgets** | Set monthly limits by category or overall, see spent vs. remaining, overspend warnings |
| **Goals** | Create savings goals with target amount, current amount, target date, and progress tracking |
| **Projects** | Plan financial projects with estimated cost, deadline, progress %, and status |

---

## 🚀 Quick Start

### Prerequisites
- Java 17+
- Maven 3.8+

### Run
```bash
# Clone and start
git clone https://github.com/korayagcicek-png/FinanceBoard.git
cd FinanceBoard
mvn spring-boot:run
```

Open your browser at **http://localhost:8080**

The app seeds realistic sample data on first startup automatically.

### H2 Database Console
During development, you can inspect the database at:  
**http://localhost:8080/h2-console**  
- JDBC URL: `jdbc:h2:file:./data/financeboard`
- Username: `sa`
- Password: *(empty)*

---

## 🏗️ Architecture

The project follows **Spring Modulith** conventions with clean package-per-module boundaries:

```
com.financeboard/
├── FinanceBoardApplication.java     # Entry point
├── DataInitializer.java             # Seed data on startup
├── GlobalExceptionHandler.java      # Error handling
│
├── shared/                          # Shared domain: Category entity and service
├── transactions/                    # Transactions module
│   └── dto/                         # Form DTOs
├── budgeting/                       # Budgets module
│   └── dto/
├── goals/                           # Savings goals module
│   └── dto/
├── projects/                        # Financial projects module
│   └── dto/
├── dashboard/                       # Dashboard aggregation module
│   └── dto/
└── ai/                              # AI placeholder module (no-op, see below)
```

### Technology Stack
| Layer | Technology |
|---|---|
| Framework | Spring Boot 3.3 |
| Architecture | Spring Modulith 1.2 |
| UI | Thymeleaf + Bootstrap 5 |
| Charts | Chart.js 4 |
| Database | H2 (file-based, local) |
| Persistence | Spring Data JPA / Hibernate |
| Validation | Jakarta Bean Validation |

### Switching to PostgreSQL/SQLite
1. Replace the H2 driver in `pom.xml` with the target database driver
2. Update `spring.datasource.*` in `application.properties`
3. Change `spring.jpa.hibernate.ddl-auto` to `validate` and manage schema with Flyway/Liquibase

---

## 🤖 AI Extension (Optional)

The `ai` module contains a clean interface `AiInsightService` and a no-op implementation that is safe to use by default.

Future integration points (designed for **local LLMs via Ollama**):
- `explainSpendingPatterns(year, month)` — natural language explanation of spend patterns
- `suggestBudgetImprovements()` — category-based budget advice
- `summarizeMonthlyActivity(year, month)` — monthly narrative summary
- `suggestCategory(description)` — automatic transaction categorization

**Ollama integration notes:**
- Ollama runs a local REST API at `http://localhost:11434`
- Compatible with open models: `llama2`, `mistral`, `phi`, `gemma`, etc.
- No API keys or cloud dependencies required
- Replace `NoOpAiInsightService` with an Ollama HTTP client implementation

---

## 📋 Module Overview

### `shared` — Categories
Provides the `Category` entity (name, type, color, icon) shared across all modules. Accessed via `CategoryService`.

### `transactions` — Transaction Tracking
Core financial ledger. Transactions have: amount, type (INCOME/EXPENSE), date, category, description, notes.  
Includes summary queries for monthly totals and category breakdowns.

### `budgeting` — Budget Management
Define per-category or overall monthly budget limits. Calculates `BudgetStatus` (spent, remaining, % used, overspend flag) by combining budget limits with transaction data.

### `goals` — Savings Goals
Track long-term savings goals: target amount, current saved amount, target date, status (ACTIVE/PAUSED/COMPLETED).

### `projects` — Financial Projects
Plan future large-scale financial projects: name, estimated cost, deadline, progress (0–100%), status, and notes.

### `dashboard` — Aggregated View
Aggregates data from all modules for the main dashboard view. Builds KPI summaries, chart data, budget statuses, and goal snapshots.

---

## 🧪 Testing

```bash
mvn test
```

Tests use an in-memory H2 database with `create-drop` to ensure isolation.
