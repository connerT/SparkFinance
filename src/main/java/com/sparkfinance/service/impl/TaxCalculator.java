package com.sparkfinance.service.impl;

import java.math.BigDecimal;

/**
 * Created by connertolley on 3/29/16.
 */
public class TaxCalculator {

    public static final BigDecimal SSA_RATE = new BigDecimal(0.062);
    public static final BigDecimal MEDICARE_RATE = new BigDecimal(0.0145);
    public static final String SUPPORTED_YEARS[] = { "2014" };
}
