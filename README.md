# Bank Account Kata

## Overview

This project implements the **Bank Account Kata** using Java and a lightweight **Hexagonal Architecture (Ports & Adapters)** approach.

The goal is to provide a service API and its implementation that satisfies the following requirements:

* Deposit money
* Withdraw money
* View account operation history
* Print account statements

The implementation intentionally avoids:

* User Interface (UI)
* Database persistence
* Framework dependencies

in accordance with the kata instructions.

---

# Requirements

## US1 – Deposit

> In order to save money
> As a bank client
> I want to make a deposit in my account

## US2 – Withdrawal

> In order to retrieve some or all of my savings
> As a bank client
> I want to make a withdrawal from my account

## US3 – Account Statement

> In order to check my operations
> As a bank client
> I want to see the history (operation, date, amount, balance) of my operations

---

# Solution

The solution exposes a service API through the `BankAccountUseCase` interface:

```java
void deposit(Amount amount);

void withdraw(Amount amount);

String printStatement();
```

Example usage:

```java
service.deposit(Amount.of("1000"));
        service.withdraw(Amount.of("300"));

String statement = service.printStatement();
```

---

# Why Hexagonal Architecture?

Hexagonal Architecture (Ports & Adapters) was chosen to keep the business logic independent from technical concerns.

Even though this kata is relatively small, it demonstrates several software craftsmanship principles:

* Separation of Concerns
* Dependency Inversion
* Testability
* Maintainability
* Extensibility

The account business rules should not depend on:

* Console output
* System clock
* Database technology
* Frameworks
* REST APIs

Instead, business logic communicates through ports (interfaces), while infrastructure details are implemented through adapters.

### Without Hexagonal Architecture

```java
public class Account {

    public void deposit(...) {}

    public void withdraw(...) {}

    public void printStatement() {
        System.out.println(...);
    }
}
```

In this design, business logic and technical concerns become tightly coupled.

### With Hexagonal Architecture

```text
Deposit
Withdrawal
Statement Generation
        |
        v
      Ports
        |
        v
     Adapters
```

The business layer remains isolated from infrastructure details.

### Why These Ports?

Only three ports were introduced because they provide immediate value while keeping the solution simple.

#### ClockProvider

Avoids direct usage of:

```java
LocalDate.now()
```

inside business logic.

Benefits:

* Deterministic tests
* Easier maintenance
* Better separation of concerns

Example:

```java
ClockProvider fixedClock =
    () -> LocalDate.of(2026, 6, 14);
```

Tests always produce predictable results.

#### TransactionRepository

Today:

```text
InMemoryTransactionRepository
```

Tomorrow:

```text
JpaTransactionRepository
MongoTransactionRepository
```

could be introduced without changing business logic.

#### StatementPrinter

Today:

```text
ConsoleStatementPrinter
```

Tomorrow:

```text
PdfStatementPrinter
EmailStatementPrinter
```

could replace it without modifying account operations.

This follows the **Open/Closed Principle**: the application is open for extension but closed for modification.

---

# Project Structure

```text
com.bank
├── application
│   └── BankAccountService
│
├── domain
│   └── model
│       ├── Amount
│       ├── Transaction
│       └── TransactionType
│
├── ports
│   ├── in
│   │   └── BankAccountUseCase
│   │
│   └── out
│       ├── ClockProvider
│       ├── StatementPrinter
│       └── TransactionRepository
│
└── adapters
    └── out
        ├── ConsoleStatementPrinter
        ├── InMemoryTransactionRepository
        └── SystemClockProvider
```

---

# Architecture Details

## Domain Layer

The domain contains the business concepts.

### Amount

Represents a monetary value.

Responsibilities:

* Addition
* Subtraction
* Encapsulation of money-related logic

Using a dedicated value object avoids spreading money operations throughout the application.

---

### Transaction

Represents a banking operation.

Contains:

* Operation type
* Date
* Amount
* Balance after operation

A transaction is immutable and represents a complete account event.

---

### TransactionType

Defines the supported operations:

```java
DEPOSIT
WITHDRAWAL
```

---

## Application Layer

### BankAccountService

Implements the business use cases.

Responsibilities:

* Deposit money
* Withdraw money
* Maintain account balance
* Create transactions
* Delegate persistence
* Delegate statement formatting

The service contains the application workflow while remaining independent from infrastructure details.

---

## Input Port

### BankAccountUseCase

Defines the API exposed to clients.

```java
void deposit(Amount amount);

void withdraw(Amount amount);

String printStatement();
```

This is the service API requested by the kata.

---

## Output Ports

### TransactionRepository

Stores and retrieves transaction history.

The application depends only on the interface.

The current implementation stores transactions in memory.

---

### ClockProvider

Provides the current date.

This avoids direct usage of:

```java
LocalDate.now()
```

inside business logic and allows deterministic tests.

---

### StatementPrinter

Formats transaction history into a printable statement.

The application does not depend on how statements are displayed.

---

## Adapters

### InMemoryTransactionRepository

Stores transactions in memory.

Chosen because the kata explicitly states:

> Nothing more, especially no persistence

---

### SystemClockProvider

Returns the current system date.

Production implementation of `ClockProvider`.

---

### ConsoleStatementPrinter

Formats transactions into a statement string.

Current output format:

```text
Date || Operation || Amount || Balance
```

Example:

```text
2026-06-14 || DEPOSIT || 1000 || 1000
2026-06-14 || WITHDRAWAL || 300 || 700
2026-06-14 || DEPOSIT || 200 || 900
```

Returning a String keeps the solution independent from any UI.

---

# Requirement Coverage

## US1 – Deposit

Implemented through:

```java
deposit(Amount amount)
```

Behavior:

* Adds money to the current balance
* Creates a transaction
* Stores operation history

Example:

```java
service.deposit(Amount.of("1000"));
```

Result:

```text
Balance = 1000
```

---

## US2 – Withdrawal

Implemented through:

```java
withdraw(Amount amount)
```

Behavior:

* Subtracts money from the current balance
* Creates a transaction
* Stores operation history

Example:

```java
service.withdraw(Amount.of("300"));
```

Result:

```text
Balance = 700
```

---

## US3 – Account Statement

Implemented through:

```java
printStatement()
```

Returns a complete history of operations.

Example:

```text
Date || Operation || Amount || Balance

2026-06-14 || DEPOSIT || 1000 || 1000
2026-06-14 || WITHDRAWAL || 300 || 700
2026-06-14 || DEPOSIT || 200 || 900
```

This satisfies the requirement:

> history (operation, date, amount, balance)

Each statement line explicitly contains:

* Operation
* Date
* Amount
* Balance after operation

---

# Assumptions

The following assumptions were made:

* Amount must be strictly positive.
* Deposits increase the balance.
* Withdrawals decrease the balance.
* Negative balances are allowed because overdraft rules are not specified in the kata.
* Transactions are displayed in chronological order.
* No persistence is required.
* No user interface is required.
* Statement output remains intentionally simple.
* Dates are provided through `ClockProvider` to keep tests deterministic.

---

# Testing Strategy

The solution follows a TDD-inspired approach.

Covered scenarios:

### Deposit

* Deposit money
* Balance updated correctly

### Withdrawal

* Withdraw money
* Balance updated correctly

### Validation

* Negative amount rejected
* Zero amount rejected
* Null amount rejected

### Statement

* Statement contains operations
* Statement contains dates
* Statement contains amounts
* Statement contains balances
* Statement preserves operation history order

---

# Example

Given:

```java
service.deposit(Amount.of("1000"));
service.withdraw(Amount.of("300"));
service.deposit(Amount.of("200"));
```

The generated statement is:

```text
Date || Operation || Amount || Balance

2026-06-14 || DEPOSIT || 1000 || 1000
2026-06-14 || WITHDRAWAL || 300 || 700
2026-06-14 || DEPOSIT || 200 || 900
```

---

# How to Run

Run all tests:

```bash
mvn clean test
```

Expected result:

```text
BUILD SUCCESS
```

---
