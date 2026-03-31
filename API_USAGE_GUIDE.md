# 📚 Hướng Dẫn Sử Dụng API - CAOXUANBINH Project

## 🎯 Tổng Quan

Dự án đã được thiết lập với 3 API chính:
- **Product API** - Quản lý sản phẩm
- **Category API** - Quản lý danh mục
- **User API** - Quản lý người dùng

---

## 🚀 Cách Chạy

### 1️⃣ Khởi động Spring Boot Application
```bash
mvn spring-boot:run
```

### 2️⃣ Truy cập API Test Dashboard
Mở browser và nhập: **http://localhost:8080/api-test.html**

### 3️⃣ Test API Trực Tiếp
Nhập URL: **http://localhost:8080/api/products**

---

## 📦 Product API Endpoints

**Base URL:** `http://localhost:8080/api/products`

### GET All Products
```
GET http://localhost:8080/api/products
```
✅ **Response (200 OK):**
```json
[
  {
    "id": 1,
    "name": "iPhone 16 Pro Max",
    "price": 33990000,
    "description": "Flagship moi nhat tu Apple",
    "image": "iphone16promax.png",
    "category": { "id": 1, "name": "iPhone" },
    "promotionType": "DISCOUNT",
    "discountPercent": 10,
    "stockQuantity": 50
  },
  ...
]
```

### GET Product by ID
```
GET http://localhost:8080/api/products/1
```

### POST Create Product
```
POST http://localhost:8080/api/products
Content-Type: application/json

{
  "name": "Samsung Galaxy S25",
  "price": 25000000,
  "description": "Latest Samsung flagship",
  "image": "samsung.jpg",
  "category": { "id": 5, "name": "Samsung" },
  "promotionType": "DISCOUNT",
  "discountPercent": 5,
  "stockQuantity": 20
}
```

### PUT Update Product
```
PUT http://localhost:8080/api/products/1
Content-Type: application/json

{
  "name": "iPhone 16 Pro Max Updated",
  "price": 35000000,
  "description": "Updated description"
}
```

### DELETE Product
```
DELETE http://localhost:8080/api/products/1
```

---

## 🏷️ Category API Endpoints

**Base URL:** `http://localhost:8080/api/categories`

### GET All Categories
```
GET http://localhost:8080/api/categories
```
✅ Auto-creates sample categories on first call

### GET Root Categories
```
GET http://localhost:8080/api/categories/root
```

### GET Category by ID
```
GET http://localhost:8080/api/categories/1
```

### POST Create Category
```
POST http://localhost:8080/api/categories
Content-Type: application/json

{
  "name": "New Electronics",
  "icon": "bi-cpu"
}
```

### PUT Update Category
```
PUT http://localhost:8080/api/categories/1
Content-Type: application/json

{
  "name": "Updated Electronics",
  "icon": "bi-microchip"
}
```

### DELETE Category
```
DELETE http://localhost:8080/api/categories/1
```

---

## 👤 User API Endpoints

**Base URL:** `http://localhost:8080/api/users`

### GET All Users
```
GET http://localhost:8080/api/users
```

### GET User by Username
```
GET http://localhost:8080/api/users/admin
```

### POST Create User
```
POST http://localhost:8080/api/users
Content-Type: application/json

{
  "username": "newuser",
  "password": "123456",
  "email": "user@example.com",
  "phone": "0123456789"
}
```

---

## 🧪 Test với cURL

### Product Tests
```bash
# Get all products
curl http://localhost:8080/api/products

# Get product by ID
curl http://localhost:8080/api/products/1

# Create product
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Test Product",
    "price": 100000,
    "description": "Test Description",
    "image": "test.jpg",
    "promotionType": "NONE",
    "discountPercent": 0,
    "stockQuantity": 10
  }'

# Update product
curl -X PUT http://localhost:8080/api/products/1 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Updated Product",
    "price": 150000,
    "description": "Updated Description"
  }'

# Delete product
curl -X DELETE http://localhost:8080/api/products/1
```

### Category Tests
```bash
# Get all categories
curl http://localhost:8080/api/categories

# Get root categories
curl http://localhost:8080/api/categories/root

# Create category
curl -X POST http://localhost:8080/api/categories \
  -H "Content-Type: application/json" \
  -d '{"name": "New Category", "icon": "bi-phone"}'

# Update category
curl -X PUT http://localhost:8080/api/categories/1 \
  -H "Content-Type: application/json" \
  -d '{"name": "Updated Category", "icon": "bi-laptop"}'

# Delete category
curl -X DELETE http://localhost:8080/api/categories/1
```

### User Tests
```bash
# Get all users
curl http://localhost:8080/api/users

# Get user by username
curl http://localhost:8080/api/users/admin

# Create user
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "123456",
    "email": "test@example.com",
    "phone": "0123456789"
  }'
```

---

## 📝 Postman Collection

### Import to Postman

1. Mở Postman
2. Click **Import** → **Raw Text**
3. Copy collection JSON:

```json
{
  "info": {
    "name": "CAOXUANBINH API",
    "description": "Product, Category, User APIs"
  },
  "item": [
    {
      "name": "Products",
      "item": [
        {
          "name": "Get All Products",
          "request": {
            "method": "GET",
            "url": "http://localhost:8080/api/products"
          }
        },
        {
          "name": "Create Product",
          "request": {
            "method": "POST",
            "url": "http://localhost:8080/api/products",
            "body": {
              "raw": "{\"name\": \"Test\", \"price\": 100000}"
            }
          }
        }
      ]
    }
  ]
}
```

---

## ✅ Kiểm Tra Hoạt Động

1. **Khởi động server**: `mvn spring-boot:run`
2. **Truy cập**: http://localhost:8080/api-test.html
3. **Kiểm tra dữ liệu mẫu**: 
   - Sẽ có tự động 12 sản phẩm mẫu
   - Sẽ có 30 danh mục mẫu

---

## 🔒 Features

✅ **CORS Support** - Có thể gọi từ frontend khác nhau
✅ **Auto Sample Data** - Tự động tạo dữ liệu mẫu
✅ **Full CRUD** - Create, Read, Update, Delete
✅ **Error Handling** - Xử lý lỗi toàn diện
✅ **JSON Response** - Trả về JSON chuẩn

---

## 📊 Dữ Liệu Mẫu

### Sample Products (12 items)
- iPhone 16 Pro Max (33.99M)
- iPhone 15 Pro (27.99M)
- Samsung Galaxy S25 Ultra (31.99M)
- Samsung Galaxy A55 (9.49M)
- Xiaomi 14T Pro (18.99M)
- OPPO Find X8 (21.99M)
- Realme GT 7 Pro (14.99M)
- MacBook Pro M4 (54.99M)
- MacBook Air M3 (32.99M)
- Dell XPS 15 (45.99M)
- ASUS ROG Zephyrus G14 (42.99M)
- Lenovo ThinkPad X1 Carbon (38.99M)

### Sample Categories (30 items)
- Điện thoại (5 loại: iPhone, Samsung, Xiaomi, OPPO, Realme)
- Laptop (5 loại: MacBook, Dell, ASUS, Lenovo, HP)
- Máy tính bảng (3 loại: iPad, Samsung Tab, Xiaomi Pad)
- Đồng hồ (3 loại: Apple Watch, Samsung Watch, Garmin)
- Phụ kiện (14 loại: Tai nghe, Loa, Camera, chuột, bàn phím, v.v.)

---

## 🐛 Debugging

### Lỗi: "CORS error"
**Solution:** API đã có `@CrossOrigin`, hãy kiểm tra browser console

### Lỗi: "Product not found"
**Solution:** Sử dụng ID từ danh sách GET all products

### Lỗi: "Port already in use"
**Solution:** Thay port trong `application.properties`
```properties
server.port=8081
```

---

## 📞 Support

- Framework: Spring Boot 3.x
- Database: JPA/Hibernate
- Authentication: Spring Security
- API Style: RESTful

---

## 🎓 Bài Kiểm Tra

**Yêu cầu:**
- ✅ Product API: GET, POST, PUT, DELETE
- ✅ Category API: GET, POST, PUT, DELETE
- ✅ User API: GET, POST
- ✅ Dữ liệu trả về đầy đủ
- ✅ CORS hỗ trợ

**Điểm:**
- Đúng hết: **+1 điểm**
- Sai: **-1 điểm**

---

**Ngày cập nhật:** March 20, 2026
**Status:** ✅ Ready for Testing
