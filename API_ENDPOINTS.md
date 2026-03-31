# API Endpoints - CAOXUANBINH Project

**Base URL:** `http://localhost:8080`

---

## 📦 PRODUCT API (`/api/products`)

| Method | Endpoint | Description | Request Body |
|--------|----------|-------------|--------------|
| GET | `/api/products` | Lấy tất cả sản phẩm | - |
| GET | `/api/products/{id}` | Lấy sản phẩm theo ID | - |
| POST | `/api/products` | Tạo sản phẩm mới | `{ name, price, description, ... }` |
| PUT | `/api/products/{id}` | Cập nhật sản phẩm | `{ name, price, description, ... }` |
| DELETE | `/api/products/{id}` | Xóa sản phẩm | - |

**Ví dụ Product Object:**
```json
{
  "id": 1,
  "name": "iPhone 16 Pro Max",
  "price": 33990000,
  "description": "Flagship moi nhat tu Apple",
  "image": "iphone16promax.png",
  "promotionType": "DISCOUNT",
  "discountPercent": 10,
  "giftDescription": "",
  "stockQuantity": 50,
  "category": { "id": 1, "name": "iPhone" }
}
```

---

## 🏷️ CATEGORY API (`/api/categories`)

| Method | Endpoint | Description | Request Body |
|--------|----------|-------------|--------------|
| GET | `/api/categories` | Lấy tất cả danh mục | - |
| GET | `/api/categories/root` | Lấy danh mục cấp 1 | - |
| GET | `/api/categories/{id}` | Lấy danh mục theo ID | - |
| POST | `/api/categories` | Tạo danh mục mới | `{ name, icon, parentCategory }` |
| PUT | `/api/categories/{id}` | Cập nhật danh mục | `{ name, icon, parentCategory }` |
| DELETE | `/api/categories/{id}` | Xóa danh mục | - |

**Ví dụ Category Object:**
```json
{
  "id": 1,
  "name": "iPhone",
  "icon": "bi-apple",
  "image": null,
  "parentCategory": { "id": 2, "name": "Dien thoai" },
  "children": []
}
```

---

## 👤 USER API (`/api/users`)

| Method | Endpoint | Description | Request Body |
|--------|----------|-------------|--------------|
| GET | `/api/users` | Lấy tất cả user | - |
| GET | `/api/users/{username}` | Lấy user theo username | - |
| POST | `/api/users` | Tạo user mới | `{ username, password, email, phone }` |

**Ví dụ User Object:**
```json
{
  "id": 1,
  "username": "admin",
  "password": "encoded_password",
  "email": "admin@example.com",
  "phone": "0123456789",
  "roles": [{ "id": 1, "name": "ROLE_USER" }]
}
```

---

## 🧪 Test API với cURL

### Product
```bash
# Lấy tất cả sản phẩm
curl http://localhost:8080/api/products

# Lấy sản phẩm ID 1
curl http://localhost:8080/api/products/1

# Tạo sản phẩm
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{"name":"Test Product","price":100000,"description":"Test"}'

# Cập nhật sản phẩm ID 1
curl -X PUT http://localhost:8080/api/products/1 \
  -H "Content-Type: application/json" \
  -d '{"name":"Updated","price":150000,"description":"Updated"}'

# Xóa sản phẩm ID 1
curl -X DELETE http://localhost:8080/api/products/1
```

### Category
```bash
# Lấy tất cả danh mục
curl http://localhost:8080/api/categories

# Lấy danh mục cấp 1
curl http://localhost:8080/api/categories/root

# Tạo danh mục
curl -X POST http://localhost:8080/api/categories \
  -H "Content-Type: application/json" \
  -d '{"name":"New Category","icon":"bi-phone"}'
```

### User
```bash
# Lấy tất cả user
curl http://localhost:8080/api/users

# Lấy user theo username
curl http://localhost:8080/api/users/admin

# Tạo user
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{"username":"newuser","password":"123456","email":"user@example.com","phone":"0123456789"}'
```

---

## 📝 Lưu ý

- **CORS**: Tất cả API đều hỗ trợ CORS (`@CrossOrigin`)
- **Dữ liệu mẫu**: Sẽ tự động khởi tạo khi lần đầu tiên gọi API
- **Authentication**: Hiện tại không yêu cầu authentication cho API
- **Response Format**: JSON

---

## 🎯 Tính năng

✅ Product CRUD (GET, POST, PUT, DELETE)
✅ Category CRUD (GET, POST, PUT, DELETE)  
✅ User GET & POST
✅ Tự động khởi tạo dữ liệu mẫu
✅ CORS support
✅ Error handling
