# User-Organization API

## 📘 Overview

This API provides endpoints for managing users and organizations, including registration, login, and user-organization association.
### Entity Relation Diagram
![Screenshot 2025-06-09 220413](https://github.com/user-attachments/assets/ddff2fea-a338-49b3-861d-0649eb4995ad)

## 🌍 Base URL

- Development: http://localhost:8080/
- Production: https://user-org-api.onrender.com/

## 📘 API Documentation

- Development: http://localhost:8080/swagger-ui/index.html
- Production: https://user-org-api.onrender.com/swagger-ui/index.html
---
## 🔐 Authentication

This API uses **Bearer Token (JWT)** for authentication.  
Include the token in the `Authorization` header for all secured endpoints:

```http
Authorization: Bearer <your-jwt-token>
```
---

## 📚 Endpoints

### 🔐 Auth

### Register a new user.
```http
POST /api/v1/auth/register
```
- Request body
```json
{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john@example.com",
  "password": "SecureP@ss123",
  "phone": "+1234567890"
}
```

- Response 
```json
{
  "status": "success",
  "message": "Successful registration",
  "data": {
    "user": {
      "firstName": "John",
      "lastName": "Doe",
      "email": "john@example.com",
      "password": "SecureP@ss123",
      "phone": "+1234567890"
    },
    "accessToken": "eyJh..."
  }
}
```

### Login an existing user.
```http
POST /api/v1/auth/login
```
- Request Body
```json
{
  "email": "john@example.com",
  "password": "SecureP@ss123"
}
```
- Response 
```json
{
  "status": "success",
  "message": "Successful signin",
  "data": {
    "user": {
      "firstName": "John",
      "lastName": "Doe",
      "email": "john@example.com",
      "password": "SecureP@ss123",
      "phone": "+1234567890"
    },
    "accessToken": "eyJh..."
  }
}
```
---
## 🏢 Organization
### Fetch a paginated list of all organizations.
```http
GET /api/v1/organizations
```

- Query Parameters:
```
page (optional, default: 0)

limit (optional, default: 10)
```

- Response

```json
{
  "status": "success",
  "message": "successfully retrieved all organizations",
  "data": {
    "organizations": [
      {
        "orgId": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
        "name": "org 1",
        "description": "org 1 description"
      }
    ],
    "page": 1,
    "limit": 10,
    "total": 2
  }
}
```

### Create a new organization.

- Request Body:
```json
{
  "name": "Organization Name",
  "description": "Optional description"
}
```
- Response
```json
{
  "status": "success",
  "message": "successfully created organization",
  "data": {
    "orgId": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
    "name": "org 1",
    "description": "org 1 description"
  }
}
```
### Get a specific organization by its ID.
```http
GET /api/v1/organizations/{id}
```
- Response
```json
{
  "status": "success",
  "message": "successfully retrieved organization",
  "data": {
    "orgId": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
    "name": "org 1",
    "description": "org 1 description"
  }
}
```

### Add a user to an organization.
```http
POST /api/v1/organizations/{id}/users
```
- Request Body:
```json
{
  "userId": "3fa85f64-5717-4562-b3fc-2c963f66afa6"
}
```
---

## 👤 Users
### Retrieve a paginated list of users.
```http
GET /api/v1/users
```
- Query Parameters:

```
page (optional, default: 0)

limit (optional, default: 10)
```

- Response 
```json
{
  "status": "success",
  "message": "successfully retrieved users",
  "data": {
    "users": [
      {
        "userId": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
        "firstName": "John",
        "lastName": "Doe",
        "email": "johndoe@example.com",
        "phone": "08033333333"
      }
    ],
    "page": 1,
    "limit": 10,
    "total": 1
  }
}

```
### Fetch a single user by their ID.
```http
GET /api/v1/users/{id}
```
- Response
```json
{
  "status": "success",
  "message": "successfully retrieved user",
  "data": {
    "userId": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
    "firstName": "John",
    "lastName": "Doe",
    "email": "johndoe@gmail.com",
    "phone": "08033333333"
  }
}
```
