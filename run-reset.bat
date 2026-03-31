@echo off
REM Script để reset database và khởi động app

echo ========================================
echo CAOXUANBINH - Database Reset & Run
echo ========================================
echo.

REM Kiểm tra MySQL có chạy không
echo Checking MySQL...
mysql -u root -e "SELECT 1" >nul 2>&1

if errorlevel 1 (
    echo ❌ MySQL not running!
    echo.
    echo Please start MySQL first:
    echo Windows: Services ^> MySQL80 ^> Right-click ^> Start
    echo Or run: net start MySQL80
    echo.
    pause
    exit /b 1
)

echo ✓ MySQL is running
echo.

REM Reset database
echo Resetting database...
mysql -u root -e "DROP DATABASE IF EXISTS WebBanHang; CREATE DATABASE WebBanHang;"

if errorlevel 1 (
    echo ❌ Failed to reset database!
    echo Check your MySQL password in application.properties
    pause
    exit /b 1
)

echo ✓ Database reset successful
echo.

REM Clean build
echo Cleaning build...
mvn clean install

if errorlevel 1 (
    echo ❌ Maven build failed!
    pause
    exit /b 1
)

echo ✓ Build successful
echo.

REM Run app
echo Starting application...
echo ========================================
echo Watch for: "Sample data initialization completed!"
echo ========================================
echo.

mvn spring-boot:run

pause
