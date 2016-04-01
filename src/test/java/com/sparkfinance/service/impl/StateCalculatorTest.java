package com.sparkfinance.service.impl;

import com.sparkfinance.model.SalaryResults;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.math.BigDecimal;

import static org.testng.Assert.*;


public class StateCalculatorTest {

    StateCalculator sc;

    @BeforeTest
    void setup() {
        sc = new StateCalculator();
    }

    @Test
    public void testGetStateData() throws Exception {
        StateResponse sr = sc.getStateData("2014", "CA");
        assertEquals(sr.isSuccess(), true);
        assertEquals(sr.getStateName(), "california");
        assertNotNull(sr.getData());
    }

    @Test
    public void testCalculate() throws Exception {

    }

    @Test
    public void testGetStateTaxAmountSingle() throws Exception {
        BigDecimal salary =  new BigDecimal(100000.00);
        SalaryResults sr = new SalaryResults();
        sr.setYear("2014");
        sr.setState("CA");
        sr.setSalary(salary);
        sr.setFilingStatus("single");
        sr = sc.getStateTaxAmount(sr);
        BigDecimal amount = sr.getStateTax();
        BigDecimal expectedAmount = new BigDecimal("6862.50");
        assertTrue(amount.compareTo(expectedAmount) == 0, "Expected to get " + expectedAmount + " but got " + amount + " instead");
    }

    @Test
    public void testGetStateTaxAmountMarried() throws Exception {
        BigDecimal salary =  new BigDecimal(100000.00);
        SalaryResults sr = new SalaryResults();
        sr.setYear("2014");
        sr.setState("CA");
        sr.setSalary(salary);
        sr.setFilingStatus("married");
        sr = sc.getStateTaxAmount(sr);
        BigDecimal amount = sr.getStateTax();
        BigDecimal expectedAmount = new BigDecimal("4425.00");
        assertTrue(amount.compareTo(expectedAmount) == 0, "Expected " + expectedAmount + " but got " + amount + " instead");
    }

    @Test
    public void testGetStateTaxAmountMarriedSeparately() throws Exception {
        BigDecimal salary =  new BigDecimal(100000.00);
        SalaryResults sr = new SalaryResults();
        sr.setYear("2014");
        sr.setState("CA");
        sr.setSalary(salary);
        sr.setFilingStatus("married_separately");
        sr = sc.getStateTaxAmount(sr);
        BigDecimal amount = sr.getStateTax();
        BigDecimal expectedAmount = new BigDecimal("6862.50");
        assertTrue(amount.compareTo(expectedAmount) == 0, "Expected to get " + expectedAmount + " but got " + amount + " instead");
    }

    @Test
    public void testGetStateTaxAmountHeadOfHousehold() throws Exception {
        BigDecimal salary =  new BigDecimal("100000.00");
        SalaryResults sr = new SalaryResults();
        sr.setYear("2014");
        sr.setState("CA");
        sr.setSalary(salary);
        sr.setFilingStatus("head_of_household");
        sr = sc.getStateTaxAmount(sr);
        BigDecimal amount = sr.getStateTax();
        BigDecimal expectedAmount = new BigDecimal("6862.50");
        assertTrue(amount.compareTo(expectedAmount) == 0, "Expected to get " + expectedAmount + " but got " + amount + " instead");
    }

    @Test
    public void testGetStateTaxAmountNoStateTax() throws Exception {
        BigDecimal salary =  new BigDecimal(100000.00);
        SalaryResults sr = new SalaryResults();
        sr.setYear("2014");
        sr.setState("TN");
        sr.setSalary(salary);
        sr.setFilingStatus("single");
        sr = sc.getStateTaxAmount(sr);
        BigDecimal amount = sr.getStateTax();
        BigDecimal expectedAmount = BigDecimal.ZERO;
        assertTrue(amount.compareTo(expectedAmount) == 0, "Expected to get " + expectedAmount + " but got " + amount + " instead");
    }
}