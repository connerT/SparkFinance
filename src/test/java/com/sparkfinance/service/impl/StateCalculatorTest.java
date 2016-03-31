package com.sparkfinance.service.impl;

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
        BigDecimal income =  new BigDecimal(100000.00);
        BigDecimal amount = sc.getStateTaxAmount("2014", income, "single", "CA");
        BigDecimal expectedAmount = new BigDecimal("6862.50");
        assertTrue(amount.compareTo(expectedAmount) == 0, "Expected to get " + expectedAmount + " but got " + amount + " instead");
    }

    @Test
    public void testGetStateTaxAmountMarried() throws Exception {
        BigDecimal income =  new BigDecimal(100000.00);
        BigDecimal amount = sc.getStateTaxAmount("2014", income, "married", "CA");
        BigDecimal expectedAmount = new BigDecimal("4425.00");
        assertTrue(amount.compareTo(expectedAmount) == 0, "Expected " + expectedAmount + " but got " + amount + " instead");
    }

    @Test
    public void testGetStateTaxAmountMarriedSeparately() throws Exception {
        BigDecimal income =  new BigDecimal(100000.00);
        BigDecimal amount = sc.getStateTaxAmount("2014", income, "married_separately", "CA");
        BigDecimal expectedAmount = new BigDecimal("6862.50");
        assertTrue(amount.compareTo(expectedAmount) == 0, "Expected to get " + expectedAmount + " but got " + amount + " instead");
    }

    @Test
    public void testGetStateTaxAmountHeadOfHousehold() throws Exception {
        BigDecimal income =  new BigDecimal("100000.00");
        BigDecimal amount = sc.getStateTaxAmount("2014", income, "head_of_household", "CA");
        BigDecimal expectedAmount = new BigDecimal("6862.50");
        assertTrue(amount.compareTo(expectedAmount) == 0, "Expected to get " + expectedAmount + " but got " + amount + " instead");
    }

    @Test
    public void testGetStateTaxAmountNoStateTax() throws Exception {
        BigDecimal income =  new BigDecimal(100000.00);
        BigDecimal amount = sc.getStateTaxAmount("2014", income, "single", "TN");
        BigDecimal expectedAmount = BigDecimal.ZERO;
        assertTrue(amount.compareTo(expectedAmount) == 0, "Expected to get " + expectedAmount + " but got " + amount + " instead");
    }
}