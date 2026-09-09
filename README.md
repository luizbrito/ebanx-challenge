# EBANX Software Engineer Take-home Challenge

REST API developed as part of the **EBANX Software Engineer Take-home Assignment**.

The application implements a simple in-memory account system supporting:

- Account balance retrieval
- Deposits
- Withdrawals
- Transfers
- Application state reset

No database or persistence mechanism is used, as durability is not required by the challenge specification.

---

## Project Links

### Source Code

GitHub repository:

```text
https://github.com/luizbrito/ebanx-challenge/
```

### Public API

Current ngrok public endpoint:

```text
https://wooing-scotch-genetics.ngrok-free.dev
```

> The ngrok URL is temporary and is available only while the local application and ngrok tunnel are running.

### EBANX Automated Test Suite

```text
https://ipkiss.ebanx.ninja/
```

---

## Technologies

- Java 21
- Spring Boot 4.1.1
- Spring Web MVC
- Maven
- JUnit 5
- MockMvc
- SpringDoc OpenAPI / Swagger UI
- ngrok

---

# Requirements

Before running the project, make sure the following tools are installed:

- Java 21 or newer
- Maven
- Git
- ngrok, if public access is required

Check Java:

```bash
java -version
```

Check Maven:

```bash
mvn -version
```

Check Git:

```bash
git --version
```

Check ngrok:

```bash
ngrok version
```

---

# Clone the Repository

Clone the project directly from GitHub:

```bash
git clone https://github.com/luizbrito/ebanx-challenge.git
```

Enter the project directory:

```bash
cd ebanx-challenge
```

---

# Project Structure

```text
src
├── main
│   ├── java
│   │   └── com.luizbrito.ebanx_challenge
│   │       ├── config
│   │       │   └── OpenApiConfig.java
│   │       ├── controller
│   │       │   └── AccountController.java
│   │       ├── dto
│   │       │   ├── AccountResponse.java
│   │       │   ├── DepositResponse.java
│   │       │   ├── EventRequest.java
│   │       │   ├── EventType.java
│   │       │   ├── TransferResponse.java
│   │       │   └── WithdrawResponse.java
│   │       ├── service
│   │       │   └── AccountService.java
│   │       └── EbanxChallengeApplication.java
│   │
│   └── resources
│       └── application.properties
│
└── test
    └── java
        └── com.luizbrito.ebanx_challenge
            ├── controller
            │   └── AccountControllerIntegrationTest.java
            ├── service
            │   └── AccountServiceTest.java
            └── EbanxChallengeApplicationTests.java
```

---

# Architecture

The application follows a simple layered architecture:

```text
HTTP Request
     |
     v
AccountController
     |
     v
AccountService
     |
     v
In-memory Map<String, Long>
```

## Controller

Responsible for:

- Receiving HTTP requests
- Mapping request payloads
- Returning the required HTTP status codes
- Formatting API responses

## Service

Responsible for:

- Account business rules
- Deposits
- Withdrawals
- Transfers
- Balance retrieval
- Resetting application state

## In-memory Storage

Accounts are stored using:

```java
Map<String, Long>
```

Example:

```text
{
    "100" -> 15,
    "300" -> 20
}
```

The data exists only while the application is running.

Restarting the application clears all accounts.

---

# Build the Application

From the project root directory:

```bash
mvn clean package
```

A successful build should end with:

```text
BUILD SUCCESS
```

The generated executable JAR will be created under:

```text
target/ebanx-challenge-0.0.1-SNAPSHOT.jar
```

---

# Run Automated Tests

Run all tests with:

```bash
mvn test
```

Or:

```bash
mvn clean test
```

The project contains unit tests and integration tests.

## Unit Tests

`AccountServiceTest`

Validates the business rules independently from the HTTP transport layer.

Covered scenarios include:

- Balance for a non-existing account
- Creating an account through deposit
- Depositing into an existing account
- Withdrawing from an existing account
- Withdrawing from a non-existing account
- Transferring between accounts
- Transferring from a non-existing account
- Resetting application state

## Integration Tests

`AccountControllerIntegrationTest`

Uses Spring Boot and MockMvc to validate the complete HTTP contract.

These tests reproduce the main scenarios from the EBANX automated test suite.

A successful execution should finish with:

```text
Failures: 0
Errors: 0
```

---

# Running the Application

Run with Maven:

```bash
mvn spring-boot:run
```

The application starts locally at:

```text
http://localhost:8080
```

Alternatively:

```bash
java -jar target/ebanx-challenge-0.0.1-SNAPSHOT.jar
```

---

# Swagger / OpenAPI

With the application running, Swagger UI is available at:

```text
http://localhost:8080/swagger-ui.html
```

or:

```text
http://localhost:8080/swagger-ui/index.html
```

The generated OpenAPI specification is available at:

```text
http://localhost:8080/v3/api-docs
```

---

# API Endpoints

The API exposes three endpoints:

```text
POST /reset
GET  /balance
POST /event
```

---

# Reset Application State

Clears all accounts stored in memory.

## Request

```http
POST /reset
```

Local example:

```bash
curl -X POST http://localhost:8080/reset
```

Public example:

```bash
curl -X POST https://wooing-scotch-genetics.ngrok-free.dev/reset
```

## Response

```http
200 OK
```

Body:

```text
OK
```

---

# Get Account Balance

Returns the current balance of an account.

## Request

```http
GET /balance?account_id={accountId}
```

Local example:

```bash
curl "http://localhost:8080/balance?account_id=100"
```

Public example:

```bash
curl "https://wooing-scotch-genetics.ngrok-free.dev/balance?account_id=100"
```

## Existing Account

Response:

```http
200 OK
```

Example body:

```text
20
```

## Non-existing Account

Response:

```http
404 Not Found
```

Body:

```text
0
```

---

# Process Event

Account transactions are processed through:

```http
POST /event
```

Header:

```http
Content-Type: application/json
```

Supported event types:

```text
deposit
withdraw
transfer
```

---

# Deposit

Deposits an amount into a destination account.

If the account does not exist, it is automatically created.

## Request

```json
{
  "type": "deposit",
  "destination": "100",
  "amount": 10
}
```

Local example:

```bash
curl -X POST \
  -H "Content-Type: application/json" \
  -d "{\"type\":\"deposit\",\"destination\":\"100\",\"amount\":10}" \
  http://localhost:8080/event
```

Public example:

```bash
curl -X POST \
  -H "Content-Type: application/json" \
  -d "{\"type\":\"deposit\",\"destination\":\"100\",\"amount\":10}" \
  https://wooing-scotch-genetics.ngrok-free.dev/event
```

## Response

```http
201 Created
```

```json
{
  "destination": {
    "id": "100",
    "balance": 10
  }
}
```

---

# Deposit Into Existing Account

If account `100` already contains a balance of `10`, sending:

```json
{
  "type": "deposit",
  "destination": "100",
  "amount": 10
}
```

returns:

```http
201 Created
```

```json
{
  "destination": {
    "id": "100",
    "balance": 20
  }
}
```

---

# Withdraw

Withdraws an amount from an origin account.

## Request

```json
{
  "type": "withdraw",
  "origin": "100",
  "amount": 5
}
```

## Existing Account

Response:

```http
201 Created
```

```json
{
  "origin": {
    "id": "100",
    "balance": 15
  }
}
```

## Non-existing Account

Request:

```json
{
  "type": "withdraw",
  "origin": "200",
  "amount": 10
}
```

Response:

```http
404 Not Found
```

Body:

```text
0
```

---

# Transfer

Transfers an amount from an origin account to a destination account.

If the destination account does not exist, it is automatically created.

## Request

```json
{
  "type": "transfer",
  "origin": "100",
  "amount": 15,
  "destination": "300"
}
```

## Response

```http
201 Created
```

```json
{
  "origin": {
    "id": "100",
    "balance": 0
  },
  "destination": {
    "id": "300",
    "balance": 15
  }
}
```

---

# Transfer From Non-existing Account

Request:

```json
{
  "type": "transfer",
  "origin": "200",
  "amount": 15,
  "destination": "300"
}
```

Response:

```http
404 Not Found
```

Body:

```text
0
```

---

# Complete EBANX Test Flow

The expected challenge flow is:

```text
POST /reset
→ 200 OK

GET /balance?account_id=1234
→ 404 0

POST /event
{"type":"deposit","destination":"100","amount":10}
→ 201 {"destination":{"id":"100","balance":10}}

POST /event
{"type":"deposit","destination":"100","amount":10}
→ 201 {"destination":{"id":"100","balance":20}}

GET /balance?account_id=100
→ 200 20

POST /event
{"type":"withdraw","origin":"200","amount":10}
→ 404 0

POST /event
{"type":"withdraw","origin":"100","amount":5}
→ 201 {"origin":{"id":"100","balance":15}}

POST /event
{"type":"transfer","origin":"100","amount":15,"destination":"300"}
→ 201 {"origin":{"id":"100","balance":0},"destination":{"id":"300","balance":15}}

POST /event
{"type":"transfer","origin":"200","amount":15,"destination":"300"}
→ 404 0
```

---

# Exposing the API With ngrok

The EBANX tester needs a publicly accessible API.

With the Spring Boot application running locally:

```bash
mvn spring-boot:run
```

open another terminal and execute:

```bash
ngrok http 8080
```

The currently used public endpoint is:

```text
https://wooing-scotch-genetics.ngrok-free.dev
```

It forwards requests to:

```text
http://localhost:8080
```

> Because this is an ngrok development tunnel, this URL should not be considered a permanent production endpoint.

---

# Verify the Public API

Reset:

```bash
curl -X POST https://wooing-scotch-genetics.ngrok-free.dev/reset
```

Expected:

```text
OK
```

Check a non-existing account:

```bash
curl -i "https://wooing-scotch-genetics.ngrok-free.dev/balance?account_id=1234"
```

Expected:

```text
404
0
```

---

# Running the Official EBANX Tester

Open:

```text
https://ipkiss.ebanx.ninja/
```

Use the following base URL:

```text
https://wooing-scotch-genetics.ngrok-free.dev
```

Only the base URL must be supplied.

Do not append:

```text
/event
/balance
/reset
/swagger-ui
```

The tester automatically executes the required endpoints.

The expected successful result is:

```text
✅ Reset state before starting tests

✅ Get balance for non-existing account

✅ Create account with initial balance

✅ Deposit into existing account

✅ Get balance for existing account

✅ Withdraw from non-existing account

✅ Withdraw from existing account

✅ Transfer from existing account

✅ Transfer from non-existing account
```

---

# Important Notes

## Persistence

The application intentionally does not use a database.

All account balances exist only in application memory.

This follows the challenge requirement that durability is not necessary.

## Concurrency

Account operations are synchronized at the service layer to prevent concurrent operations from inconsistently modifying the shared in-memory state.

## Event Types

Event types are represented internally using:

```java
DEPOSIT
WITHDRAW
TRANSFER
```

Their JSON representations are:

```text
deposit
withdraw
transfer
```

## Scope

The implementation follows only the behavior explicitly required by the challenge.

Additional banking rules such as overdraft validation, transaction history, authentication, persistence, or external messaging were intentionally not implemented in order to keep the solution simple and malleable.

---

# Useful Commands

Clone:

```bash
git clone https://github.com/luizbrito/ebanx-challenge.git
```

Compile:

```bash
mvn compile
```

Run tests:

```bash
mvn test
```

Clean and test:

```bash
mvn clean test
```

Build:

```bash
mvn clean package
```

Run:

```bash
mvn spring-boot:run
```

Run generated JAR:

```bash
java -jar target/ebanx-challenge-0.0.1-SNAPSHOT.jar
```

Start ngrok:

```bash
ngrok http 8080
```

---

# Final Validation Checklist

Before submitting the challenge:

- [x] Repository is available at `https://github.com/luizbrito/ebanx-challenge/`
- [x] Application builds successfully
- [x] Unit tests pass
- [x] Integration tests pass
- [x] `/reset` returns `200 OK` with body `OK`
- [x] `/balance` returns the expected status and body
- [x] Deposit works for new accounts
- [x] Deposit works for existing accounts
- [x] Withdraw works for existing accounts
- [x] Withdraw from a non-existing account returns `404 0`
- [x] Transfer works for existing origin accounts
- [x] Transfer from a non-existing origin returns `404 0`
- [x] Swagger UI works
- [x] ngrok exposes the API
- [x] EBANX automated test suite passes
- [x] `target/` is not committed
- [x] No ngrok authentication token is committed
- [x] No unnecessary IDE or generated files are committed

---

# URLs Summary

| Resource             | URL                                             |
| -------------------- | ----------------------------------------------- |
| GitHub Repository    | `https://github.com/luizbrito/ebanx-challenge/` |
| Local API            | `http://localhost:8080`                         |
| Swagger UI           | `http://localhost:8080/swagger-ui/index.html`   |
| OpenAPI JSON         | `http://localhost:8080/v3/api-docs`             |
| Public API via ngrok | `https://wooing-scotch-genetics.ngrok-free.dev` |
| EBANX Test Suite     | `https://ipkiss.ebanx.ninja/`                   |

---

## Author

**Luiz Brito da Rosa**

Developed for the EBANX Software Engineer Take-home Challenge.
