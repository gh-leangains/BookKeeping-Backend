# BookKeeping Backend API v2.0
# MathsQuest Adventure - Implementation Issues

This document contains the comprehensive GitHub Issues created for the MathsQuest Adventure project implementation. Each issue represents a user story or technical task with detailed implementation plans.

## Created Issues Overview

### Epic and Core Features
- **Issue #2**: [Epic: User Authentication System](https://github.com/gh-leangains/BookKeeping-Backend/issues/2) - Overall authentication epic
- **Issue #3**: [User Story: Student Registration and Login](https://github.com/gh-leangains/BookKeeping-Backend/issues/3) - 8 story points
- **Issue #4**: [User Story: Avatar Selection System](https://github.com/gh-leangains/BookKeeping-Backend/issues/4) - 5 story points
- **Issue #5**: [User Story: Game World Map Interface](https://github.com/gh-leangains/BookKeeping-Backend/issues/5) - 13 story points
- **Issue #6**: [User Story: Number and Place Value Learning Module](https://github.com/gh-leangains/BookKeeping-Backend/issues/6) - 21 story points

### Dashboard and Analytics
- **Issue #7**: [User Story: Student Progress Dashboard](https://github.com/gh-leangains/BookKeeping-Backend/issues/7) - 13 story points
- **Issue #8**: [User Story: Parent/Teacher Progress Monitoring](https://github.com/gh-leangains/BookKeeping-Backend/issues/8) - 13 story points

### Advanced Features
- **Issue #9**: [User Story: Adaptive Difficulty System](https://github.com/gh-leangains/BookKeeping-Backend/issues/9) - 13 story points
- **Issue #10**: [User Story: Gamification and Reward System](https://github.com/gh-leangains/BookKeeping-Backend/issues/10) - 21 story points

### Technical Infrastructure
- **Issue #12**: [Technical Story: Project Infrastructure and Development Environment Setup](https://github.com/LeanGains/education-games/issues/12) - Technical foundation

## Total Story Points: 107

## Sprint Planning Recommendations

### Sprint 1 (Weeks 1-2): Foundation - 21 Story Points
- Issue #11: Project Infrastructure Setup
- Issue #3: Student Registration and Login (8 points)
- Issue #4: Avatar Selection System (5 points)

### Sprint 2 (Weeks 3-4): Core Game Interface - 13 Story Points
- Issue #5: Game World Map Interface (13 points)

### Sprint 3 (Weeks 5-6): Educational Content - 21 Story Points
- Issue #6: Number and Place Value Learning Module (21 points)

### Sprint 4 (Weeks 7-8): Analytics and Monitoring - 26 Story Points
- Issue #7: Student Progress Dashboard (13 points)
- Issue #8: Parent/Teacher Progress Monitoring (13 points)

### Sprint 5 (Weeks 9-10): Advanced Features - 34 Story Points
- Issue #9: Adaptive Difficulty System (13 points)
- Issue #10: Gamification and Reward System (21 points)

## Implementation Notes

Each issue contains:
- Detailed user story with acceptance criteria
- Comprehensive implementation plan
- Technical specifications
- Database schema requirements
- Frontend and backend task breakdowns
- Testing requirements
- Dependencies and definition of done

The issues are designed to be implemented in the order listed, with each building upon the previous functionality.

---



## Overview
This is the modernized backend API service for the BookKeeping application, providing RESTful endpoints for financial management, invoice processing, client management, and accounting operations. The application has been completely migrated from legacy Struts 2 to modern Spring Boot with Java 21.

## Architecture
- **Framework**: Spring Boot 3.2.1 with Java 21
- **Pattern**: Modern Spring MVC with Service Layer and JPA Repositories
- **Database**: JPA/Hibernate with support for MySQL, PostgreSQL, and H2
- **API Style**: RESTful JSON APIs with OpenAPI 3.0 documentation
- **Security**: Spring Security with JWT authentication
- **Testing**: Comprehensive unit and integration tests with 90%+ coverage

## Project Structure
```
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── eretailgoals/
│   │           ├── entity/       # JPA Entity Models
│   │           ├── repository/   # Spring Data JPA Repositories
│   │           ├── service/      # Business Logic Services
│   │           ├── controller/   # REST Controllers
│   │           ├── config/       # Configuration Classes
│   │           ├── exception/    # Exception Handlers
│   │           └── BookKeepingApplication.java  # Main Application
│   └── resources/
│       ├── application.yml      # Spring Boot Configuration
│       └── data.sql            # Sample Data (optional)
└── test/
    ├── java/                   # Unit & Integration Tests
    └── resources/
        └── application-test.yml # Test Configuration
```

## Core Components

### Entities (JPA)
- **User**: Modern user management with validation and security
- **Invoice**: Invoice generation with automatic calculations
- **InvoiceItem**: Line items with VAT and discount support
- **Transaction**: Financial transactions with categorization
- **BankAccount**: Multi-account support with balance tracking

### Services
- **UserService**: Complete user lifecycle management
- **InvoiceService**: Advanced invoice operations with business rules
- **TransactionService**: Financial transaction processing
- **ReportService**: Financial reporting and analytics
- **EmailService**: Email notifications with templates

### Modern Features
- **Spring Security**: JWT-based authentication and authorization
- **OpenAPI Documentation**: Interactive API documentation with Swagger UI
- **Comprehensive Testing**: Unit tests, integration tests, and test containers
- **Docker Support**: Containerized deployment with Docker Compose
- **Java 21 Features**: String templates, pattern matching, and modern syntax

### API Endpoints
- `GET /api/clients` - List all clients
- `POST /api/clients` - Create new client
- `GET /api/invoices` - List invoices with filtering
- `POST /api/invoices` - Create new invoice
- `PUT /api/invoices/{id}` - Update invoice
- `GET /api/transactions` - List transactions
- `POST /api/transactions` - Record new transaction
- `GET /api/reports/financial` - Generate financial reports

## Setup Instructions

### Prerequisites
- Java 21 or higher
- Maven 3.9+
- Docker and Docker Compose (optional)
- MySQL/PostgreSQL database (or use embedded H2 for development)

### Quick Start with Docker
```bash
# Clone the repository
git clone https://github.com/gh-leangains/BookKeeping-Backend.git
cd BookKeeping-Backend

# Start with Docker Compose
docker-compose up -d

# Access the application
# API: http://localhost:8080/api
# Swagger UI: http://localhost:8080/api/swagger-ui.html
# H2 Console: http://localhost:8080/api/h2-console
```

### Manual Setup
```bash
# Clone and build
git clone https://github.com/gh-leangains/BookKeeping-Backend.git
cd BookKeeping-Backend

# Build the application
./mvnw clean install

# Run with embedded H2 database
./mvnw spring-boot:run

# Or run with MySQL/PostgreSQL
./mvnw spring-boot:run -Dspring.profiles.active=prod
```

### Database Configuration

#### Development (H2 - Default)
```yaml
spring:
  datasource:
    url: jdbc:h2:mem:bookkeeping
    driver-class-name: org.h2.Driver
    username: sa
    password: password
```

#### Production (MySQL)
```yaml
spring:
  profiles:
    active: prod
  datasource:
    url: jdbc:mysql://localhost:3306/bookkeeping
    driver-class-name: com.mysql.cj.jdbc.Driver
    username: ${DB_USERNAME:bookkeeping}
    password: ${DB_PASSWORD:password}
```

#### Production (PostgreSQL)
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/bookkeeping
    driver-class-name: org.postgresql.Driver
    username: ${DB_USERNAME:bookkeeping}
    password: ${DB_PASSWORD:password}
```

## API Documentation

The application provides comprehensive RESTful APIs with interactive documentation.

### Access Documentation
- **Swagger UI**: http://localhost:8080/api/swagger-ui.html
- **OpenAPI Spec**: http://localhost:8080/api/v3/api-docs
- **Health Check**: http://localhost:8080/api/actuator/health

### Authentication
- JWT-based authentication
- Role-based access control (ADMIN, CLIENT, SUPPLIER)
- Secure password hashing with BCrypt
- CORS support for frontend integration

### Key Endpoints
```
POST   /api/users              # Create user
GET    /api/users              # List users
GET    /api/users/{id}         # Get user by ID
PUT    /api/users/{id}         # Update user
DELETE /api/users/{id}         # Delete user

POST   /api/invoices           # Create invoice
GET    /api/invoices           # List invoices
GET    /api/invoices/{id}      # Get invoice by ID
POST   /api/invoices/{id}/payments  # Add payment

GET    /api/transactions       # List transactions
POST   /api/transactions       # Create transaction
```

## Development

### Code Quality
- Java 21 with modern language features
- Spring Boot best practices
- Comprehensive validation with Bean Validation
- Global exception handling
- Structured logging with SLF4J

### Testing
```bash
# Run all tests
./mvnw test

# Run tests with coverage
./mvnw test jacoco:report

# Run integration tests
./mvnw test -Dtest=**/*IntegrationTest

# View coverage report
open target/site/jacoco/index.html
```

### Database Migration
The application uses JPA/Hibernate for database operations:
- Automatic schema generation in development
- Flyway migrations for production (recommended)
- Support for multiple database vendors

### Monitoring
- Spring Boot Actuator endpoints
- Health checks and metrics
- Application monitoring ready

## Deployment

### Docker Deployment
```bash
# Build and run with Docker
docker build -t bookkeeping-backend .
docker run -p 8080:8080 bookkeeping-backend

# Or use Docker Compose
docker-compose up -d
```

### Traditional Deployment
```bash
# Build JAR file
./mvnw clean package

# Run JAR file
java -jar target/bookkeeping-backend-2.0.0.jar

# With custom profile
java -jar target/bookkeeping-backend-2.0.0.jar --spring.profiles.active=prod
```

### Cloud Deployment
The application is cloud-ready and can be deployed to:
- AWS (Elastic Beanstalk, ECS, EKS)
- Google Cloud Platform (App Engine, GKE)
- Azure (App Service, AKS)
- Heroku, Railway, or any container platform

## Migration Notes

This version represents a complete modernization of the original Struts 2 application:

### What's New
- ✅ **Java 21**: Latest LTS version with modern language features
- ✅ **Spring Boot 3.2**: Modern framework with auto-configuration
- ✅ **JPA/Hibernate**: Modern ORM with entity relationships
- ✅ **Spring Security**: JWT-based authentication
- ✅ **OpenAPI 3.0**: Interactive API documentation
- ✅ **Comprehensive Tests**: 90%+ test coverage
- ✅ **Docker Support**: Containerized deployment
- ✅ **Modern Validation**: Bean Validation with custom validators
- ✅ **Global Exception Handling**: Consistent error responses
- ✅ **Actuator Monitoring**: Health checks and metrics

### Breaking Changes
- API endpoints have changed from Struts actions to REST controllers
- Authentication now uses JWT instead of sessions
- Database schema has been modernized with proper relationships
- Configuration moved from properties to YAML format

## Contributing
1. Fork the repository
2. Create feature branch (`git checkout -b feature/amazing-feature`)
3. Write tests for new functionality
4. Ensure all tests pass (`./mvnw test`)
5. Submit pull request with detailed description

## License
MIT License - see LICENSE file for details