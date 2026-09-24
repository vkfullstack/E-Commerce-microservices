# 🛒 E-Commerce Microservices Backend

A **distributed E-Commerce Backend** built using **Java, Spring Boot, PostgreSQL, Docker, and Microservices Architecture**.

The application is divided into independent services for **user management, product catalog, and cart/order processing**. Each service has its own database and can be developed, tested, and deployed independently.

---

## 🚀 Tech Stack

| Technology           | Usage                             |
| -------------------- | --------------------------------- |
| ☕ Java 25            | Backend development               |
| 🌱 Spring Boot 4.1.1 | REST APIs & application framework |
| 🗄️ Spring Data JPA  | Database access                   |
| 🐘 PostgreSQL 14     | Relational database               |
| 🐳 Docker Compose    | Database & infrastructure         |
| 📚 Swagger / OpenAPI | API documentation                 |
| 📮 Postman           | API testing                       |
| 🧩 Lombok            | Boilerplate reduction             |
| ⚡ HikariCP           | Database connection pooling       |
| 🛠️ Maven            | Build & dependency management     |
| 🔀 Git & GitHub      | Version control                   |

---

# 🏗️ Architecture

The project follows a **Microservices Architecture** with a **Database-per-Service** design.

```text
                         ┌──────────────────────┐
                         │   Client / Postman    │
                         └──────────┬───────────┘
                                    │
             ┌──────────────────────┼──────────────────────┐
             │                      │                      │
             ▼                      ▼                      ▼
   ┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
   │  User Service   │    │ Product Service │    │  Order Service  │
   │     :8081       │    │     :8082       │    │     :8083       │
   └────────┬────────┘    └────────┬────────┘    └────────┬────────┘
            │                      │                      │
            ▼                      ▼                      ▼
       ┌─────────┐            ┌───────────┐          ┌──────────┐
       │ userdb  │            │ productdb │          │ orderdb  │
       └─────────┘            └───────────┘          └──────────┘
            │                      │                      │
            └──────────────────────┼──────────────────────┘
                                   │
                          Docker Compose
                     PostgreSQL 14 + pgAdmin
```

### Design Principles

* **Microservices Architecture**
* **Database-per-Service**
* **Layered Architecture**
* **DTO-based API design**
* **RESTful APIs**
* **Transactional Checkout**
* **Soft Delete**
* **Containerized Infrastructure**

---

# 📦 Microservices

| Service         |   Port | Database    | Responsibility            |
| --------------- | -----: | ----------- | ------------------------- |
| User Service    | `8081` | `userdb`    | User & address management |
| Product Service | `8082` | `productdb` | Product catalog & search  |
| Order Service   | `8083` | `orderdb`   | Cart & order processing   |

---

# 👤 1. User Service

Responsible for managing users and their addresses.

### Features

* Register user
* Get user by ID
* Update user
* User + Address one-to-one relationship
* Cascade mapping
* Customer role

### APIs

| Method | Endpoint          | Description   |
| ------ | ----------------- | ------------- |
| `POST` | `/api/users`      | Register user |
| `GET`  | `/api/users/{id}` | Get user      |
| `PUT`  | `/api/users/{id}` | Update user   |

### Example Request

```json
POST /api/users

{
  "name": "Vijay",
  "email": "vijay@example.com",
  "address": {
    "street": "MG Road",
    "city": "Pune",
    "state": "Maharashtra",
    "pincode": "411001"
  }
}
```

---

# 📦 2. Product Service

Responsible for product catalog management.

### Features

* Create product
* List active products
* Search products
* Update product
* Soft delete product
* Stock filtering
* JPQL-based keyword search

### APIs

| Method   | Endpoint                              | Description         |
| -------- | ------------------------------------- | ------------------- |
| `POST`   | `/api/products`                       | Create product      |
| `GET`    | `/api/products`                       | Get active products |
| `GET`    | `/api/products/search?keyword=Laptop` | Search products     |
| `DELETE` | `/api/products/{id}`                  | Soft delete product |

### Soft Delete

Products are not physically removed from the database.

```java
product.setActive(false);
```

This allows historical product references to remain available for existing orders.

---

# 🛒 3. Order Service

Responsible for cart management and order placement.

### Features

* Add product to cart
* Merge duplicate cart items
* View cart
* Calculate order total
* Create multi-item orders
* Transactional checkout
* Clear cart after successful order

### APIs

| Method | Endpoint        | Description      |
| ------ | --------------- | ---------------- |
| `POST` | `/api/cart/add` | Add item to cart |
| `GET`  | `/api/cart`     | View cart        |
| `POST` | `/api/orders`   | Place order      |

The current implementation uses the `X-USER-ID` header to identify the user.

Example:

```text
X-USER-ID: 1
```

---

# 🔄 End-to-End Order Flow

```text
1. Register User
       │
       ▼
2. Browse/Search Products
       │
       ▼
3. Add Product to Cart
       │
       ▼
4. View Cart
       │
       ▼
5. Place Order
       │
       ▼
6. Calculate Total
       │
       ▼
7. Create Order + Order Items
       │
       ▼
8. Clear Cart
       │
       ▼
9. Return Order Response
```

---

# 🗄️ Database Architecture

Each microservice owns its own database.

```text
PostgreSQL
│
├── userdb
│   ├── users
│   └── address
│
├── productdb
│   └── product
│
└── orderdb
    ├── cart_item
    ├── orders
    └── order_item
```

### Database-per-Service

Services do **not** directly access another service's database.

Cross-service relationships are represented using IDs.

For example:

```text
Order Service
    │
    └── productId ──────► Product Service
```

There is no cross-database foreign key.

---

# 🧩 Project Structure

Each service follows a layered structure:

```text
service/
├── controller/
│   └── REST Controllers
│
├── service/
│   └── Business Logic
│
├── repository/
│   └── Spring Data JPA Repositories
│
├── entity/
│   └── JPA Entities
│
└── dto/
    └── Request / Response DTOs
```

### Layered Flow

```text
Client
  │
  ▼
Controller
  │
  ▼
Service
  │
  ▼
Repository
  │
  ▼
PostgreSQL
```

---

# 🔐 Current Identity Approach

The current version uses a simple HTTP header:

```text
X-USER-ID: 1
```

This is intentionally kept simple for the current implementation.

Authentication and authorization using **Spring Security + JWT** are planned improvements.

---

# 💳 Transactional Checkout

Checkout is implemented using Spring's `@Transactional`.

```java
@Transactional
public OrderResponse placeOrder(Long userId) {
    // get cart
    // calculate total
    // create order
    // create order items
    // save order
    // clear cart
}
```

The goal is to ensure that the order creation and cart clearing participate in the same transaction.

---

# 🔎 Product Search

The Product Service supports keyword-based searching.

Example:

```http
GET /api/products/search?keyword=Laptop
```

The search checks:

* Product name
* Product description
* Active products
* Products with stock greater than zero

Example JPQL:

```java
@Query("""
    SELECT p FROM Product p
    WHERE p.active = true
    AND p.stockquantity > 0
    AND (
        LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
        OR LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%'))
    )
""")
List<Product> search(@Param("keyword") String keyword);
```

---

# 🗑️ Soft Delete

Instead of permanently deleting products:

```java
product.setActive(false);
```

The database record remains available.

```text
Before:

Product
active = true

        │
        │ DELETE
        ▼

After:

Product
active = false
```

This helps preserve historical product information.

---

# 🐳 Docker Infrastructure

Docker Compose is used for PostgreSQL and pgAdmin.

```text
Docker Compose
│
├── PostgreSQL 14
│   ├── userdb
│   ├── productdb
│   └── orderdb
│
└── pgAdmin 4
```

Start infrastructure:

```bash
docker compose up -d
```

Check running containers:

```bash
docker ps
```

Stop containers:

```bash
docker compose down
```

---

# 📚 Swagger API Documentation

Each service provides Swagger UI.

### User Service

```text
http://localhost:8081/swagger-ui/index.html
```

### Product Service

```text
http://localhost:8082/swagger-ui/index.html
```

### Order Service

```text
http://localhost:8083/swagger-ui/index.html
```

Swagger can be used to explore and test the REST APIs.

---

# 📮 Postman Testing

The APIs were manually tested using Postman.

The testing flow follows the complete E-Commerce workflow.

### Test Flow

|  # | Test             | Endpoint                   | Expected Result     |
| -: | ---------------- | -------------------------- | ------------------- |
|  1 | Register user    | `POST /api/users`          | User created        |
|  2 | Get user         | `GET /api/users/1`         | User + address      |
|  3 | Update user      | `PUT /api/users/1`         | User updated        |
|  4 | Create product   | `POST /api/products`       | Product created     |
|  5 | Search product   | `GET /api/products/search` | Matching products   |
|  6 | Soft delete      | `DELETE /api/products/1`   | Product deactivated |
|  7 | Add to cart      | `POST /api/cart/add`       | Cart item created   |
|  8 | Add same product | `POST /api/cart/add`       | Quantity increased  |
|  9 | View cart        | `GET /api/cart`            | Cart items returned |
| 10 | Place order      | `POST /api/orders`         | Order confirmed     |
| 11 | Verify cart      | `GET /api/cart`            | Cart cleared        |

---

# 📸 Postman Screenshots

Postman screenshots are included to demonstrate API testing and the end-to-end workflow.

## User Service

### Register User

![Register User](screenshots/user-service.png)

---

## Product Service

### Product API

![Product Service](screenshots/product-service.png)

---

## Cart API

### Add / View Cart

![Cart API](screenshots/cart.png)

---

## Order API

### Place Order

![Place Order](screenshots/order.png)

---

## End-to-End Testing

![Postman Testing Flow](screenshots/postman-flow.png)

> Replace the screenshot filenames above with the exact names of your uploaded images.

---

# 🧪 Testing Tools

### Postman

Used for:

* REST API testing
* Request/response validation
* Header testing
* End-to-end API workflow
* Cart and checkout testing

### Swagger

Used for:

* API documentation
* Endpoint exploration
* Request testing

---

# ⚙️ Configuration

Each service has its own Spring Boot configuration.

Example:

```yaml
spring:
  application:
    name: product-service

  datasource:
    url: jdbc:postgresql://localhost:5432/productdb
    username: postgres
    password: postgres

  jpa:
    database: PostgreSQL
    show-sql: true
    hibernate:
      ddl-auto: update

server:
  port: 8082
```

---

# 🛠️ How to Run the Project

## 1. Clone the repository

```bash
git clone YOUR_GITHUB_REPOSITORY_URL
```

```bash
cd E-Commerce-Microservices-Backend
```

---

## 2. Start PostgreSQL and pgAdmin

```bash
docker compose up -d
```

Verify:

```bash
docker ps
```

---

## 3. Start User Service

```bash
cd user-service
```

```bash
./mvnw spring-boot:run
```

Windows:

```bash
mvnw.cmd spring-boot:run
```

Runs on:

```text
http://localhost:8081
```

---

## 4. Start Product Service

```bash
cd product-service
```

```bash
mvnw.cmd spring-boot:run
```

Runs on:

```text
http://localhost:8082
```

---

## 5. Start Order Service

```bash
cd order-service
```

```bash
mvnw.cmd spring-boot:run
```

Runs on:

```text
http://localhost:8083
```

---

# 📊 Current Project Status

| Module                      | Status      |
| --------------------------- | ----------- |
| Requirement & Architecture  | ✅ Completed |
| User Service                | ✅ Completed |
| Product Service             | ✅ Completed |
| Order Service               | ✅ Completed |
| Cart Management             | ✅ Completed |
| Transactional Checkout      | ✅ Completed |
| PostgreSQL                  | ✅ Completed |
| Docker Compose              | ✅ Completed |
| Swagger/OpenAPI             | ✅ Completed |
| Postman Testing             | ✅ Completed |
| Database-per-Service        | ✅ Completed |
| Inter-Service Communication | 🔄 Planned  |
| JWT Authentication          | 🔄 Planned  |
| API Gateway                 | 🔄 Planned  |
| Service Discovery           | 🔄 Planned  |
| RabbitMQ / Kafka            | 🔄 Planned  |
| Automated Testing           | 🔄 Planned  |

---

# 🔮 Future Improvements

The following features are planned for future versions:

### 1. Inter-Service Communication

Use:

```text
OpenFeign / WebClient
```

to communicate between services.

For example:

```text
Order Service
      │
      ├──► Product Service
      │       └── Verify price & stock
      │
      └──► User Service
              └── Verify user
```

### 2. Stock Management

Add:

* Stock validation
* Stock deduction during checkout
* Out-of-stock handling

### 3. Authentication & Authorization

Implement:

```text
Spring Security
       +
JWT
       +
Role-Based Access Control
```

### 4. API Gateway

Introduce:

```text
Client
  │
  ▼
API Gateway
  │
  ├── User Service
  ├── Product Service
  └── Order Service
```

### 5. Service Discovery

Use:

```text
Eureka Server
```

for service registration and discovery.

### 6. Event-Driven Architecture

Introduce:

```text
RabbitMQ / Kafka
```

for asynchronous events such as:

```text
OrderPlacedEvent
        │
        ├──► Inventory
        ├──► Notification
        └──► Payment
```

### 7. Automated Testing

Add:

* JUnit
* Mockito
* Spring Boot Test
* Testcontainers

---

# 🎯 Key Learning Outcomes

Through this project, I practiced:

* Building REST APIs using Spring Boot
* Designing Microservices
* Database-per-Service architecture
* Spring Data JPA
* PostgreSQL
* DTO-based API design
* Transaction management
* JPQL queries
* Soft delete implementation
* Docker Compose
* Swagger/OpenAPI
* Postman API testing
* Maven project management
* Git/GitHub workflow
* Layered backend architecture

---

# 👨‍💻 Author

**Vijay Khetre**

Java Backend Developer | Spring Boot | Microservices | PostgreSQL | Docker

### Profiles

* GitHub: `https://github.com/vkfullstack`
* LeetCode: `https://leetcode.com/u/vkhetre69/`

---

# ⭐ Project Highlights

```text
Java 25
Spring Boot 4.1.1
3 Microservices
3 PostgreSQL Databases
Docker Compose
REST APIs
Spring Data JPA
Swagger/OpenAPI
Postman
Transactional Checkout
Soft Delete
Database-per-Service
```

If you found this project useful, consider giving the repository a ⭐.
