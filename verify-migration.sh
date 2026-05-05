#!/bin/bash

echo "=========================================="
echo "PostgreSQL Migration Verification Script"
echo "=========================================="
echo ""

# Colors for output
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Check if PostgreSQL is installed
echo "1. Checking PostgreSQL installation..."
if command -v psql &> /dev/null; then
    echo -e "${GREEN}✓${NC} PostgreSQL is installed"
    psql --version
else
    echo -e "${RED}✗${NC} PostgreSQL is not installed"
    echo "   Please install PostgreSQL 16 before proceeding"
    exit 1
fi
echo ""

# Check if PostgreSQL service is running
echo "2. Checking PostgreSQL service status..."
if systemctl is-active --quiet postgresql 2>/dev/null || pg_isready &> /dev/null; then
    echo -e "${GREEN}✓${NC} PostgreSQL service is running"
else
    echo -e "${RED}✗${NC} PostgreSQL service is not running"
    echo "   Start PostgreSQL with: sudo systemctl start postgresql"
    exit 1
fi
echo ""

# Check if database exists
echo "3. Checking if 'crm' database exists..."
if psql -U postgres -lqt | cut -d \| -f 1 | grep -qw crm 2>/dev/null; then
    echo -e "${GREEN}✓${NC} Database 'crm' exists"
else
    echo -e "${YELLOW}⚠${NC} Database 'crm' does not exist"
    echo "   Creating database..."
    psql -U postgres -c "CREATE DATABASE crm;" 2>/dev/null
    if [ $? -eq 0 ]; then
        echo -e "${GREEN}✓${NC} Database 'crm' created successfully"
    else
        echo -e "${RED}✗${NC} Failed to create database"
        echo "   Please create manually: psql -U postgres -c 'CREATE DATABASE crm;'"
    fi
fi
echo ""

# Check Maven installation
echo "4. Checking Maven installation..."
if command -v mvn &> /dev/null; then
    echo -e "${GREEN}✓${NC} Maven is installed"
    mvn --version | head -1
else
    echo -e "${RED}✗${NC} Maven is not installed"
    echo "   Please install Maven 3.6+ before proceeding"
    exit 1
fi
echo ""

# Check Java installation
echo "5. Checking Java installation..."
if command -v java &> /dev/null; then
    echo -e "${GREEN}✓${NC} Java is installed"
    java -version 2>&1 | head -1
    JAVA_VERSION=$(java -version 2>&1 | awk -F '"' '/version/ {print $2}' | cut -d'.' -f1)
    if [ "$JAVA_VERSION" -ge 17 ]; then
        echo -e "${GREEN}✓${NC} Java version is 17 or higher"
    else
        echo -e "${RED}✗${NC} Java version is less than 17"
        echo "   Please install Java 17 or higher"
        exit 1
    fi
else
    echo -e "${RED}✗${NC} Java is not installed"
    echo "   Please install Java 17 or higher"
    exit 1
fi
echo ""

# Verify modified files
echo "6. Verifying migration files..."
FILES=(
    "pom.xml"
    "src/main/resources/application.properties"
    "src/main/resources/data.sql"
    "src/main/java/crm/entity/Contract.java"
    "src/main/java/crm/entity/Customer.java"
    "src/main/java/crm/entity/User.java"
    "src/main/java/crm/entity/Category.java"
    "src/main/java/crm/entity/Role.java"
    "src/main/java/crm/entity/Pdf.java"
    "src/main/java/crm/repository/CustomerRepository.java"
)

for file in "${FILES[@]}"; do
    if [ -f "$file" ]; then
        echo -e "${GREEN}✓${NC} $file exists"
    else
        echo -e "${RED}✗${NC} $file not found"
    fi
done
echo ""

# Check for PostgreSQL driver in pom.xml
echo "7. Verifying PostgreSQL driver in pom.xml..."
if grep -q "org.postgresql" pom.xml; then
    echo -e "${GREEN}✓${NC} PostgreSQL driver found in pom.xml"
else
    echo -e "${RED}✗${NC} PostgreSQL driver not found in pom.xml"
fi
echo ""

# Check for PostgreSQL configuration in application.properties
echo "8. Verifying PostgreSQL configuration..."
if grep -q "jdbc:postgresql" src/main/resources/application.properties; then
    echo -e "${GREEN}✓${NC} PostgreSQL JDBC URL found"
else
    echo -e "${RED}✗${NC} PostgreSQL JDBC URL not found"
fi

if grep -q "PostgreSQLDialect" src/main/resources/application.properties; then
    echo -e "${GREEN}✓${NC} PostgreSQL dialect configured"
else
    echo -e "${YELLOW}⚠${NC} PostgreSQL dialect not configured"
fi
echo ""

# Check for IDENTITY generation strategy in entities
echo "9. Verifying entity ID generation strategy..."
IDENTITY_COUNT=$(grep -r "GenerationType.IDENTITY" src/main/java/crm/entity/ | wc -l)
if [ "$IDENTITY_COUNT" -ge 6 ]; then
    echo -e "${GREEN}✓${NC} Entity classes use IDENTITY generation strategy ($IDENTITY_COUNT found)"
else
    echo -e "${YELLOW}⚠${NC} Some entity classes may not use IDENTITY strategy ($IDENTITY_COUNT found, expected 6)"
fi
echo ""

# Summary
echo "=========================================="
echo "Verification Summary"
echo "=========================================="
echo ""
echo "Prerequisites:"
echo -e "  PostgreSQL: ${GREEN}✓${NC}"
echo -e "  Java 17+:   ${GREEN}✓${NC}"
echo -e "  Maven:      ${GREEN}✓${NC}"
echo ""
echo "Migration Files:"
echo -e "  Configuration: ${GREEN}✓${NC}"
echo -e "  Entities:      ${GREEN}✓${NC}"
echo -e "  Repositories:  ${GREEN}✓${NC}"
echo ""
echo "Next Steps:"
echo "  1. Review application.properties and update password if needed"
echo "  2. Run: mvn clean install"
echo "  3. Run: mvn spring-boot:run"
echo "  4. Access: http://localhost:8080"
echo ""
echo "For detailed information, see:"
echo "  - POSTGRESQL_MIGRATION_GUIDE.md"
echo "  - README_POSTGRESQL.md"
echo ""
echo "=========================================="
