# BookKeeping Backend API

## Overview
This is the backend API service for the BookKeeping application, providing RESTful endpoints for financial management, invoice processing, client management, and accounting operations.

## Architecture
- **Framework**: Java with Struts 2
- **Pattern**: MVC with Service Layer and DAO Pattern
- **Database**: Configurable (MySQL/PostgreSQL recommended)
- **API Style**: RESTful JSON APIs

## Project Structure
```
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── eretailgoals/
│   │           ├── dao/          # Data Access Objects
│   │           ├── model/        # Business Entity Models
│   │           ├── service/      # Business Logic Services
│   │           ├── struts/       # Struts Actions (Controllers)
│   │           └── utils/        # Utility Classes
│   └── resources/
│       ├── struts.xml           # Struts Configuration
│       └── database.properties  # Database Configuration
└── test/                        # Unit Tests
```

## Core Components

### Models
- **Client**: Customer management
- **Supplier**: Vendor management  
- **Invoice**: Invoice generation and tracking
- **Transaction**: Financial transactions
- **BankAccount**: Bank account management
- **User**: Authentication and authorization

### Services
- **ClientService**: Client CRUD operations
- **InvoiceService**: Invoice management and PDF generation
- **TransactionService**: Financial transaction processing
- **ReportService**: Financial reporting and analytics
- **EmailService**: Email notifications and document delivery

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
- Java 8 or higher
- Maven 3.6+
- MySQL/PostgreSQL database
- Application server (Tomcat 9+)

### Configuration
1. Clone the repository
2. Configure database connection in `src/main/resources/database.properties`
3. Run database migrations
4. Build with Maven: `mvn clean install`
5. Deploy WAR file to application server

### Database Configuration
```properties
db.driver=com.mysql.cj.jdbc.Driver
db.url=jdbc:mysql://localhost:3306/bookkeeping
db.username=your_username
db.password=your_password
```

## API Documentation
The backend provides RESTful APIs that return JSON responses. All endpoints require authentication except for login/register.

### Authentication
- Session-based authentication
- Role-based access control (Admin, User)
- JWT token support for API access

### Response Format
```json
{
  "success": true,
  "data": {...},
  "message": "Operation completed successfully",
  "timestamp": "2024-01-15T10:30:00Z"
}
```

## Development
- Follow MVC pattern
- Use service layer for business logic
- Implement proper error handling
- Write unit tests for all services
- Use DAO pattern for data access

## Deployment
The backend can be deployed as a WAR file to any Java application server or containerized with Docker for cloud deployment.

## Contributing
1. Fork the repository
2. Create feature branch
3. Write tests for new functionality
4. Submit pull request

## License
MIT License - see LICENSE file for details