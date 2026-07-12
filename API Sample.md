# API Sample

### 1. User

```java
public enum Role {
    ADMIN,
    SELLER,
    CUSTOMER
}
```

- Create:
```text
"role": "ROLE",
"username": "USERNAME",
"password": "PASSWORD"
```

- Update:
```json
{
  "id": 1,
  "role": "ROLE",
  "username": "USERNAME",
  "password": "PASSWORD"
}
```

- Response:
```json
{
  "id": 1,
  "username": "USERNAME",
  "role": "ROLE"
}
```

### 2. Product

- Create
```json
{
  "name": "NAME",
  "description": "DESCRIPTION",
  "quantity": 0,
  "price": 0.0
}
```

- Update:
```json
{
  "id": 1,
  "name": "NAME",
  "description": "DESCRIPTION",
  "quantity": 0,
  "price": 0.0
}
```

- Response:
```json
{
  "id": 1,
  "name": "NAME",
  "description": "DESCRIPTION",
  "quantity": 0,
  "price": 0.0,
  "sellerId": 1
}
```

### 3. Cart & CartItem

- Update:
```json
{
  "cartId": 1,
  "productId": 1,
  "quantity": 0
}
```

- Response:
```json
{
  "id": 1,
  "customerId": 1,
  "price": 0.0,
  "cartItemDtoList": [
    {
      "id": 1,
      "cartId": 1,
      "productId": 1,
      "name": "NAME",
      "quantity": 0,
      "price": 0.0
    }
  ]
}
```

### 4. Order & OrderItem

```java
public enum OrderStatus {
    PROCESSING,
    CANCELED,
    COMPLETED
}
```

- Create
```json
{
  "address": "ADDRESS",
  "creditCard": "CREDIT CARD"
}
```

- Update
```json
{
  "id": 1,
  "orderStatus": "ORDER STATUS"
}
```

- Response:
```json
{
  "id": 1,
  "sellerId": 1,
  "customerId": 2,
  "orderStartDate": "2000-01-01",
  "orderCompleteDate": "2000-01-01",
  "orderStatus": "ORDER STATUS",
  "address": "ADDRESS",
  "creditCard": "CREDIT CARD",
  "price": 0.0,
  "orderItemDtoList": [
    {
      "orderId": 1,
      "productId": 1,
      "name": "NAME",
      "quantity": 0,
      "price": 0.0
    }
  ]
}
```