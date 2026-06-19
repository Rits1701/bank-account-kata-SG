package com.bank.adapters.out;

import com.bank.ports.out.ClockProvider;

import java.time.LocalDate;

public class SystemClockProvider implements ClockProvider {
    @Override
    public LocalDate now() {
        return LocalDate.now();
    }
}
