# CommerceCore - E-commerce Backend API

## 🚀 Overview
CommerceCore is a production-ready backend for an e-commerce platform built using Spring Boot. 
It provides secure authentication (JWT + OAuth2), role-based authorization, cart management, order processing, and product/category management. 
This project demonstrates a monolithic backend architecture with modern security practices. 

## 🛠️ Tech Stack
- Java 17+
- Spring Boot
- Spring Security (JWT + OAuth2)
- Spring Data JPA (Hibernate)
- MySQL
- Redis (Token Blacklisting & Caching)
- Lombok

## 🔐 Features

### 1. Authentication & Security
- JWT-based authentication
- OAuth2 login (Google)
- Refresh token rotation
- Token blacklisting (Redis)
- Role-based access control (ADMIN / USER)
- Secure cookie handling

### 2. User & Roles
- User registration & login
- Role-based authorization

### 3. Product & Category
- Product CRUD operations
- Category management
- Filtering & specifications

### 4. Cart & Orders
- Add/remove items from cart
- Place order from cart
- Stock validation
- Order history

### 5. Advanced Security Features
- Logout from single device
- Logout from all devices
- Token ownership validation
- Refresh token reuse protection (optional enhancement)

## 📁 Project Structure

```bash
com.CommerceCore
├── config/            # Security & application configuration
├── controller/        # REST API endpoints
├── dto/               # Data Transfer Objects
├── entity/            # Database entities   
├── exception/         # Global exception handling
├── repository/        # Data access layer (JPA Repositories)
├── security/          # JWT, filters, authentication logic, oauth2
├── service/           # Business logic layer
├── specification/     # Product specification (filter product)
└── util/              # Common utilities (cookies, helpers)
```

## 🔑 API Endpoints

### Auth

| Method | Endpoint | Description |
|--------|---------|------------|
| POST | /api/auth/create | Register user |
| POST | /api/auth/login | Login |
| POST | /api/auth/refresh | Refresh access token |
| POST | /api/auth/logout | Logout |
| POST | /api/auth/logout-all | Logout from all devices |

### Users

| Method | Endpoint | Description |
|--------|---------|------------|
| GET | /api/users | Get user by user id |

### Categories

| Method | Endpoint | Description |
|--------|---------|------------|
| GET | /api/categories | Get all categories |
| POST | /api/categories | Create categories (ADMIN) |

### Products

#### Params 
| Param  | Description |
|--------|------------|
| keyword | Product name|
| category | Category |
| minPrice | Minimum Price |
| maxPrice | Maximum Price |
| sortBy | Sorting field |
| direction | asc/desc |

| Method | Endpoint | Description |
|--------|---------|------------|
| POST | /api/products | Create product (ADMIN) |
| GET | /api/products?page=page_no&size=number&sortBy=field&direction=asc | Get all products |
| GET | /api/products/{productId} | Get product by product id |
| GET | /api/products/filter?name=Samsung 300W Charger&category=C Type Charger&sortBy=price&direction=asc | Get filtered product (static query) |
| GET | /api/products/specification?category=Mobile&sortBy=price&direction=desc | Get filtered product (dynamic query) |

### Carts

| Method | Endpoint | Description |
|--------|---------|------------|
| POST | /api/carts/add?productId=id&quantity=number | Add to cart |
| GET | /api/carts | Get cart by user id |

### Orders

| Method | Endpoint | Description |
|--------|---------|------------|
| POST | /api/orders | Place order |
| GET | /api/orders | Get user orders |

## 🔄 Authentication Flow

1. User logs in → receives Access Token + Refresh Token (cookie)
2. Access token used for API requests
3. When expired → use refresh token to get new access token
4. Refresh token rotation ensures security
5. Logout revokes refresh token and blacklists access token

## ⚙️ Setup Instructions

### 1. Clone repo
```bash
git clone https://github.com/Sunanda01/CommerceCore.git
cd commerce-core
```

### 2. Configure application.yaml
```bash
spring.datasource.url=jdbc:mysql://localhost:3306/commerce
spring.datasource.username=root
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update

spring.security.oauth2.client.registration.google.client-id=GOOGLE CLIENT ID
spring.security.oauth2.client.registration.google.client-secret=GOOGLE CLIENT SECRET
```

### 3. Run application
```bash
./mvnw spring-boot:run
```

## 🔮 Future Improvements

- Refresh token reuse detection
- Logout from all devices
- Rate limiting
- Email verification
- Payment integration
- Microservices architecture
