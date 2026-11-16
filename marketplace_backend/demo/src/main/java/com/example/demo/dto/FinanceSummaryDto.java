package com.example.demo.dto;

import java.math.BigDecimal;

public record FinanceSummaryDto
(
        BigDecimal availableBalance,
        BigDecimal frozenBalance,
        BigDecimal totalPaid
) {}
