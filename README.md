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
- Docker

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
- Product create and read operations
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

### Default URL = http://localhost:8080
#### Default PORT Number for Tomcat Server is 8080

### To change Port Number
```bash
// application.yml
server:
  port: PORT_NUMBER
```

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
| page | Page number |
| size | No of content in single page |
| keyword | Product name|
| category | Category |
| minPrice | Minimum Price |
| maxPrice | Maximum Price |
| sortBy | Sorting field |
| direction | asc/desc |

| Method | Endpoint | Description |
|--------|---------|------------|
| POST | /api/products | Create product (ADMIN) |
| GET | /api/products?page=1&size=3&sortBy=field&direction=asc | Get all products |
| GET | /api/products/{productId} | Get product by product id |
| GET | /api/products/filter?name=Samsung 300W Charger&category=C Type Charger&sortBy=price&direction=asc | Get filtered product (static query) |
| GET | /api/products/specification?category=Mobile&sortBy=price&direction=desc | Get filtered product (dynamic query) |

### Carts

#### Params
| Param  | Description |
|--------|------------|
| productId | Product id |
| quantity | No of items |

| Method | Endpoint | Description |
|--------|---------|------------|
| POST | /api/carts/add?productId=3&quantity=2 | Add to cart |
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
spring:
  jdbc: mysql://localhost:3306/commerce
  datasource:
    username: root
    password: your_password
  jpa:
    hibernate:
      ddl-auto: update

  security:
    oauth2:
      client:
        registration:
          google:
            client-id: GOOGLE CLIENT ID
            client-secret: GOOGLE CLIENT SECRET
```

### 3. Run docker compose
```bash
docker-compose up -d
```

### 4. Run application
```bash
./mvnw spring-boot:run
```

## 🔮 Future Improvements

- Refresh token reuse detection
- Rate limiting
- Email verification
- Payment integration
- Microservices architecture
