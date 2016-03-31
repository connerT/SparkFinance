package com.sparkfinance.service.impl;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class SalaryService {

    // fed tax rates
    final BigDecimal FED_TAX_10 = BigDecimal.valueOf(0.10); // $1 - 9075
    final BigDecimal FED_TAX_15 = BigDecimal.valueOf(0.15); // $9076 - 36900
    final BigDecimal FED_TAX_25 = BigDecimal.valueOf(0.25); // $36901 - 89350
    final BigDecimal FED_TAX_28 = BigDecimal.valueOf(0.28); // $89351 - 186350
    final BigDecimal FED_TAX_33 = BigDecimal.valueOf(0.33); // $186,351 - 405,100
    final BigDecimal FED_TAX_35 = BigDecimal.valueOf(0.35); // $405,101 - $406,750
    final BigDecimal FED_TAX_39 = BigDecimal.valueOf(0.396); // > $406,750

    public BigDecimal getRealSalary(String year, String salaryString, String state, String fourOoneK, String timePeriod, String filingStatus) {
        BigDecimal takeHomePay = BigDecimal.valueOf(0);
        salaryString = sanitizeString(salaryString);
        BigDecimal salary = new BigDecimal(salaryString);
        try {
            //subtract 401K first
            takeHomePay = getSalaryAfter401k(salary, fourOoneK);
            //then subtract federal taxes
            BigDecimal fedTax = getFedTax(takeHomePay);
            //then subtract state taxes
            BigDecimal stateTax = getStateTax(takeHomePay, filingStatus, state, year);
            // Divide amount by time period requested
            takeHomePay = takeHomePay.subtract(fedTax).subtract(stateTax);
            takeHomePay = getSalaryPerTimerPeriod(timePeriod, takeHomePay);
        } catch (NumberFormatException ex) {
            System.out.println("You must enter a number for your income");
        } catch (Exception ex) {
            System.out.println("An unexpected exception has occurred");
        }

        return takeHomePay;
    }

    private BigDecimal getStateTax(BigDecimal takeHomePay, String filingStatus, String state, String year) {
        StateCalculator sc = new StateCalculator();
        BigDecimal stateTaxAmount = sc.getStateTaxAmount(year, takeHomePay, filingStatus, state);
        return stateTaxAmount;
    }

    public BigDecimal getSalaryPerTimerPeriod(String timePeriod, BigDecimal salary) {
        salary = validateSalary(salary);
        switch (timePeriod) {
            case "WEEK":
                salary = salary.divide(new BigDecimal(52), 2);
                break;
            case "BIWEEK":
                salary = salary.divide(new BigDecimal(26), 2);
                break;
            case "MONTH":
                salary = salary.divide(new BigDecimal(12), 2);
                break;
            case "YEAR":
                salary = salary.divide(new BigDecimal(1), 2);
                break;
            default:
                throw new IllegalArgumentException("Invalid time period: " + timePeriod);
        }
        return salary;
    }

    private String sanitizeString(String s) {
        //remove commas
        s = s.replaceAll("[^\\d.]", "");
        return s;
    }

    private BigDecimal validateSalary(BigDecimal salary) {
        if (salary.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Salary cannot be less than or equal to zero");
        }
        return salary;
    }

    public BigDecimal getSalaryAfter401k(BigDecimal salary, String fourOoneK) {
        BigDecimal income = salary;
        BigDecimal fourK = new BigDecimal(fourOoneK);
        BigDecimal fourKdecimal = fourK.divide(new BigDecimal(100));
        BigDecimal amountToSubtract = income.multiply(fourKdecimal);
        BigDecimal endAmount = income.subtract(amountToSubtract);
        return endAmount;
    }

    public BigDecimal getFedTax(BigDecimal salary) {
        BigDecimal taxRate = getFedTaxRate(salary);
        BigDecimal tax = salary.multiply(taxRate);
        tax = tax.setScale(2, RoundingMode.CEILING);
        return tax;
    }

    public BigDecimal getFedTaxRate(BigDecimal salary) {

        salary = validateSalary(salary);
        BigDecimal taxRate = BigDecimal.ZERO;

        if (salary.compareTo(BigDecimal.ZERO) >= 0 &&
                salary.compareTo(new BigDecimal(9075.00)) <= 0) {
            taxRate = FED_TAX_10;
        } else if (salary.compareTo(new BigDecimal(9075.00)) > 0 &&
                salary.compareTo(new BigDecimal(36900.00)) <= 0) {
            taxRate = FED_TAX_15;
        } else if (salary.compareTo(new BigDecimal(36900.00)) > 0 &&
                salary.compareTo(new BigDecimal(89350.00)) <= 0) {
            taxRate = FED_TAX_25;
        } else if (salary.compareTo(new BigDecimal(89350.00)) > 0 &&
                salary.compareTo(new BigDecimal(186350.00)) <= 0) {
            taxRate = FED_TAX_28;
        } else if (salary.compareTo(new BigDecimal(186350.00)) > 0 &&
                salary.compareTo(new BigDecimal(405100.00)) <= 0) {
            taxRate = FED_TAX_33;
        } else if (salary.compareTo(new BigDecimal(405100.00)) > 0 &&
                salary.compareTo(new BigDecimal(406750.00)) <= 0) {
            taxRate = FED_TAX_35;
        } else {
            taxRate = FED_TAX_39;
        }
        return taxRate;
    }
}
