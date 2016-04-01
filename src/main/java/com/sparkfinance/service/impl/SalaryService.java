package com.sparkfinance.service.impl;

import com.sparkfinance.model.SalaryResults;
import org.apache.commons.lang3.StringUtils;
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

    public SalaryResults getRealSalary(String year, String salaryString, String state, String fourOoneK, String timePeriod, String filingStatus) {
        salaryString = sanitizeString(salaryString);
        SalaryResults salResults = new SalaryResults();
        salResults.setSalary(new BigDecimal(salaryString));
        salResults.setFilingStatus(filingStatus);
        salResults.setState(state);
        salResults.setYear("2014");
        salResults.setTimePeriod(timePeriod);
        salResults.setFourOoneKpercentage(new BigDecimal(fourOoneK));
        try {
            //validate salary is not less than zero
            salResults = validateSalary(salResults);
            //subtract 401K first
            salResults = getSalaryAfter401k(salResults);
            //then get federal taxes
            salResults = getFedTax(salResults);
            //then get state taxes
            salResults = getStateTax(salResults);
            // Get salary so far (minus 401k)
            BigDecimal sal = salResults.getSalary();
            // Subtract Federal tax
            sal = sal.subtract(salResults.getFederalTax());
            // Subtract State tax
            sal = sal.subtract(salResults.getStateTax());
            // Set the "real" income before dividing by pay periods
            salResults.setSalary(sal);
            // Divide by the time period
            salResults = getSalaryPerTimerPeriod(salResults);
            // Set the final(real) income
            salResults.setRealIncome(salResults.getSalary());
        } catch (NumberFormatException ex) {
            System.out.println("You must enter a number for your income");
        } catch (IllegalArgumentException ex) {
            System.out.println("An illegal argument exception has occured");
        } catch (Exception ex) {
            System.out.println("An unexpected exception has occurred");
        }

        return salResults;
    }

    private SalaryResults getStateTax(SalaryResults sal) {
        StateCalculator sc = new StateCalculator();
        SalaryResults results = sc.getStateTaxAmount(sal);
        return results;
    }

    public SalaryResults getSalaryPerTimerPeriod(SalaryResults sal) {

        String timePeriod = StringUtils.upperCase(sal.getTimePeriod());
        BigDecimal salary = sal.getSalary();
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
        sal.setSalary(salary);
        return sal;
    }

    private String sanitizeString(String s) {
        //remove commas
        s = s.replaceAll("[^\\d.]", "");
        return s;
    }

    private SalaryResults validateSalary(SalaryResults sal) {
        BigDecimal salary = sal.getSalary();
        if (salary.compareTo(BigDecimal.ZERO) <= 0) {
            sal.setSuccess(false);
            sal.setReason("Salary cannot be zero or less");
            throw new IllegalArgumentException("Salary cannot be less than or equal to zero");
        }
        return sal;
    }

    public SalaryResults getSalaryAfter401k(SalaryResults sal) {
        BigDecimal income = sal.getSalary();
        BigDecimal fourK = sal.getFourOoneKpercentage();
        if (fourK.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal fourKdecimal = fourK.divide(new BigDecimal(100));
            BigDecimal amountToSubtract = income.multiply(fourKdecimal);
            BigDecimal endAmount = income.subtract(amountToSubtract);
            sal.setSalary(endAmount);
        } else {
            // don't do anything
        }
        return sal;
    }

    public SalaryResults getFedTax(SalaryResults sal) {
        sal = getFedTaxRate(sal);
        BigDecimal salary = sal.getSalary();
        BigDecimal tax = salary.multiply(sal.getFedTaxRate());
        tax = tax.setScale(2, RoundingMode.CEILING);
        sal.setFederalTax(tax);
        return sal;
    }

    public SalaryResults getFedTaxRate(SalaryResults sal) {

        BigDecimal salary = sal.getSalary();
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

        sal.setFedTaxRate(taxRate);
        return sal;
    }

    public SalaryResults getStateIncomeTax(String stateAbbr, BigDecimal incomeBD, String filingStatus) {
        SalaryResults sr = new SalaryResults();
        sr.setState(stateAbbr);
        sr.setSalary(incomeBD);
        sr.setFilingStatus(filingStatus);
        sr.setYear("2014");
        StateCalculator sc = new StateCalculator();
        sr = sc.getStateTaxAmount(sr);
        return sr;
    }
}
