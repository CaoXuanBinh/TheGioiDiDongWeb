#!/bin/bash

# Script để reset database và khởi động app

echo "========================================"
echo "CAOXUANBINH - Database Reset & Run"
echo "========================================"
echo ""

# Kiểm tra MySQL có chạy không
echo "Checking MySQL..."
mysql -u root -e "SELECT 1" >/dev/null 2>&1

if [ $? -ne 0 ]; then
    echo "❌ MySQL not running!"
    echo ""
    echo "Please start MySQL first:"
    echo "macOS: brew services start mysql"
    echo "Linux: sudo systemctl start mysql"
    echo ""
    exit 1
fi

echo "✓ MySQL is running"
echo ""

# Reset database
echo "Resetting database..."
mysql -u root -e "DROP DATABASE IF EXISTS WebBanHang; CREATE DATABASE WebBanHang;"

if [ $? -ne 0 ]; then
    echo "❌ Failed to reset database!"
    echo "Check your MySQL password in application.properties"
    exit 1
fi

echo "✓ Database reset successful"
echo ""

# Clean build
echo "Cleaning build..."
mvn clean install

if [ $? -ne 0 ]; then
    echo "❌ Maven build failed!"
    exit 1
fi

echo "✓ Build successful"
echo ""

# Run app
echo "Starting application..."
echo "========================================"
echo "Watch for: 'Sample data initialization completed!'"
echo "========================================"
echo ""

mvn spring-boot:run
