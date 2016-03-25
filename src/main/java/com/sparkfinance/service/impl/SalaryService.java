package com.sparkfinance.service.impl;

import org.springframework.stereotype.Service;

/**
 * Created by conner.tolley on 3/24/16.
 */
@Service
public class SalaryService {

    // fed tax rates
    final double FED_TAX_10 = 0.10; // $1 - 9075
    final double FED_TAX_15 = 0.15; // $9076 - 36900
    final double FED_TAX_25 = 0.25; // $36901 - 89350
    final double FED_TAX_28 = 0.28; // $89351 - 186350
    final double FED_TAX_33 = 0.33; // $186,351 - 405,100
    final double FED_TAX_35 = 0.35; // $405,101 - $406,750
    final double FED_TAX_39 = 0.396; // > $406,750

    public double getRealSalary(String salary, String stateTax, String fourOoneK) {
        double salAfterFedTax = 0;

        try {
            salary = salary.replaceAll("[^\\d.]", "");
            double begSalary = Double.valueOf(salary);
            salAfterFedTax = getSalaryAfterFedTax(begSalary);
        } catch (NumberFormatException ex) {
            System.out.println("You must enter a number for your income");
        }

        return salAfterFedTax;
    }

    private Double getSalaryAfterFedTax(double salary) {
        double taxRate = getFedTaxRate(salary);
        double tax = salary * taxRate;
        salary = salary - tax;
        return salary;
    }

    private double getFedTaxRate(double salary) {
        double taxRate = 0.0;
        return taxRate;
    }
}
