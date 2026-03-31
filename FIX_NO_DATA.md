# 🔧 FIX: Không có dữ liệu khi gọi API

## ❌ Vấn đề
```
GET http://localhost:8080/api/products
→ Trả về: [] (mảng trống)
```

---

## ✅ Giải pháp

### Bước 1: Đảm bảo MySQL Đang Chạy

#### Windows
1. Mở **Services** (nhấn Win + R, gõ `services.msc`)
2. Tìm **MySQL80** hoặc **MySQL57**
3. Nhấp chuột phải → **Start** (nếu chưa chạy)
4. Kiểm tra status: **Running** ✓

#### macOS
```bash
brew services start mysql
```

#### Linux
```bash
sudo systemctl start mysql
```

---

### Bước 2: Kiểm tra Password MySQL

Mở file `application.properties`:
```properties
spring.datasource.username=root
spring.datasource.password=          # <- Để trống nếu không có password
```

**Nếu bạn có password MySQL:**
```properties
spring.datasource.password=YOUR_PASSWORD
```

---

### Bước 3: Xóa Database Cũ (Reset)

#### Cách 1: Dùng MySQL Command
```bash
mysql -u root -p
# Nhập password (hoặc Enter nếu không có)

DROP DATABASE IF EXISTS WebBanHang;
CREATE DATABASE WebBanHang;
EXIT;
```

#### Cách 2: Dùng MySQL Workbench
1. Mở MySQL Workbench
2. Kết nối đến server
3. Right-click `WebBanHang` → Drop Schema
4. Create Database mới tên `WebBanHang`

---

### Bước 4: Khởi động Lại Application

```bash
# Dừng app nếu đang chạy (Ctrl + C)

# Clean build
mvn clean install

# Chạy lại
mvn spring-boot:run
```

#### Chờ khi thấy log:
```
========================================
Initializing sample data...
========================================
✓ Categories initialized
✓ Products initialized
========================================
Sample data initialization completed!
========================================
```

---

### Bước 5: Test API

Mở browser:
```
http://localhost:8080/api/products
```

✅ Khi thành công sẽ thấy:
```json
[
  {
    "id": 1,
    "name": "iPhone 16 Pro Max",
    "price": 33990000,
    "description": "Flagship moi nhat tu Apple",
    ...
  },
  ...
]
```

---

## 🐛 Troubleshooting

### ❌ Vẫn không có dữ liệu?

#### Kiểm tra 1: MySQL kết nối được không?
```bash
mysql -u root -p WebBanHang
```
- Nếu lỗi → MySQL chưa chạy
- Nếu ok → Table được tạo chưa?

```sql
SHOW TABLES;
```

Nên có:
- `users`
- `roles`
- `products`
- `categories`
- `cart_items`
- `orders`
- `order_details`
- `point_vouchers`

#### Kiểm tra 2: Xem Console Log
Mở console Spring Boot, tìm:
```
ERROR
Exception
```

Copy message lỗi để fix

#### Kiểm tra 3: Xóa hết data và reset
```sql
DELETE FROM products;
DELETE FROM categories;
DELETE FROM users;
```

Rồi khởi động lại app

#### Kiểm tra 4: Thử tạo product thủ công
```bash
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{"name":"Test","price":100000,"description":"Test"}'
```

Nếu được → POST hoạt động
Nếu lỗi → Có vấn đề với API

---

## 📝 Giải pháp nếu vẫn fail

### Option 1: Kiểm tra DataInitializer
File `DataInitializer.java` được tạo sẽ chạy tự động khi app start.
Kiểm tra console xem log của nó có không.

### Option 2: Gọi API thủ công để trigger seeding
```bash
# Cái này sẽ gọi ensureSampleProducts()
curl http://localhost:8080/api/products
```

### Option 3: Tạo user admin bằng curl
```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "username":"admin",
    "password":"admin123",
    "email":"admin@example.com",
    "phone":"0123456789"
  }'
```

### Option 4: Reset toàn bộ database

Sửa `application.properties`:
```properties
spring.jpa.hibernate.ddl-auto=create  # Thay từ update
```

Khởi động app (sẽ xóa và tạo lại tất cả tables)

Sau đó sửa lại:
```properties
spring.jpa.hibernate.ddl-auto=update
```

Khởi động lại app

---

## ✅ Nếu vẫn không được

Gửi console log khi run:
```bash
mvn spring-boot:run 2>&1 | tee app.log
```

Copy toàn bộ nội dung file `app.log` để debug

---

## 📞 Quick Check

Chạy các lệnh này:

```bash
# 1. Kiểm tra MySQL
mysql -u root -p WebBanHang -e "SELECT COUNT(*) as TableCount FROM information_schema.tables WHERE table_schema='WebBanHang';"

# 2. Kiểm tra Products
mysql -u root -p WebBanHang -e "SELECT COUNT(*) as ProductCount FROM products;"

# 3. Kiểm tra Categories
mysql -u root -p WebBanHang -e "SELECT COUNT(*) as CategoryCount FROM categories;"

# 4. Test API
curl http://localhost:8080/api/products | head -20
```

---

**Nếu mọi thứ đúng:**
- MySQL running ✓
- Password đúng ✓
- Database tạo ✓
- App khởi động ✓

→ Sẽ có dữ liệu ✓✓✓
