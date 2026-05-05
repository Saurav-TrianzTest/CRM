# CRM Application - PostgreSQL Quick Start

## Prerequisites
- Java 17 or higher
- Maven 3.6+
- PostgreSQL 16 installed and running

## Quick Setup

### 1. Create PostgreSQL Database
```bash
# Connect to PostgreSQL
psql -U postgres

# Create database
CREATE DATABASE crm;

# Exit psql
\q
```

### 2. Configure Database Connection
Edit `src/main/resources/application.properties` if needed:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/crm
spring.datasource.username=postgres
spring.datasource.password=password
```

### 3. Run the Application
```bash
mvn spring-boot:run
```

### 4. Access the Application
Open your browser and navigate to:
```
http://localhost:8080
```

## Default Users
| Username | Password | Role |
|----------|----------|------|
| admin    | admin    | ROLE_ADMIN |
| user     | user     | ROLE_USER |
| manager  | manager  | ROLE_MANAGER |
| owner    | owner    | ROLE_OWNER |

## What Changed from MySQL?

### Dependencies
- ✅ Replaced MySQL connector with PostgreSQL driver
- ✅ Updated to use `org.postgresql:postgresql`

### Configuration
- ✅ Changed JDBC URL from `jdbc:mysql://` to `jdbc:postgresql://`
- ✅ Added PostgreSQL dialect configuration
- ✅ Added PostgreSQL-specific JPA properties

### Entity Classes
- ✅ Changed ID generation from `AUTO` to `IDENTITY`
- ✅ All entities now use PostgreSQL-compatible ID generation

### SQL Scripts
- ✅ Updated `data.sql` for PostgreSQL syntax
- ✅ Added sequence reset statements
- ✅ Fixed timestamp format

### Native Queries
- ✅ Updated native SQL queries to PostgreSQL syntax
- ✅ Removed MySQL-specific schema prefixes

## Troubleshooting

### Database Connection Error
```
Error: Connection refused
```
**Solution**: Ensure PostgreSQL is running:
```bash
# Linux/Mac
sudo systemctl status postgresql

# Windows
# Check Services for PostgreSQL service
```

### Authentication Error
```
Error: password authentication failed
```
**Solution**: Update the password in `application.properties` to match your PostgreSQL setup.

### Database Not Found
```
Error: database "crm" does not exist
```
**Solution**: Create the database using the SQL command above.

## Features
- User Management with Role-Based Access Control
- Customer Management
- Contract Management
- Category Management
- PDF Generation and Management
- Data Export (CSV, Excel, PDF)
- Search and Filter Capabilities

## Technology Stack
- Spring Boot 3.2.0
- Spring Data JPA
- Spring Security
- PostgreSQL 16
- Thymeleaf
- Lombok
- iText PDF
- Apache POI (Excel)
- OpenCSV

## Project Structure
```
src/
├── main/
│   ├── java/crm/
│   │   ├── controller/     # REST and Web Controllers
│   │   ├── entity/         # JPA Entities
│   │   ├── repository/     # Spring Data Repositories
│   │   ├── service/        # Business Logic
│   │   ├── view/           # Custom View Resolvers
│   │   └── utils/          # Utility Classes
│   └── resources/
│       ├── application.properties  # Configuration
│       ├── data.sql               # Initial Data
│       └── messages.properties    # i18n Messages
└── test/
    └── java/crm/          # Test Classes
```

## API Endpoints
- `/` - Home page
- `/login` - Login page
- `/register` - User registration
- `/customers` - Customer management
- `/contracts` - Contract management
- `/users` - User management (Admin only)
- `/export` - Data export functionality

## Development

### Build the Project
```bash
mvn clean install
```

### Run Tests
```bash
mvn test
```

### Package as JAR
```bash
mvn package
```

The JAR file will be created in the `target/` directory.

### Run the JAR
```bash
java -jar target/crm-0.0.1-SNAPSHOT.jar
```

## Database Schema
The application uses Hibernate DDL auto-generation with `create-drop` strategy. On startup:
1. All tables are dropped (if they exist)
2. New tables are created based on entity definitions
3. Initial data is loaded from `data.sql`

### Main Tables
- `users` - Application users
- `role` - User roles
- `customer` - Customer information
- `category` - Customer categories
- `customer_category` - Many-to-many relationship
- `contract` - Contract information
- `pdf` - PDF document metadata

## Production Considerations

### Change DDL Strategy
For production, update `application.properties`:
```properties
# Change from create-drop to validate or update
spring.jpa.hibernate.ddl-auto=validate
```

### Enable Connection Pooling
Add HikariCP configuration:
```properties
spring.datasource.hikari.maximum-pool-size=10
spring.datasource.hikari.minimum-idle=5
spring.datasource.hikari.connection-timeout=30000
```

### Security
- Change default passwords
- Use environment variables for sensitive data
- Enable HTTPS
- Configure CORS properly
- Implement rate limiting

## Migration from MySQL
If you're migrating from MySQL, see `POSTGRESQL_MIGRATION_GUIDE.md` for detailed information about:
- All changes made
- Differences between MySQL and PostgreSQL
- Rollback procedures
- Performance optimization tips

## Support
For detailed migration information and troubleshooting, refer to:
- `POSTGRESQL_MIGRATION_GUIDE.md` - Complete migration documentation
- PostgreSQL Documentation: https://www.postgresql.org/docs/16/
- Spring Boot Documentation: https://docs.spring.io/spring-boot/docs/3.2.0/reference/html/

---
**Version**: 0.0.1-SNAPSHOT  
**Database**: PostgreSQL 16  
**Framework**: Spring Boot 3.2.0  
**Java**: 17
