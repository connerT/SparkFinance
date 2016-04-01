package com.sparkfinance.service.impl;

import com.sparkfinance.model.SalaryResults;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.math.BigDecimal;

import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

public class SalaryServiceTest {

    SalaryService sal;

    @BeforeTest
    void setup() {
        sal = new SalaryService();
    }


    @Test
    public void testGetRealSalary() throws Exception {
        SalaryResults sr = sal.getRealSalary("2014", "100000", "CA", "0", "YEAR", "single");
        BigDecimal salary = sr.getRealIncome();
        BigDecimal expectedAmount = new BigDecimal("65137.50");
        assertTrue(salary.compareTo(expectedAmount) == 0, "Expected to get " + expectedAmount + " but got " + salary + " instead");
    }

    @Test
    public void testGetRealSalaryZeroSalary() throws Exception {
        SalaryResults sr = sal.getRealSalary("2014", "0", "CA", "4", "YEAR", "single");
        assertNull(sr.getRealIncome());
    }

    @Test
    public void testGetRealSalaryWeekly() throws Exception {
        SalaryResults sr = sal.getRealSalary("2014", "100000", "CA", "0", "WEEK", "single");
        BigDecimal salary = sr.getRealIncome();
        BigDecimal expectedAmount = new BigDecimal("1252.65");
        assertTrue(salary.compareTo(expectedAmount) == 0, "Expected to get " + expectedAmount + " but got " + salary + " instead");
    }

    @Test
    public void testGetRealSalaryWith401k() throws Exception {
        SalaryResults sr = sal.getRealSalary("2014", "100000", "CA", "4", "YEAR", "single");
        BigDecimal salary = sr.getRealIncome();
        BigDecimal expectedAmount = new BigDecimal("62629.50");
        assertTrue(salary.compareTo(expectedAmount) == 0, "Expected to get " + expectedAmount + " but got " + salary + " instead");
    }

    @Test
    public void testGetRealSalaryWithNoStateIncomeTax() throws Exception {
        SalaryResults sr = sal.getRealSalary("2014", "100000", "TN", "0", "YEAR", "single");
        BigDecimal salary = sr.getRealIncome();
        BigDecimal expectedAmount = new BigDecimal("72000.00");
        assertTrue(salary.compareTo(expectedAmount) == 0, "Expected to get " + expectedAmount + " but got " + salary + " instead");
    }

    @Test
    public void testGetSalaryAfter401k() throws Exception {
        SalaryResults sr = new SalaryResults();
        sr.setYear("2014");
        sr.setState("CA");
        sr.setSalary(new BigDecimal("100000.00"));
        sr.setFilingStatus("single");
        sr.setFourOoneKpercentage(new BigDecimal("4.00"));
        sr = sal.getSalaryAfter401k(sr);
        BigDecimal actualAmount = sr.getSalary();
        BigDecimal expectedAmount = new BigDecimal("96000.00");
        assertTrue(actualAmount.compareTo(expectedAmount) == 0, "Expected to get " + expectedAmount + " but got " + actualAmount + " instead");
    }

    @Test
    public void testGetFedTax() throws Exception {
        SalaryResults sr = new SalaryResults();
        sr.setSalary(new BigDecimal("100000.00"));
        sr = sal.getFedTax(sr);
        BigDecimal expectedResult = new BigDecimal("28000.00");
        BigDecimal tax = sr.getFederalTax();
        assertTrue(tax.compareTo(expectedResult) == 0, "Expected to get " + expectedResult + " but got " + tax + " instead");
    }

    @Test
    public void testSalaryIsZeroFedTaxRate() throws IllegalArgumentException {
        SalaryResults salRes = new SalaryResults();
        salRes.setSalary(BigDecimal.ZERO);
        SalaryResults sr = sal.getFedTaxRate(salRes);
        BigDecimal taxRate = sr.getFedTaxRate();
        BigDecimal expectedResult = new BigDecimal("0.1");
        assertTrue(taxRate.compareTo(expectedResult) == 0, "Expected to get " + expectedResult + " but got " + taxRate + " instead");
    }

    @Test
    public void testGetFedTaxRate() throws Exception {
        SalaryResults salRes = new SalaryResults();
        salRes.setSalary(new BigDecimal("100000.00"));
        SalaryResults sr = sal.getFedTaxRate(salRes);
        BigDecimal taxRate = sr.getFedTaxRate();
        BigDecimal expectedResult = new BigDecimal("0.28");
        assertTrue(taxRate.compareTo(expectedResult) == 0, "Expected to get " + expectedResult + " but got " + taxRate + " instead");
    }

    @Test
    public void testGetSalaryPerTimerPeriod() throws Exception {
        SalaryResults sr = new SalaryResults();
        sr.setTimePeriod("WEEK");
        sr.setSalary(new BigDecimal("100000.00"));
        sr = sal.getSalaryPerTimerPeriod(sr);
        BigDecimal expectedResults = new BigDecimal("1923.08");
        BigDecimal actualResults = sr.getSalary();
        assertTrue(actualResults.compareTo(expectedResults) == 0, "Expected to get " + expectedResults + " but got " + actualResults + " instead");
    }
}