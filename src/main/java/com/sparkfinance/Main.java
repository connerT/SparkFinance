package com.sparkfinance;

import com.sparkfinance.service.impl.SalaryService;

/**
 * Created by connertolley on 3/31/16.
 */
public class Main {

    public static void main(String[] args) {
        new FinanceController(new SalaryService());
    }

}
