# Inventory Management System API

A comprehensive Spring Boot REST API for managing inventory products with stock tracking and low stock alerts.

## 🚀 Features

- **Product Management**: CRUD operations for products
- **Inventory Control**: Increase/decrease stock with validation
- **Low Stock Alerts**: Automatic detection of products below threshold
- **RESTful API**: Clean, well-documented REST endpoints
- **Database Integration**: MySQL with JPA/Hibernate
- **Docker Support**: Containerized application with Docker Compose
- **API Documentation**: Swagger/OpenAPI 3.0 documentation
- **Comprehensive Testing**: JUnit 5 + Mockito unit tests
- **CI/CD Pipeline**: GitHub Actions for automated testing and building

## 🛠 Tech Stack

- **Java 11**
- **Spring Boot 3.2.0**
- **Spring Data JPA**
- **MySQL 8.0**
- **Maven**
- **Lombok**
- **Docker & Docker Compose**
- **JUnit 5 & Mockito**
- **Swagger/OpenAPI 3.0**

## 📋 Prerequisites

- Java 11 or higher
- Maven 3.6+
- MySQL 8.0+ (or Docker)
- Docker & Docker Compose (optional)

## 🚀 Quick Start

### Option 1: Using Docker Compose (Recommended)

1. Clone the repository:
```bash
git clone <repository-url>
cd inventory-management
```

2. Start the application with Docker Compose:
```bash
docker-compose up -d
```

3. Access the application:
- API: http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui.html
- API Docs: http://localhost:8080/api-docs

### Option 2: Local Development

1. Start MySQL database:
```bash
# Using Docker
docker run --name inventory-mysql -e MYSQL_ROOT_PASSWORD=password -e MYSQL_DATABASE=inventory_db -p 3306:3306 -d mysql:8.0
```

2. Run the application:
```bash
./mvnw spring-boot:run
```

## 📚 API Endpoints

### Product Management
- `POST /products` - Create a new product
- `GET /products` - Get all products
- `GET /products/{id}` - Get product by ID
- `PUT /products/{id}` - Update product details
- `DELETE /products/{id}` - Delete product

### Inventory Management
- `POST /products/{id}/increase` - Increase stock
- `POST /products/{id}/decrease` - Decrease stock
- `GET /products/low-stock` - Get low stock products

## 🧪 Testing

Run the test suite:
```bash
./mvnw test
```

Run tests with coverage:
```bash
./mvnw test jacoco:report
```

## 📖 API Documentation

Once the application is running, visit:
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/api-docs

## 🐳 Docker

### Build Docker Image
```bash
docker build -t inventory-management .
```

### Run with Docker Compose
```bash
docker-compose up -d
```

### Stop Services
```bash
docker-compose down
```

## 📦 Postman Collection

Import the Postman collection from `postman/Inventory-Management-API.postman_collection.json` to test all endpoints.

## 🔧 Configuration

### Database Configuration
Update `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/inventory_db
spring.datasource.username=root
spring.datasource.password=password
```

### Environment Variables
- `SPRING_DATASOURCE_URL` - Database URL
- `SPRING_DATASOURCE_USERNAME` - Database username
- `SPRING_DATASOURCE_PASSWORD` - Database password

## 🏗 Project Structure

```
src/
├── main/
│   ├── java/com/verto/inventory/
│   │   ├── controller/          # REST controllers
│   │   ├── service/            # Business logic
│   │   ├── repository/         # Data access layer
│   │   ├── entity/             # JPA entities
│   │   ├── dto/                # Data transfer objects
│   │   └── config/             # Configuration classes
│   └── resources/
│       ├── application.properties
│       └── data.sql            # Sample data
└── test/
    └── java/com/verto/inventory/
        ├── controller/         # Controller tests
        └── service/           # Service tests
```

## 🚦 CI/CD Pipeline

The project includes a GitHub Actions workflow that:
- Runs tests on MySQL database
- Builds the application
- Performs security scans
- Builds Docker images (on main branch)

## 📝 Sample Data

The application loads sample products on startup via `data.sql`:
- 10 sample products with various stock levels
- Different low stock thresholds for testing

## 🔍 Monitoring & Health Checks

- Health endpoint: `GET /actuator/health`
- Application metrics: `GET /actuator/metrics`

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests for new functionality
5. Ensure all tests pass
6. Submit a pull request

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 🆘 Support

For support and questions:
- Create an issue in the repository
- Check the API documentation at `/swagger-ui.html`
- Review the Postman collection for examples
