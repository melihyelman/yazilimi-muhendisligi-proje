# API Endpointleri

## Auth
- **POST** `/api/auth/register`
  - Request:
    ```json
    { "username": "string", "password": "string", "role": "ADMIN" | "VENDOR" | "USER" }
    ```
  - Response:
    ```json
    { "id": 1, "username": "string", "role": "ADMIN" | "VENDOR" | "USER" }
    ```

- **POST** `/api/auth/login`
  - Request:
    ```json
    { "username": "string", "password": "string" }
    ```
  - Response:
    ```json
    { "token": "string" }
    ```

- **POST** `/api/auth/logout`
  - Request: Yok
  - Response:
    ```json
    {}
    ```

---

## Users
- **GET** `/api/users`
  - Response:
    ```json
    [
      { "id": 1, "username": "string", "role": "ADMIN" | "VENDOR" | "USER" }
    ]
    ```
- **GET** `/api/users/{id}`
  - Response:
    ```json
    { "id": 1, "username": "string", "role": "ADMIN" | "VENDOR" | "USER" }
    ```
- **GET** `/api/users/profile`
  - Response:
    ```json
    { "id": 1, "username": "string", "role": "ADMIN" | "VENDOR" | "USER" }
    ```
- **POST** `/api/users`
  - Request:
    ```json
    { "username": "string", "password": "string", "role": "ADMIN" | "VENDOR" | "USER" }
    ```
  - Response:
    ```json
    { "id": 1, "username": "string", "role": "ADMIN" | "VENDOR" | "USER" }
    ```
- **PUT** `/api/users/{id}`
  - Request:
    ```json
    { "username": "string", "password": "string", "role": "ADMIN" | "VENDOR" | "USER" }
    ```
  - Response:
    ```json
    { "id": 1, "username": "string", "role": "ADMIN" | "VENDOR" | "USER" }
    ```
- **DELETE** `/api/users/{id}`
  - Response:
    ```json
    {}
    ```

---

## Employees
- **GET** `/api/employees`
  - Response:
    ```json
    [
      {
        "id": 1,
        "name": "string",
        "department": "string",
        "username": "string",
        "role": "string",
        "assignments": [ /* EmployeeAssignment[] */ ]
      }
    ]
    ```
- **GET** `/api/employees/{id}`
  - Response:
    ```json
    {
      "id": 1,
      "name": "string",
      "department": "string",
      "username": "string",
      "role": "string",
      "assignments": [ /* EmployeeAssignment[] */ ]
    }
    ```
- **POST** `/api/employees`
  - Request:
    ```json
    { "name": "string", "department": "string", "username": "string", "role": "string", "passwordHash": "string" }
    ```
  - Response:
    ```json
    { "id": 1, "name": "string", "department": "string", "username": "string", "role": "string", "assignments": [ /* EmployeeAssignment[] */ ] }
    ```
- **PUT** `/api/employees/{id}`
  - Request:
    ```json
    { "name": "string", "department": "string", "username": "string", "role": "string", "assignments": [ /* EmployeeAssignment[] */ ] }
    ```
  - Response:
    ```json
    { "id": 1, "name": "string", "department": "string", "username": "string", "role": "string", "assignments": [ /* EmployeeAssignment[] */ ] }
    ```
- **DELETE** `/api/employees/{id}`
  - Response:
    ```json
    {}
    ```
- **POST** `/api/employees/{id}/assignVehicle`
  - Request:
    ```json
    { "vehicleId": 1, "startDate": "YYYY-MM-DD", "endDate": "YYYY-MM-DD" }
    ```
  - Response:
    ```json
    { "id": 1, "employee": { /* Employee */ }, "vehicle": { /* Vehicle */ }, "startDate": "YYYY-MM-DD", "endDate": "YYYY-MM-DD", "status": "string" }
    ```

---

## Employee Assignments
- **GET** `/api/employee-assignments`
  - Response:
    ```json
    [
      { "id": 1, "employee": { /* Employee */ }, "vehicle": { /* Vehicle */ }, "startDate": "YYYY-MM-DD", "endDate": "YYYY-MM-DD", "status": "string" }
    ]
    ```
- **GET** `/api/employee-assignments/{id}`
  - Response:
    ```json
    { "id": 1, "employee": { /* Employee */ }, "vehicle": { /* Vehicle */ }, "startDate": "YYYY-MM-DD", "endDate": "YYYY-MM-DD", "status": "string" }
    ```
- **POST** `/api/employee-assignments`
  - Request:
    ```json
    { "employee": { /* Employee */ }, "vehicle": { /* Vehicle */ }, "startDate": "YYYY-MM-DD", "endDate": "YYYY-MM-DD", "status": "string" }
    ```
  - Response:
    ```json
    { "id": 1, "employee": { /* Employee */ }, "vehicle": { /* Vehicle */ }, "startDate": "YYYY-MM-DD", "endDate": "YYYY-MM-DD", "status": "string" }
    ```
- **POST** `/api/employee-assignments/{id}/approve`
  - Response:
    ```json
    { "id": 1, "employee": { /* Employee */ }, "vehicle": { /* Vehicle */ }, "startDate": "YYYY-MM-DD", "endDate": "YYYY-MM-DD", "status": "APPROVED" }
    ```
- **POST** `/api/employee-assignments/{id}/reject`
  - Response:
    ```json
    { "id": 1, "employee": { /* Employee */ }, "vehicle": { /* Vehicle */ }, "startDate": "YYYY-MM-DD", "endDate": "YYYY-MM-DD", "status": "REJECTED" }
    ```
- **DELETE** `/api/employee-assignments/{id}`
  - Response:
    ```json
    {}
    ```

---

## Pool Assignments
- **GET** `/api/pool-assignments`
  - Response:
    ```json
    [
      { "id": 1, "vehicle": { /* Vehicle */ }, "mission": "string", "startDate": "YYYY-MM-DD", "endDate": "YYYY-MM-DD" }
    ]
    ```
- **GET** `/api/pool-assignments/{id}`
  - Response:
    ```json
    { "id": 1, "vehicle": { /* Vehicle */ }, "mission": "string", "startDate": "YYYY-MM-DD", "endDate": "YYYY-MM-DD" }
    ```
- **POST** `/api/pool-assignments`
  - Request:
    ```json
    { "vehicle": { /* Vehicle */ }, "mission": "string", "startDate": "YYYY-MM-DD", "endDate": "YYYY-MM-DD" }
    ```
  - Response:
    ```json
    { "id": 1, "vehicle": { /* Vehicle */ }, "mission": "string", "startDate": "YYYY-MM-DD", "endDate": "YYYY-MM-DD" }
    ```
- **PUT** `/api/pool-assignments/{id}`
  - Request:
    ```json
    { "mission": "string" }
    ```
  - Response:
    ```json
    { "id": 1, "vehicle": { /* Vehicle */ }, "mission": "string", "startDate": "YYYY-MM-DD", "endDate": "YYYY-MM-DD" }
    ```
- **DELETE** `/api/pool-assignments/{id}`
  - Response:
    ```json
    {}
    ```

---

## Vehicles
- **GET** `/api/vehicles`
  - Response:
    ```json
    [
      {
        "id": 1,
        "plateNumber": "string",
        "brand": "string",
        "model": "string",
        "ownership": "string",
        "leaseStartDate": "YYYY-MM-DD",
        "leaseEndDate": "YYYY-MM-DD",
        "readings": [ /* OdometerReading[] */ ],
        "expenses": [ /* Expense[] */ ],
        "assignments": [ /* Assignment[] */ ]
      }
    ]
    ```
- **GET** `/api/vehicles/{id}`
  - Response:
    ```json
    {
      "id": 1,
      "plateNumber": "string",
      "brand": "string",
      "model": "string",
      "ownership": "string",
      "leaseStartDate": "YYYY-MM-DD",
      "leaseEndDate": "YYYY-MM-DD",
      "readings": [ /* OdometerReading[] */ ],
      "expenses": [ /* Expense[] */ ],
      "assignments": [ /* Assignment[] */ ]
    }
    ```
- **POST** `/api/vehicles`
  - Request:
    ```json
    { "plateNumber": "string", "brand": "string", "model": "string", "ownership": "string", "leaseStartDate": "YYYY-MM-DD", "leaseEndDate": "YYYY-MM-DD" }
    ```
  - Response:
    ```json
    { "id": 1, "plateNumber": "string", "brand": "string", "model": "string", "ownership": "string", "leaseStartDate": "YYYY-MM-DD", "leaseEndDate": "YYYY-MM-DD", "readings": [ /* OdometerReading[] */ ], "expenses": [ /* Expense[] */ ], "assignments": [ /* Assignment[] */ ] }
    ```
- **PUT** `/api/vehicles/{id}`
  - Request:
    ```json
    { "plateNumber": "string", "brand": "string", "model": "string", "ownership": "string", "leaseStartDate": "YYYY-MM-DD", "leaseEndDate": "YYYY-MM-DD", "readings": [ /* OdometerReading[] */ ], "expenses": [ /* Expense[] */ ], "assignments": [ /* Assignment[] */ ] }
    ```
  - Response:
    ```json
    { "id": 1, "plateNumber": "string", "brand": "string", "model": "string", "ownership": "string", "leaseStartDate": "YYYY-MM-DD", "leaseEndDate": "YYYY-MM-DD", "readings": [ /* OdometerReading[] */ ], "expenses": [ /* Expense[] */ ], "assignments": [ /* Assignment[] */ ] }
    ```
- **DELETE** `/api/vehicles/{id}`
  - Response:
    ```json
    {}
    ```
- **GET** `/api/vehicles/{id}/current-odo`
  - Response:
    ```json
    12345
    ```
- **GET** `/api/vehicles/{id}/odometer?start=YYYY-MM-DD&end=YYYY-MM-DD`
  - Response:
    ```json
    { "mileage": 1234 }
    ```
- **POST** `/api/vehicles/{id}/assign/pool`
  - Request:
    ```json
    { "mission": "string", "startDate": "YYYY-MM-DD", "endDate": "YYYY-MM-DD" }
    ```
  - Response:
    ```json
    { "id": 1, "vehicle": { /* Vehicle */ }, "mission": "string", "startDate": "YYYY-MM-DD", "endDate": "YYYY-MM-DD" }
    ```
- **POST** `/api/vehicles/{id}/assign/employee`
  - Request:
    ```json
    { "employeeId": 1, "startDate": "YYYY-MM-DD", "endDate": "YYYY-MM-DD" }
    ```
  - Response:
    ```json
    { "id": 1, "employee": { /* Employee */ }, "vehicle": { /* Vehicle */ }, "startDate": "YYYY-MM-DD", "endDate": "YYYY-MM-DD", "status": "string" }
    ```
- **GET** `/api/vehicles/{id}/forecast`
  - Response:
    ```json
    { "forecast": 1234.56 }
    ```

---

## Expenses
- **GET** `/api/expenses`
  - Response:
    ```json
    [
      {
        "id": 1,
        "amount": 123.45,
        "date": "YYYY-MM-DD",
        "description": "string",
        "vehicle": { /* Vehicle */ },
        "vendor": { /* Vendor */ },
        "validatedBy": { /* User */ },
        "type": "FUEL" | "MAINTENANCE" | "OTHER"
      }
    ]
    ```
- **GET** `/api/expenses/{id}`
  - Response:
    ```json
    {
      "id": 1,
      "amount": 123.45,
      "date": "YYYY-MM-DD",
      "description": "string",
      "vehicle": { /* Vehicle */ },
      "vendor": { /* Vendor */ },
      "validatedBy": { /* User */ },
      "type": "FUEL" | "MAINTENANCE" | "OTHER"
    }
    ```
- **POST** `/api/expenses?vehicleId=number&vendorId=number&validatedBy=number`
  - Request:
    ```json
    { "amount": 123.45, "date": "YYYY-MM-DD", "description": "string", "type": "FUEL" | "MAINTENANCE" | "OTHER" }
    ```
  - Response:
    ```json
    { "id": 1, "amount": 123.45, "date": "YYYY-MM-DD", "description": "string", "vehicle": { /* Vehicle */ }, "vendor": { /* Vendor */ }, "validatedBy": { /* User */ }, "type": "FUEL" | "MAINTENANCE" | "OTHER" }
    ```
- **PUT** `/api/expenses/{id}`
  - Request:
    ```json
    { "amount": 123.45, "date": "YYYY-MM-DD", "description": "string", "vehicle": { /* Vehicle */ }, "vendor": { /* Vendor */ }, "validatedBy": { /* User */ }, "type": "FUEL" | "MAINTENANCE" | "OTHER" }
    ```
  - Response:
    ```json
    { "id": 1, "amount": 123.45, "date": "YYYY-MM-DD", "description": "string", "vehicle": { /* Vehicle */ }, "vendor": { /* Vendor */ }, "validatedBy": { /* User */ }, "type": "FUEL" | "MAINTENANCE" | "OTHER" }
    ```
- **DELETE** `/api/expenses/{id}`
  - Response:
    ```json
    {}
    ```
- **GET** `/api/expenses/total?type=FUEL`
  - Response:
    ```json
    { "total": 1234.56 }
    ```

---

## Vendors
- **GET** `/api/vendors`
  - Response:
    ```json
    [
      { "id": 1, "name": "string", "address": "string", "phone": "string", "email": "string" }
    ]
    ```
- **GET** `/api/vendors/{id}`
  - Response:
    ```json
    { "id": 1, "name": "string", "address": "string", "phone": "string", "email": "string" }
    ```
- **POST** `/api/vendors`
  - Request:
    ```json
    { "name": "string", "address": "string", "phone": "string", "email": "string", "passwordHash": "string" }
    ```
  - Response:
    ```json
    { "id": 1, "name": "string", "address": "string", "phone": "string", "email": "string" }
    ```
- **PUT** `/api/vendors/{id}`
  - Request:
    ```json
    { "name": "string", "address": "string", "phone": "string", "email": "string" }
    ```
  - Response:
    ```json
    { "id": 1, "name": "string", "address": "string", "phone": "string", "email": "string" }
    ```
- **DELETE** `/api/vendors/{id}`
  - Response:
    ```json
    {}
    ```

---

## Odometer Readings
- **GET** `/api/readings`
  - Response:
    ```json
    [
      { "id": 1, "vehicle": { /* Vehicle */ }, "date": "YYYY-MM-DD", "km": 12345 }
    ]
    ```
- **GET** `/api/readings/{id}`
  - Response:
    ```json
    { "id": 1, "vehicle": { /* Vehicle */ }, "date": "YYYY-MM-DD", "km": 12345 }
    ```
- **POST** `/api/readings`
  - Request:
    ```json
    { "vehicle": { /* Vehicle */ }, "date": "YYYY-MM-DD", "km": 12345 }
    ```
  - Response:
    ```json
    { "id": 1, "vehicle": { /* Vehicle */ }, "date": "YYYY-MM-DD", "km": 12345 }
    ```
- **PUT** `/api/readings/{id}`
  - Request:
    ```json
    { "vehicle": { /* Vehicle */ }, "date": "YYYY-MM-DD", "km": 12345 }
    ```
  - Response:
    ```json
    { "id": 1, "vehicle": { /* Vehicle */ }, "date": "YYYY-MM-DD", "km": 12345 }
    ```
- **GET** `/api/readings/vehicle/{vehicleId}`
  - Response:
    ```json
    [ { "id": 1, "vehicle": { /* Vehicle */ }, "date": "YYYY-MM-DD", "km": 12345 } ]
    ```
- **DELETE** `/api/readings/{id}`
  - Response:
    ```json
    {}
    ```

---

## Forecast
- **GET** `/api/forecast/{vehicleId}`
  - Response:
    ```json
    1234.56
    ```
- **GET** `/api/vehicles/{id}/forecast`
  - Response:
    ```json
    { "forecast": 1234.56 }
    ```

---

## Reports
- **GET** `/api/reports/vehicle/{id}?from=YYYY-MM-DD&to=YYYY-MM-DD`
  - Response:
    ```json
    { "vehicleId": 1, "totalDistance": 1234, "totalFuel": 123.45, "totalExpense": 2345.67, "tripCount": 10 }
    ```

---

Alanlar, entity ve DTO dosyalarından çıkarılmıştır. Eğer bir alan eksikse veya daha fazla detay isterseniz, ilgili entity veya DTO dosyasını belirtirseniz ekleyebilirim.