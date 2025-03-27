# Pizza Shop API Documentation

## Overview

The Pizza Shop API provides a comprehensive set of endpoints for managing pizzas and orders in a pizza shop application. The API is built using Spring Boot and follows RESTful principles with reactive programming support.

## Base URL

```
http://localhost:8080
```

## Pizza Management Endpoints

### 1. Get All Pizzas

- **Endpoint**: `GET /pizzas`
- **Description**: Retrieves a list of all available pizzas
- **Response**:
  ```json
  {
    "id": "string",
    "name": "string",
    "description": "string",
    "price": number,
    "toppings": ["string"],
    "sizeOptions": ["string"]
  }
  ```
- **Example**:
  ```bash
  curl -X GET http://localhost:8080/pizzas
  ```

### 2. Get Pizza by ID

- **Endpoint**: `GET /pizzas/{id}`
- **Description**: Retrieves a specific pizza by its ID
- **Parameters**:
  - `id`: string (required)
- **Response**:
  ```json
  {
    "id": "string",
    "name": "string",
    "description": "string",
    "price": number,
    "toppings": ["string"],
    "sizeOptions": ["string"]
  }
  ```
- **Example**:
  ```bash
  curl -X GET http://localhost:8080/pizzas/1
  ```

### 3. Create New Pizza

- **Endpoint**: `POST /pizzas`
- **Description**: Creates a new pizza
- **Request Body**:
  ```json
  {
    "name": "string",
    "description": "string",
    "price": number,
    "toppings": ["string"],
    "sizeOptions": ["string"]
  }
  ```
- **Example**:
  ```bash
  curl -X POST http://localhost:8080/pizzas \
    -H "Content-Type: application/json" \
    -d '{
      "name": "Margherita",
      "description": "Classic pizza with tomato sauce and mozzarella",
      "price": 12.99,
      "toppings": ["tomato sauce", "mozzarella"],
      "sizeOptions": ["small", "medium", "large"]
    }'
  ```

## Order Management Endpoints

### 1. Get All Orders

- **Endpoint**: `GET /orders`
- **Description**: Retrieves a list of all orders
- **Response**:
  ```json
  {
    "id": "string",
    "status": "string",
    "timestamp": "string",
    "pizzas": [
      {
        "id": "string",
        "quantity": number,
        "size": "string"
      }
    ]
  }
  ```
- **Example**:
  ```bash
  curl -X GET http://localhost:8080/orders
  ```

### 2. Get Orders by Status

- **Endpoint**: `GET /orders/status/{status}`
- **Description**: Retrieves orders filtered by status
- **Parameters**:
  - `status`: string (required) - Possible values: "pending", "preparing", "ready", "delivered", "cancelled"
- **Response**:
  ```json
  {
    "id": "string",
    "status": "string",
    "timestamp": "string",
    "pizzas": [
      {
        "id": "string",
        "quantity": number,
        "size": "string"
      }
    ]
  }
  ```
- **Example**:
  ```bash
  curl -X GET http://localhost:8080/orders/status/pending
  ```

### 3. Get Order by ID

- **Endpoint**: `GET /orders/{id}`
- **Description**: Retrieves a specific order by its ID
- **Parameters**:
  - `id`: string (required)
- **Response**:
  ```json
  {
    "id": "string",
    "status": "string",
    "timestamp": "string",
    "pizzas": [
      {
        "id": "string",
        "quantity": number,
        "size": "string"
      }
    ]
  }
  ```
- **Example**:
  ```bash
  curl -X GET http://localhost:8080/orders/1
  ```

### 4. Create New Order

- **Endpoint**: `POST /orders`
- **Description**: Creates a new order
- **Request Body**:
  ```json
  {
    "status": "pending",
    "pizzas": [
      {
        "id": "string",
        "quantity": number,
        "size": "string"
      }
    ]
  }
  ```
- **Example**:
  ```bash
  curl -X POST http://localhost:8080/orders \
    -H "Content-Type: application/json" \
    -d '{
      "status": "pending",
      "pizzas": [
        {
          "id": "1",
          "quantity": 2,
          "size": "medium"
        }
      ]
    }'
  ```

## Error Responses

The API may return the following error responses:

- **400 Bad Request**: When the request body is invalid or missing required fields
- **404 Not Found**: When a requested resource (pizza or order) is not found
- **500 Internal Server Error**: When there's an unexpected error processing the request

## Response Headers

All responses include the following headers:

- `Content-Type`: `application/json`
- `X-Response-Time`: Time taken to process the request (in milliseconds)

## Notes

1. All endpoints are reactive and support streaming responses
2. Error messages are logged for debugging purposes
3. Timestamps are returned in ISO 8601 format
4. All endpoints require proper JSON formatting in request bodies