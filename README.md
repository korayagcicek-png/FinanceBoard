# FinanceBoard

A **local-first personal finance tracking and budgeting application** built with Spring Boot, Thymeleaf, and Spring Modulith.

> **Privacy-friendly**: All your data stays on your machine. No accounts, no cloud, no telemetry.

---

## Features

- **Dashboard** — Key financial indicators, spending charts, budget overview at a glance
- **Transactions** — Add, edit, delete income and expense transactions with category support
- **Budgets** — Set monthly spending limits per category, track remaining budget and overspending warnings
- **Savings Goals** — Define goals with target amounts, track progress, and add contributions
- **Financial Projects** — Plan projects with estimated costs, deadlines, and progress tracking
- **Categories** — Manage custom categories for income and expenses
- **Charts** — Pie chart for spending by category, bar chart for monthly income vs expenses (Chart.js)
- **Sample Data** — Pre-loaded with realistic sample data so you can explore immediately

---

## Technology Stack

| Layer         | Technology                        |
|---------------|-----------------------------------|
| Framework     | Spring Boot 3.2                   |
| Architecture  | Spring Modulith                   |
| UI            | Thymeleaf + Thymeleaf Layout Dialect |
| CSS           | Bootstrap 5.3                     |
| Charts        | Chart.js 4.4                      |
| Database      | H2 (file-based, local persistence)|
| ORM           | Spring Data JPA / Hibernate       |
| Language      | Java 17                           |
| Build Tool    | Maven                             |

---

## Project Structure (Spring Modulith Modules)

```
com.financeboard/
├── FinanceBoardApplication.java    # Application entry point
├── DataInitializer.java            # Sample data seeding
│
├── shared/                         # Shared module (Category entity)
│   ├── Category.java
│   ├── CategoryRepository.java
│   ├── CategoryService.java
│   └── CategoryController.java
│
├── transactions/                   # Transactions module
│   ├── Transaction.java
│   ├── TransactionRepository.java
│   ├── TransactionService.java
│   └── TransactionController.java
│
├── budgeting/                      # Budgeting module
│   ├── Budget.java
│   ├── BudgetRepository.java
│   ├── BudgetService.java
│   └── BudgetController.java
│
├── goals/                          # Savings Goals module
│   ├── Goal.java
│   ├── GoalRepository.java
│   ├── GoalService.java
│   └── GoalController.java
│
├── projects/                       # Financial Projects module
│   ├── FinancialProject.java
│   ├── ProjectRepository.java
│   ├── ProjectService.java
│   └── ProjectController.java
│
└── dashboard/                      # Dashboard module
    ├── DashboardData.java
    ├── DashboardService.java
    └── DashboardController.java
```

---

## Prerequisites

- **Java 17+** — [Download](https://adoptium.net/)
- **Maven 3.8+** — [Download](https://maven.apache.org/download.cgi) *(or use the included `mvnw` wrapper)*
- Internet connection on first run (for CDN-served Bootstrap and Chart.js)

---

## Quick Start

### 1. Clone the repository

```bash
git clone https://github.com/korayagcicek-png/FinanceBoard.git
cd FinanceBoard
```

> **Note**: If you downloaded the source directly, skip this step and just enter the project directory.

### 2. Build the project

```bash
./mvnw clean package -DskipTests
```

Or on Windows:
```cmd
mvnw.cmd clean package -DskipTests
```

### 3. Run the application

```bash
./mvnw spring-boot:run
```

Or run the JAR directly:
```bash
java -jar target/financeboard-0.0.1-SNAPSHOT.jar
```

### 4. Open in browser

Navigate to: **http://localhost:8080**

The application will automatically seed sample data on the first run.

---

## Database

The application uses an **H2 file-based database** stored locally at:
```
./financeboard-data.mv.db
```

Data **persists between restarts**. No external database setup is required.

### H2 Console

For direct database access, the H2 console is available at:
**http://localhost:8080/h2-console**

- **JDBC URL**: `jdbc:h2:file:./financeboard-data`
- **Username**: `sa`
- **Password**: *(empty)*

---

## Configuration

Configuration is in `src/main/resources/application.properties`:

```properties
# Change the port if 8080 is already in use
server.port=8080

# Database location (relative to working directory)
spring.datasource.url=jdbc:h2:file:./financeboard-data;AUTO_SERVER=TRUE
```

---

## Running Tests

```bash
./mvnw test
```

Tests use an in-memory H2 database and the `test` Spring profile.

---

## Optional AI Extension

The architecture is prepared for a future AI module. To add an AI assistant later:

1. Create a new `com.financeboard.ai` module package
2. Integrate with [Ollama](https://ollama.ai/) for local LLM support (free, no cloud required)
3. Use Spring's `RestTemplate` or `WebClient` to call the Ollama REST API at `http://localhost:11434`

Suggested future AI features:
- Explain spending patterns
- Suggest budget improvements
- Summarize monthly financial activity
- Help categorize transactions

---

## Navigation

| URL             | Description                        |
|-----------------|------------------------------------|
| `/`             | Dashboard                          |
| `/transactions` | All transactions (filterable)      |
| `/budgets`      | Monthly budgets                    |
| `/goals`        | Savings goals                      |
| `/projects`     | Financial projects                 |
| `/categories`   | Manage categories                  |
| `/h2-console`   | H2 database console (dev use)      |

---

## Screenshots

The application includes:
- Dashboard with KPI cards, pie chart (spending by category), bar chart (monthly trends)
- Responsive transaction list with filters (month, year, category, type)
- Budget tracker with progress bars and overspending warnings
- Goal tracker with contribution form and progress visualization
- Project tracker with cost and deadline tracking

---

## License

This is a personal, private-use application. Use freely for your own financial management.