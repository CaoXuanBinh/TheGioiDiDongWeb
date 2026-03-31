-- Create Database
CREATE DATABASE IF NOT EXISTS WebBanHang;
USE WebBanHang;

-- Create Tables (Spring JPA sẽ tạo, nhưng để chắc chắn)
-- Bạn có thể chạy lệnh sau để xóa database và reset:
-- DROP DATABASE WebBanHang;
-- CREATE DATABASE WebBanHang;

-- Sau đó khởi động Spring Boot:
-- mvn spring-boot:run

-- Nếu vẫn không có dữ liệu, kiểm tra:
-- 1. MySQL đã chạy? 
--    Windows: Services > MySQL80 hoặc MySQL57
--    macOS: brew services start mysql
--    Linux: sudo systemctl start mysql

-- 2. Root password đúng?
--    Mở application.properties và kiểm tra:
--    spring.datasource.username=root
--    spring.datasource.password=

-- 3. Check console log xem có lỗi gì không

-- Test thử:
SELECT COUNT(*) FROM products;
SELECT COUNT(*) FROM categories;
SELECT COUNT(*) FROM users;
