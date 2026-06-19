Bank Account Kata
Senior-level implementation using Hexagonal Architecture and TDD.

Requirements
Deposit
Withdrawal
Statement printing (date | amount | balance)
No persistence
No UI
Architecture
Tests -> BankAccountService (Use Case) -> Domain -> Ports -> Adapters

Run
mvn clean test