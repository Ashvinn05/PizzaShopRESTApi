# 🍕 Pizza Shop REST API  

## 🚀 Overview  

A modern, **reactive REST API** for managing a pizza shop's operations. Built with **Spring Boot 3.4.4** and **Java 17**, this application provides a **scalable** solution for handling **pizza orders** and **inventory management**.  

---

## 🏁 Getting Started  

### 👋 Prerequisites
1. Java 17 installed
2. Maven installed
3. MongoDB installed and running
4. Git installed (for cloning the repository)
5. Docker installed (for Docker environments)

### 🏷️ Project Setup
1. Clone the repository:
```bash
git clone [repository-url]
cd pizzashop
```

### ▶️ Build & Run  

#### 💻 Local Development
```bash
mvn clean install
mvn spring-boot:run
```

#### 🐳 Docker Development Environment
```bash
# 1. First create the environment file from template
cp .example.env.txt .env.dev

# 2. Edit .env.dev with your development environment settings

# 3. Build and run Docker containers
docker-compose --env-file .env.dev build
docker-compose --env-file .env.dev up
```

#### 🐳 Docker Production Environment
```bash
# 1. First create the environment file from template
cp .example.env.txt .env.prod

# 2. Edit .env.prod with your production environment settings

# 3. Build and run Docker containers
docker-compose --env-file .env.prod build
docker-compose --env-file .env.prod up
```

### 📝 API Documentation  
Available at `/doc/API-Doc.md`  

---

## 🔥 Key Features  

### 1️⃣ **Reactive Architecture**  

The system uses **reactive types** (`Mono` and `Flux`) throughout the system, ensuring:  

✅ **Non-blocking database operations** – Uses Spring Data MongoDB Reactive  
✅ **Efficient handling of multiple concurrent requests** – Asynchronous processing  
✅ **Automatic backpressure handling** – Prevents memory overload  
✅ **Reduced memory footprint** – Streams data instead of loading everything into memory  
✅ **Better CPU utilization** – Optimized non-blocking operations  
✅ **Improved scalability** – Handles thousands of concurrent users with minimal resources  
✅ **Resource optimization** – Reduces thread pool size and memory usage  
✅ **Faster response times** – Non-blocking operations allow for quicker response to user requests  

💡 **Example of Reactive Pattern Usage**:  
```java
// From OrderService.java
public Flux<Order> getAllOrders() {
    return orderRepository.findAll()
        .doOnNext(order -> log.debug("Found order: {}", order))
        .switchIfEmpty(Flux.empty())
        .doOnError(e -> {
            log.error("[ERROR] getAllOrders - Error fetching orders", e);
            throw new RuntimeException("Failed to fetch orders", e);
        });
}
```

---

### 2️⃣ **Robust Error Handling**  

The application implements **comprehensive error handling mechanisms**:  

🚩 **Custom Exception Handling** – Uses `NotFoundException` for missing resources  
⚠️ **Validation Exceptions** – Throws `IllegalArgumentException` for invalid inputs  
🔄 **Runtime Exception Wrapping** – Provides meaningful messages  
🔄 **Global Exception Handler** – Centralized error handling with consistent response format  
� **Detailed Error Logging** – Comprehensive logging for debugging  
🔄 **User-friendly Error Messages** – Clear and helpful error messages for clients  

�💡 **Example of Error Handling**:  
```java
// From OrderService.java
public Mono<Order> createOrder(Order newOrder) {
    if (newOrder.getPizzas() == null || newOrder.getPizzas().isEmpty()) {
        throw new IllegalArgumentException("At least one pizza is required");
    }
    // ...
}

// Global Exception Handler
@ExceptionHandler({NotFoundException.class, IllegalArgumentException.class})
public ResponseEntity<ErrorResponse> handleException(Exception ex) {
    ErrorResponse error = new ErrorResponse(ex.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
}
```

---

### 3️⃣ **Data Validation**  

The system implements **multiple layers of validation**:  

🛡️ **Model-Level Validation** – Uses Jakarta Validation (`@NotNull`, `@NotBlank`, `@Size`)  
📝 **Service-Level Validation** – Implements custom validation logic  
💛 **Global Exception Handling** – Ensures consistent error responses  
✅ **Input Validation** – Validates all incoming requests  
✅ **Business Rule Validation** – Enforces business rules at service level  
✅ **Database Constraints** – Ensures data integrity at storage level  
✅ **Type Safety** – Uses proper data types to prevent runtime errors  

💡 **Example of Model Validation**:  
```java
@Data
@Document(collection = "orders")
public class Order {
    @NotNull(message = "List of pizzas is required")
    @Size(min = 1, message = "At least one pizza is required")
    private List<String> pizzas;
    
    @NotBlank(message = "Order status is required")
    private String status;
    
    @NotNull(message = "Timestamp is required")
    private Date timestamp;
    
    @Valid
    private Customer customer;
}

// Custom validation annotation
@Constraint(validatedBy = PizzaValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPizza {
    String message() default "Invalid pizza selection";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
```

---

## 🏢 Architecture  

This application follows a **clean, layered architecture**:  

🔹 **Controller Layer** – REST endpoints for API interactions  
🔹 **Service Layer** – Business logic and validation  
🔹 **Repository Layer** – Database operations using Spring Data MongoDB Reactive  
🔹 **Model Layer** – Data objects with validation  

---

## 📦 Dependencies  

🔹 **Spring Boot** – 3.4.4  
🔹 **Spring WebFlux** – Reactive web framework  
🔹 **Spring Data MongoDB Reactive** – MongoDB integration  
🔹 **Project Reactor** – Core reactive programming library  
🔹 **Lombok** – Reduces boilerplate code  
🔹 **JUnit 5** – Testing framework  
🔹 **Mockito** – Mocking framework  
🔹 **Spring Boot Actuator** – Monitoring and management  
🔹 **Spring Boot Validation** – Data validation  
🔹 **Spring Boot DevTools** – Development tools  

---

## 💻 Technical Stack  

📌 **Language**: Java 17  
📌 **Framework**: Spring Boot 3.4.4  
📌 **Database**: MongoDB (Reactive)  
📌 **Testing**: JUnit 5, Mockito,   
📌 **Build Tool**: Maven  
📌 **Logging**: SLF4J  
📌 **Code Generation**: Lombok  
📌 **Monitoring**: Spring Boot Actuator  
📌 **Development Tools**: Spring Boot DevTools  

---

## 🛠️ Development Tools  
🛠️ **Spring Boot DevTools** – Hot reloading and improved development experience  
📊 **Spring Boot Actuator** – Production-ready features for monitoring and managing your application  
🔍 **Actuator Endpoints**:  
- `/actuator/health` – Application health status  
- `/actuator/metrics` – Application metrics  
- `/actuator/logs` – Log management  
- `/actuator/beans` – Spring bean information  

---

## 🧪 Testing  

🛠️ **Unit Tests** – For service layer functionality  
⚡ **Reactive Testing** – Uses Reactor Test’s `StepVerifier`  
🎮 **Mocking** – Utilizes Mockito for dependency injection  


---

## ✨ Performance Optimizations  

🚀 **Reactive Streams** – Efficient memory usage via streaming  
🔄 **Non-blocking I/O** – Improved CPU utilization  
🛡️ **Automatic Backpressure** – Prevents memory overload  

---

## 🔒 Security Features  

✅ **Input Validation** – Prevents SQL injection and other attacks  
✅ **Error Handling** – Prevents information leakage  
✅ **Secure Logging** – Handles sensitive information carefully  
✅ **Data Validation** – Ensures integrity of input data  

---