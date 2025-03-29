# Pizza Shop Application Test Cases

## Pizza Service Test Cases

### 1. Pizza Retrieval Tests

- **shouldReturnAllPizzasWhenAvailable**
  - **Description**: Tests successful retrieval of all pizzas
  - **Scenario**: Returns a Flux with one pizza when data exists
  - **Expected**: StepVerifier verifies 1 pizza is returned

- **shouldReturnEmptyFluxWhenNoPizzasAvailable**
  - **Description**: Tests empty pizza list retrieval
  - **Scenario**: Returns empty Flux when no pizzas exist
  - **Expected**: StepVerifier verifies completion with no items

- **shouldThrowExceptionWhenErrorFetchingPizzas**
  - **Description**: Tests error handling during pizza retrieval
  - **Scenario**: Simulates repository error
  - **Expected**: StepVerifier verifies RuntimeException is thrown

- **shouldReturnPizzaWhenIdExists**
  - **Description**: Tests successful pizza retrieval by ID
  - **Scenario**: Returns pizza when valid ID is provided
  - **Expected**: StepVerifier verifies pizza is returned

- **shouldThrowNotFoundExceptionWhenIdDoesNotExist**
  - **Description**: Tests error handling for non-existent pizza
  - **Scenario**: Attempts to retrieve non-existent pizza
  - **Expected**: StepVerifier verifies NotFoundException is thrown

### 2. Pizza Creation Tests

- **shouldCreateNewPizza**
  - **Description**: Tests successful pizza creation
  - **Scenario**: Creates new pizza with valid name, price, toppings, and size options
  - **Expected**: StepVerifier verifies pizza is created with correct attributes

- **shouldThrowExceptionWhenCreatingDuplicatePizza**
  - **Description**: Tests duplicate pizza prevention
  - **Scenario**: Attempts to create pizza with existing name
  - **Expected**: StepVerifier verifies IllegalArgumentException is thrown

- **shouldThrowExceptionWhenCreatingPizzaWithInvalidPrice**
  - **Description**: Tests price validation
  - **Scenario**: Attempts to create pizza with invalid price (negative or non-numeric)
  - **Expected**: StepVerifier verifies IllegalArgumentException is thrown

- **shouldThrowExceptionWhenCreatingPizzaWithInvalidToppings**
  - **Description**: Tests toppings validation
  - **Scenario**: Attempts to create pizza with non-array toppings
  - **Expected**: StepVerifier verifies IllegalArgumentException is thrown

- **shouldThrowExceptionWhenCreatingPizzaWithInvalidSizeOptions**
  - **Description**: Tests size options validation
  - **Scenario**: Attempts to create pizza with invalid size options
  - **Expected**: StepVerifier verifies IllegalArgumentException is thrown

### 3. Pizza Update Tests

- **shouldUpdateExistingPizza**
  - **Description**: Tests successful pizza update
  - **Scenario**: Updates existing pizza's details with valid attributes
  - **Expected**: StepVerifier verifies updated pizza is returned

- **shouldThrowNotFoundExceptionWhenUpdatingNonExistingPizza**
  - **Description**: Tests error handling for non-existent pizza update
  - **Scenario**: Attempts to update non-existent pizza
  - **Expected**: StepVerifier verifies NotFoundException is thrown

- **shouldThrowExceptionWhenUpdatingPizzaWithInvalidAttributes**
  - **Description**: Tests validation during pizza update
  - **Scenario**: Attempts to update pizza with invalid attributes
  - **Expected**: StepVerifier verifies IllegalArgumentException is thrown

### 4. Pizza Deletion Tests

- **shouldDeleteExistingPizza**
  - **Description**: Tests successful pizza deletion
  - **Scenario**: Deletes existing pizza
  - **Expected**: StepVerifier verifies deletion is complete

- **shouldThrowNotFoundExceptionWhenDeletingNonExistingPizza**
  - **Description**: Tests error handling for non-existent pizza deletion
  - **Scenario**: Attempts to delete non-existent pizza
  - **Expected**: StepVerifier verifies NotFoundException is thrown

## Order Service Test Cases

### 1. Order Retrieval Tests

- **shouldReturnAllOrdersWhenAvailable**
  - **Description**: Tests successful retrieval of all orders
  - **Scenario**: Returns a Flux with one order when data exists
  - **Expected**: StepVerifier verifies 1 order is returned

- **shouldReturnEmptyFluxWhenNoOrdersAvailable**
  - **Description**: Tests empty order list retrieval
  - **Scenario**: Returns empty Flux when no orders exist
  - **Expected**: StepVerifier verifies completion with no items

- **shouldThrowExceptionWhenErrorFetchingOrders**
  - **Description**: Tests error handling during order retrieval
  - **Scenario**: Simulates repository error
  - **Expected**: StepVerifier verifies RuntimeException is thrown

- **shouldReturnOrdersByStatusWhenAvailable**
  - **Description**: Tests successful order retrieval by status
  - **Scenario**: Returns orders with matching status
  - **Expected**: StepVerifier verifies orders are returned

- **shouldReturnEmptyFluxWhenNoOrdersByStatus**
  - **Description**: Tests empty result for status-based retrieval
  - **Scenario**: Returns empty Flux when no orders match status
  - **Expected**: StepVerifier verifies completion with no items

- **shouldReturnOrderWhenIdExists**
  - **Description**: Tests successful order retrieval by ID
  - **Scenario**: Returns order when valid ID is provided
  - **Expected**: StepVerifier verifies order is returned

- **shouldThrowNotFoundExceptionWhenIdDoesNotExist**
  - **Description**: Tests error handling for non-existent order
  - **Scenario**: Attempts to retrieve non-existent order
  - **Expected**: StepVerifier verifies NotFoundException is thrown

### 2. Order Creation Tests

- **shouldCreateNewOrder**
  - **Description**: Tests successful order creation
  - **Scenario**: Creates new order with valid pizzas and customer details
  - **Expected**: StepVerifier verifies order is created with correct attributes

- **shouldThrowExceptionWhenCreatingOrderWithNoPizzas**
  - **Description**: Tests validation for order creation
  - **Scenario**: Attempts to create order without pizzas
  - **Expected**: AssertThrows verifies IllegalArgumentException is thrown

- **shouldThrowExceptionWhenCreatingOrderWithInvalidPizzas**
  - **Description**: Tests pizza ID validation
  - **Scenario**: Attempts to create order with invalid pizza IDs
  - **Expected**: StepVerifier verifies IllegalArgumentException is thrown

- **shouldThrowExceptionWhenCreatingOrderWithInvalidCustomerDetails**
  - **Description**: Tests customer details validation
  - **Scenario**: Attempts to create order with invalid customer details
  - **Expected**: StepVerifier verifies IllegalArgumentException is thrown

### 3. Order Update Tests

- **shouldUpdateOrderStatus**
  - **Description**: Tests successful order status update
  - **Scenario**: Updates existing order's status following valid transition
  - **Expected**: StepVerifier verifies status is updated

- **shouldThrowNotFoundExceptionWhenUpdatingNonExistingOrder**
  - **Description**: Tests error handling for non-existent order update
  - **Scenario**: Attempts to update non-existent order
  - **Expected**: StepVerifier verifies NotFoundException is thrown

- **shouldThrowExceptionWhenUpdatingOrderWithInvalidStatusTransition**
  - **Description**: Tests status transition validation
  - **Scenario**: Attempts to update order status with invalid transition
  - **Expected**: StepVerifier verifies IllegalArgumentException is thrown

- **shouldThrowExceptionWhenUpdatingOrderWithInvalidStatus**
  - **Description**: Tests status validation
  - **Scenario**: Attempts to update order with invalid status value
  - **Expected**: StepVerifier verifies IllegalArgumentException is thrown

### 4. Order Cancellation Tests

- **shouldCancelExistingOrder**
  - **Description**: Tests successful order cancellation
  - **Scenario**: Cancels existing order
  - **Expected**: StepVerifier verifies cancellation is complete

- **shouldThrowNotFoundExceptionWhenCancellingNonExistingOrder**
  - **Description**: Tests error handling for non-existent order cancellation
  - **Scenario**: Attempts to cancel non-existent order
  - **Expected**: StepVerifier verifies NotFoundException is thrown

## Testing Framework

- **Testing Framework**: JUnit 5
- **Mocking Framework**: Mockito
- **Reactive Testing**: Reactor Test (StepVerifier)
- **Test Type**: Unit Tests

## Key Testing Concepts

1. **Mocking**: Uses @Mock and @InjectMocks for dependency injection
2. **Test Setup**: Uses @BeforeEach for common test setup
3. **Assertions**: Uses StepVerifier for reactive streams
4. **Error Handling**: Tests exception handling for various scenarios
5. **Validation**: Tests business rules and constraints
6. **Status Transitions**: Tests valid and invalid status transitions
7. **Reactive Streams**: Testing Flux and Mono operations

## Coverage Areas

1. **Positive Scenarios**: Successful operations
2. **Negative Scenarios**: Error handling and validation
3. **Edge Cases**: Empty lists, non-existent IDs
4. **Reactive Streams**: Testing Flux and Mono operations
5. **Business Rules**: Validation of business constraints
6. **Status Transitions**: Valid and invalid status transitions
7. **Validation Rules**: Tests for all input validation rules

## Additional Test Cases

### Pizza Validation Tests

- **shouldValidatePizzaName**
  - **Description**: Tests pizza name validation
  - **Scenario**: Attempts to create pizza with invalid name
  - **Expected**: StepVerifier verifies IllegalArgumentException is thrown

- **shouldValidatePizzaPrice**
  - **Description**: Tests pizza price validation
  - **Scenario**: Attempts to create pizza with invalid price
  - **Expected**: StepVerifier verifies IllegalArgumentException is thrown

- **shouldValidatePizzaToppings**
  - **Description**: Tests pizza toppings validation
  - **Scenario**: Attempts to create pizza with invalid toppings
  - **Expected**: StepVerifier verifies IllegalArgumentException is thrown

### Order Validation Tests

- **shouldValidateOrderPizzas**
  - **Description**: Tests order pizza validation
  - **Scenario**: Attempts to create order with invalid pizzas
  - **Expected**: StepVerifier verifies IllegalArgumentException is thrown

- **shouldValidateOrderStatus**
  - **Description**: Tests order status validation
  - **Scenario**: Attempts to create order with invalid status
  - **Expected**: StepVerifier verifies IllegalArgumentException is thrown

- **shouldValidateCustomerDetails**
  - **Description**: Tests customer details validation
  - **Scenario**: Attempts to create order with invalid customer details
  - **Expected**: StepVerifier verifies IllegalArgumentException is thrown