# VisaFlow

A comprehensive REST API for managing visa applications and user information. Built with Spring Boot 3.3.0 and Java 21 LTS.

## Features

✨ **User Management**
- User registration and authentication
- Role-based access control
- JWT-based token authentication

✨ **Visa Management**
- Create, read, update, and delete visa applications
- Track visa status and details
- Secure API endpoints with Spring Security

✨ **Security**
- JWT authentication with custom filters
- Password encryption with Spring Security
- CORS support for secure cross-origin requests
- Secured endpoints with role-based authorization

✨ **Database**
- H2 in-memory database (development)
- Hibernate ORM for data persistence
- Spring Data JPA repositories

## Tech Stack

| Technology | Version | Purpose |
|-----------|---------|---------|
| Java | 21 LTS | Runtime environment |
| Spring Boot | 3.3.0 | Web framework |
| Spring Security | Latest | Authentication & Authorization |
| Spring Data JPA | Latest | Data access layer |
| Hibernate | 6.5.2.Final | ORM framework |
| H2 Database | Latest | In-memory database |
| JWT (JJWT) | 0.11.5 | Token-based authentication |
| Maven | 3.9.11 | Build tool |
| JUnit & Spring Test | Latest | Unit testing |

## Prerequisites

- **Java 21 LTS** or higher
- **Maven 3.9.11** or higher
- **Git** (for cloning and version control)

## Installation

### 1. Clone the Repository

```bash
git clone https://github.com/salujaavaneetkaur-a11y/visa-management-system.git
cd visa-management-system
```

### 2. Build the Project

```bash
mvn clean package
```

### 3. Run the Application

```bash
# Using Maven
mvn spring-boot:run

# Or using the JAR file
java -jar target/Visa-0.0.1-SNAPSHOT.jar
```

The application will start on `http://localhost:8080`

## API Endpoints

### Authentication

#### Register User
```
POST /api/auth/register
Content-Type: application/json

{
  "username": "john_doe",
  "password": "secure_password",
  "email": "john@example.com"
}
```

#### Login
```
POST /api/auth/login
Content-Type: application/json

{
  "username": "john_doe",
  "password": "secure_password"
}

Response:
{
  "token": "eyJhbGc..."
}
```

### Visa Management

#### Get All Visas
```
GET /api/visas
Headers:
  Authorization: Bearer <token>
```

#### Get Visa by ID
```
GET /api/visas/{id}
Headers:
  Authorization: Bearer <token>
```

#### Create New Visa
```
POST /api/visas
Content-Type: application/json
Headers:
  Authorization: Bearer <token>

{
  "visaType": "STUDENT",
  "destination": "USA",
  "duration": 4
}
```

#### Update Visa
```
PUT /api/visas/{id}
Content-Type: application/json
Headers:
  Authorization: Bearer <token>

{
  "visaType": "TOURIST",
  "destination": "Canada",
  "duration": 2
}
```

#### Delete Visa
```
DELETE /api/visas/{id}
Headers:
  Authorization: Bearer <token>
```

## Project Structure

```
visa-management-system/
├── src/
│   ├── main/
│   │   ├── java/com/fresco/Visa/
│   │   │   ├── VisaApplication.java          # Main Spring Boot application
│   │   │   ├── Auth/
│   │   │   │   ├── AuthenticationRequest.java
│   │   │   │   └── AuthenticationResponse.java
│   │   │   ├── Config/
│   │   │   │   ├── DataLoader.java           # Initial data setup
│   │   │   │   ├── SecurityConfig.java       # Spring Security configuration
│   │   │   │   ├── UserInfoUserDetails.java
│   │   │   │   └── UserInfoUserDetailsService.java
│   │   │   ├── Controllers/
│   │   │   │   └── Controller.java           # REST API endpoints
│   │   │   ├── Dto/
│   │   │   │   ├── DeleteDto.java
│   │   │   │   └── UpdateDto.java
│   │   │   ├── Entities/
│   │   │   │   ├── UserInfo.java
│   │   │   │   └── Visa.java
│   │   │   ├── Filter/
│   │   │   │   └── JwtAuthFilter.java        # JWT authentication filter
│   │   │   ├── Repositories/
│   │   │   │   ├── UserInfoRepository.java
│   │   │   │   └── VisaRepository.java
│   │   │   └── Services/
│   │   │       ├── ApplicationService.java
│   │   │       ├── AuthService.java
│   │   │       └── JwtService.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       └── java/.../VisaApplicationTests.java
├── pom.xml
├── mvnw                          # Maven wrapper
├── README.md
├── .gitignore
└── JAVA_UPGRADE_SUMMARY.md      # Java 21 upgrade details
```

## Configuration

### Application Properties

Edit `src/main/resources/application.properties`:

```properties
# Server Configuration
server.port=8080
spring.application.name=Visa Management System

# Database Configuration
spring.datasource.url=jdbc:h2:mem:visadb
spring.datasource.driver-class-name=org.h2.Driver
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect

# JPA/Hibernate Configuration
spring.jpa.hibernate.ddl-auto=create-drop
spring.h2.console.enabled=true

# JWT Configuration
jwt.secret=your_secret_key_here
jwt.expiration=86400000
```

## Running Tests

Execute unit tests to ensure everything is working correctly:

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=VisaApplicationTests

# Run with coverage
mvn test jacoco:report
```

Expected test results:
- ✅ Tests run: 13
- ✅ Failures: 0
- ✅ Errors: 0

## Database

### H2 Console

Access the H2 database console:
- **URL**: `http://localhost:8080/h2-console`
- **JDBC URL**: `jdbc:h2:mem:visadb`
- **Username**: `sa`
- **Password**: (leave blank)

### Database Schema

**Users Table**
```sql
CREATE TABLE user_info (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255),
    roles VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

**Visas Table**
```sql
CREATE TABLE visa (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    visa_type VARCHAR(100),
    destination VARCHAR(255),
    duration INT,
    status VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user_info(id)
);
```

## Security Implementation

### JWT Authentication Flow

1. **User Registration**: Create new user account
2. **User Login**: Receive JWT token
3. **Token Usage**: Include token in Authorization header
4. **Token Validation**: JWT filter validates token on each request
5. **Role-Based Access**: Spring Security checks user roles

### Key Security Features

- ✅ Password encryption using Spring Security's BCryptPasswordEncoder
- ✅ JWT token generation and validation
- ✅ Custom JWT authentication filter
- ✅ CORS configuration for secure cross-origin requests
- ✅ Role-based authorization on endpoints

## Java 21 Upgrade

This project has been upgraded to **Java 21 LTS** with:
- ✅ Full backward compatibility maintained
- ✅ All 13 unit tests passing
- ✅ Zero compilation errors
- ✅ Successfully tested with Spring Boot 3.3.0

See `JAVA_UPGRADE_SUMMARY.md` for detailed upgrade information.

## Development Workflow

### Building

```bash
# Clean build
mvn clean package

# Skip tests
mvn clean package -DskipTests

# Build and run
mvn clean package && java -jar target/Visa-0.0.1-SNAPSHOT.jar
```

### IDE Setup (IntelliJ IDEA)

1. Open project
2. Configure JDK: Set Project SDK to Java 21
3. Enable annotation processing for Lombok
4. Run `VisaApplication.java` as main class

### IDE Setup (Eclipse)

1. Import as Maven project
2. Configure JRE: Window > Preferences > Java > Installed JREs > Set to Java 21
3. Maven > Update Project
4. Run `VisaApplication.java` as Spring Boot App

## Troubleshooting

### Port Already in Use
```bash
# Change port in application.properties
server.port=8081

# Or kill process on port 8080
lsof -ti:8080 | xargs kill -9
```

### Maven Build Issues
```bash
# Clear Maven cache
rm -rf ~/.m2/repository
mvn clean install
```

### JWT Token Expired
- Tokens expire after 24 hours by default
- Login again to get a new token
- Adjust expiration in `application.properties`

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit changes (`git commit -m 'Add amazing feature'`)
4. Push to branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Author

**Avaneet**
- GitHub: [@salujaavaneetkaur-a11y](https://github.com/salujaavaneetkaur-a11y)
- Email: salujaavaneetkaur@gmail.com

## Acknowledgments

- Spring Boot framework and community
- Spring Security for authentication/authorization
- JWT for token-based authentication
- H2 Database for embedded database support

## Contact & Support

For questions or support:
- Open an issue on GitHub
- Email: salujaavaneetkaur@gmail.com
- LinkedIn: [Avaneet Kaur Saluja](https://linkedin.com/in/avaneet-kaur-saluja)

---

**Project Status**: ✅ Active Development
**Last Updated**: December 14, 2025
**Java Version**: 21 LTS
**Spring Boot Version**: 3.3.0
