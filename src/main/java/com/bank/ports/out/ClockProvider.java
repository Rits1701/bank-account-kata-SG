package com.bank.ports.out;

import java.time.LocalDate;

public interface ClockProvider {
    LocalDate now();
}
