# Pizza Shop API Documentation

## Overview

The Pizza Shop API provides a comprehensive set of endpoints for managing pizzas and orders in a pizza shop application. The API is built using Spring Boot and follows RESTful principles with reactive programming support.

## Base URL

```bash
http://localhost:8080
```

## Pizza Management Endpoints

### 1. Get All Pizzas

- **Endpoint**: `GET /pizzas`
- **Description**: Retrieves a list of all available pizzas
- **Response**:
  ```json
  {
    "status": "success",
    "message": "Pizzas retrieved successfully",
    "data": [
      {
        "id": "6423e15d5f8b9a0b5c6a3e15",
        "name": "Margherita",
        "description": "Classic pizza with tomato sauce and fresh mozzarella",
        "price": 12.99,
        "toppings": ["tomato sauce", "fresh mozzarella"],
        "sizeOptions": ["small", "medium", "large"]
      }
    ]
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
  - `id`: string (required) - MongoDB ObjectId
- **Response**:
  ```json
  {
    "status": "success",
    "message": "Pizza retrieved successfully",
    "data": {
      "id": "6423e15d5f8b9a0b5c6a3e15",
      "name": "Margherita",
      "description": "Classic pizza with tomato sauce and fresh mozzarella",
      "price": 12.99,
      "toppings": ["tomato sauce", "fresh mozzarella"],
      "sizeOptions": ["small", "medium", "large"]
    }
  }
  ```
- **Example**:
  ```bash
  curl -X GET http://localhost:8080/pizzas/6423e15d5f8b9a0b5c6a3e15
  ```

### 3. Create New Pizza

- **Endpoint**: `POST /pizzas`
- **Description**: Creates a new pizza
- **Request Body**:
  ```json
  {
    "name": "Pepperoni",
    "description": "Classic pizza with tomato sauce, fresh mozzarella, and pepperoni",
    "price": 14.99,
    "toppings": ["tomato sauce", "fresh mozzarella", "pepperoni"],
    "sizeOptions": ["small", "medium", "large"]
  }
  ```
- **Example**:
  ```bash
  curl -X POST http://localhost:8080/pizzas \
    -H "Content-Type: application/json" \
    -d '{
      "name": "Pepperoni",
      "description": "Classic pizza with tomato sauce, fresh mozzarella, and pepperoni",
      "price": 14.99,
      "toppings": ["tomato sauce", "fresh mozzarella", "pepperoni"],
      "sizeOptions": ["small", "medium", "large"]
    }'
  ```

### 4. Update Pizza

- **Endpoint**: `PUT /pizzas/{id}`
- **Description**: Updates an existing pizza
- **Parameters**:
  - `id`: string (required) - MongoDB ObjectId
- **Request Body**:
  ```json
  {
    "name": "Pepperoni",
    "description": "Classic pizza with tomato sauce, fresh mozzarella, and pepperoni",
    "price": 14.99,
    "toppings": ["tomato sauce", "fresh mozzarella", "pepperoni"],
    "sizeOptions": ["small", "medium", "large"]
  }
  ```
- **Example**:
  ```bash
  curl -X PUT http://localhost:8080/pizzas/6423e15d5f8b9a0b5c6a3e15 \
    -H "Content-Type: application/json" \
    -d '{
      "name": "Pepperoni",
      "description": "Classic pizza with tomato sauce, fresh mozzarella, and pepperoni",
      "price": 14.99,
      "toppings": ["tomato sauce", "fresh mozzarella", "pepperoni"],
      "sizeOptions": ["small", "medium", "large"]
    }'
  ```

### 5. Delete Pizza

- **Endpoint**: `DELETE /pizzas/{id}`
- **Description**: Deletes a pizza by its ID
- **Parameters**:
  - `id`: string (required) - MongoDB ObjectId
- **Response**:
  ```json
  {
    "status": "success",
    "message": "Pizza deleted successfully"
  }
  ```
- **Example**:
  ```bash
  curl -X DELETE http://localhost:8080/pizzas/6423e15d5f8b9a0b5c6a3e15
  ```

## Order Management Endpoints

### 1. Get All Orders

- **Endpoint**: `GET /orders`
- **Description**: Retrieves a list of all orders
- **Response**:
  ```json
  {
    "status": "success",
    "message": "Orders retrieved successfully",
    "data": [
      {
        "id": "6423e15d5f8b9a0b5c6a3e16",
        "status": "pending",
        "timestamp": "2025-03-28T16:23:03.000+05:30",
        "pizzas": ["6423e15d5f8b9a0b5c6a3e15"],
        "customerName": "John Doe",
        "customerEmail": "john.doe@example.com",
        "customerPhone": "+12345678901",
        "additionalAttributes": {
          "deliveryInstructions": "Leave at front door",
          "specialRequests": "Extra cheese"
        }
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
    "status": "success",
    "message": "Orders retrieved successfully",
    "data": [
      {
        "id": "6423e15d5f8b9a0b5c6a3e16",
        "status": "pending",
        "timestamp": "2025-03-28T16:23:03.000+05:30",
        "pizzas": ["6423e15d5f8b9a0b5c6a3e15"],
        "customerName": "John Doe",
        "customerEmail": "john.doe@example.com",
        "customerPhone": "+12345678901",
        "additionalAttributes": {
          "deliveryInstructions": "Leave at front door",
          "specialRequests": "Extra cheese"
        }
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
  - `id`: string (required) - MongoDB ObjectId
- **Response**:
  ```json
  {
    "status": "success",
    "message": "Order retrieved successfully",
    "data": {
      "id": "6423e15d5f8b9a0b5c6a3e16",
      "status": "pending",
      "timestamp": "2025-03-28T16:23:03.000+05:30",
      "pizzas": ["6423e15d5f8b9a0b5c6a3e15"],
      "customerName": "John Doe",
      "customerEmail": "john.doe@example.com",
      "customerPhone": "+12345678901",
      "additionalAttributes": {
        "deliveryInstructions": "Leave at front door",
        "specialRequests": "Extra cheese"
      }
    }
  }
  ```
- **Example**:
  ```bash
  curl -X GET http://localhost:8080/orders/6423e15d5f8b9a0b5c6a3e16
  ```

### 4. Create New Order

- **Endpoint**: `POST /orders`
- **Description**: Creates a new order
- **Request Body**:
  ```json
  {
    "pizzas": ["6423e15d5f8b9a0b5c6a3e15"],
    "status": "pending",
    "timestamp": "2025-03-29T13:33:35.000+05:30",
    "customerName": "John Doe",
    "customerEmail": "john.doe@example.com",
    "customerPhone": "+12345678901",
    "additionalAttributes": {
      "deliveryInstructions": "Leave at front door",
      "specialRequests": "Extra cheese"
    }
  }
  ```
- **Validation Rules**:
  - `pizzas`: Required, must contain at least one valid MongoDB ObjectId (24 hex characters)
  - `status`: Required, must be one of: "pending", "preparing", "ready", "delivered", "cancelled"
  - `timestamp`: Required
  - `customerName`: Optional, maximum length 100 characters
  - `customerEmail`: Optional, must be a valid email format
  - `customerPhone`: Optional, must be 10-15 digits, optionally starting with +
  - `additionalAttributes`: Optional, values can be any text
- **Response**:
  ```json
  {
    "status": "success",
    "message": "Order created successfully",
    "data": {
      "id": "6423e15d5f8b9a0b5c6a3e16",
      "status": "pending",
      "timestamp": "2025-03-29T13:33:35.000+05:30",
      "pizzas": ["6423e15d5f8b9a0b5c6a3e15"],
      "customerName": "John Doe",
      "customerEmail": "john.doe@example.com",
      "customerPhone": "+12345678901",
      "additionalAttributes": {
        "deliveryInstructions": "Leave at front door",
        "specialRequests": "Extra cheese"
      }
    }
  }
  ```
- **Example**:
  ```bash
  curl -X POST http://localhost:8080/orders \
    -H "Content-Type: application/json" \
    -d '{
      "pizzas": ["6423e15d5f8b9a0b5c6a3e15"],
      "status": "pending",
      "timestamp": "2025-03-29T13:33:35.000+05:30",
      "customerName": "John Doe",
      "customerEmail": "john.doe@example.com",
      "customerPhone": "+12345678901",
      "additionalAttributes": {
        "deliveryInstructions": "Leave at front door",
        "specialRequests": "Extra cheese"
      }
    }'
  ```

### 5. Update Order Status

- **Endpoint**: `PUT /orders/{id}/status`
- **Description**: Updates the status of an existing order
- **Parameters**:
  - `id`: string (required) - MongoDB ObjectId
- **Request Body**:
  ```json
  {
    "status": "preparing"
  }
  ```
- **Validation Rules**:
  - Status must be one of: "pending", "preparing", "ready", "delivered", "cancelled"
  - Status transitions must follow this order: pending -> preparing -> ready -> delivered/cancelled
- **Response**:
  ```json
  {
    "status": "success",
    "message": "Order status updated successfully",
    "data": {
      "id": "6423e15d5f8b9a0b5c6a3e16",
      "status": "preparing",
      "timestamp": "2025-03-29T13:33:35.000+05:30",
      "pizzas": ["6423e15d5f8b9a0b5c6a3e15"],
      "customerName": "John Doe",
      "customerEmail": "john.doe@example.com",
      "customerPhone": "+12345678901",
      "additionalAttributes": {
        "deliveryInstructions": "Leave at front door",
        "specialRequests": "Extra cheese"
      }
    }
  }
  ```
- **Example**:
  ```bash
  curl -X PUT http://localhost:8080/orders/6423e15d5f8b9a0b5c6a3e16/status \
    -H "Content-Type: application/json" \
    -d '{
      "status": "preparing"
    }'
  ```

### 6. Cancel Order

- **Endpoint**: `DELETE /orders/{id}`
- **Description**: Cancels an existing order
- **Parameters**:
  - `id`: string (required) - MongoDB ObjectId
- **Response**:
  ```json
  {
    "status": "success",
    "message": "Order cancelled successfully"
  }
  ```
- **Example**:
  ```bash
  curl -X DELETE http://localhost:8080/orders/6423e15d5f8b9a0b5c6a3e16
  ```

## Error Responses

The API may return the following error responses:

### 400 Bad Request
- **Invalid ID Format**:
  ```json
  {
    "status": "error",
    "message": "Pizza ID must be a valid MongoDB ObjectId"
  }
  ```
- **Missing Required Fields**:
  ```json
  {
    "status": "error",
    "message": "List of pizzas is required"
  }
  ```
- **Invalid Status**:
  ```json
  {
    "status": "error",
    "message": "Status must be one of: pending, preparing, ready, delivered, cancelled"
  }
  ```
- **Invalid Phone Number**:
  ```json
  {
    "status": "error",
    "message": "Phone number must be 10-15 digits, optionally starting with +"
  }
  ```
- **Invalid Email**:
  ```json
  {
    "status": "error",
    "message": "Invalid email format"
  }
  ```
- **Invalid Additional Attributes**:
  ```json
  {
    "status": "error",
    "message": "Additional attribute values must be text"
  }
  ```

### 404 Not Found
- **Pizza Not Found**:
  ```json
  {
    "status": "error",
    "message": "Pizza not found with id: 6423e15d5f8b9a0b5c6a3e15"
  }
  ```
- **Order Not Found**:
  ```json
  {
    "status": "error",
    "message": "Order not found with id: 6423e15d5f8b9a0b5c6a3e16"
  }
  ```

{{ ... }}