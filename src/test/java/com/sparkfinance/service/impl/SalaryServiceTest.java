package com.sparkfinance.service.impl;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.math.BigDecimal;

import static org.testng.Assert.assertTrue;

public class SalaryServiceTest {

    SalaryService sal;

    @BeforeTest
    void setup() {
        sal = new SalaryService();
    }


    @Test
    public void testGetRealSalary() throws Exception {
        BigDecimal salary = sal.getRealSalary("2014", "100000", "CA", "0", "YEAR", "single");
        BigDecimal expectedAmount = new BigDecimal("65137.50");
        assertTrue(salary.compareTo(expectedAmount) == 0, "Expected to get " + expectedAmount + " but got " + salary + " instead");
    }

    @Test
    public void testGetRealSalaryZeroSalary() throws Exception {
        BigDecimal salary = sal.getRealSalary("2014", "0", "CA", "4", "YEAR", "single");
        BigDecimal expectedAmount = BigDecimal.ZERO;
        assertTrue(salary.compareTo(expectedAmount) == 0, "Expected to get " + expectedAmount + " but got " + salary + " instead");
    }

    @Test
    public void testGetRealSalaryWeekly() throws Exception {
        BigDecimal salary = sal.getRealSalary("2014", "100000", "CA", "0", "WEEK", "single");
        BigDecimal expectedAmount = new BigDecimal("1252.65");
        assertTrue(salary.compareTo(expectedAmount) == 0, "Expected to get " + expectedAmount + " but got " + salary + " instead");
    }

    @Test
    public void testGetRealSalaryWith401k() throws Exception {
        BigDecimal salary = sal.getRealSalary("2014", "100000", "CA", "4", "YEAR", "single");
        BigDecimal expectedAmount = new BigDecimal("62629.50");
        assertTrue(salary.compareTo(expectedAmount) == 0, "Expected to get " + expectedAmount + " but got " + salary + " instead");
    }

    @Test
    public void testGetRealSalaryWithNoStateIncomeTax() throws Exception {
        BigDecimal salary = sal.getRealSalary("2014", "100000", "TN", "0", "YEAR", "single");
        BigDecimal expectedAmount = new BigDecimal("72000.00");
        assertTrue(salary.compareTo(expectedAmount) == 0, "Expected to get " + expectedAmount + " but got " + salary + " instead");
    }

    @Test
    public void testGetSalaryAfter401k() throws Exception {
        BigDecimal salary = sal.getSalaryAfter401k(new BigDecimal(100000.00), "4");
        BigDecimal expectedAmount = new BigDecimal("96000.00");
        assertTrue(salary.compareTo(expectedAmount) == 0, "Expected to get " + expectedAmount + " but got " + salary + " instead");
    }

    @Test
    public void testGetFedTax() throws Exception {
        BigDecimal tax = sal.getFedTax(new BigDecimal(100000));
        assertTrue(tax.compareTo(new BigDecimal(28000.00)) == 0);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testSalaryIsZeroFedTaxRate() throws IllegalArgumentException {
        BigDecimal taxRate = sal.getFedTaxRate(BigDecimal.ZERO);
    }

    @Test
    public void testGetFedTaxRate() throws Exception {
        BigDecimal taxRate = sal.getFedTaxRate(new BigDecimal(100000));
        System.out.println("tax rate is " + taxRate.toString());
        assertTrue(taxRate.compareTo(new BigDecimal("0.28")) == 0);
    }

    @Test
    public void testGetSalaryPerTimerPeriod() throws Exception {
        BigDecimal salary = sal.getSalaryPerTimerPeriod("WEEK", new BigDecimal(100000));
        System.out.println("salary is " + salary.toString());
        assertTrue(salary.compareTo(new BigDecimal("1924")) == 0);
    }
}