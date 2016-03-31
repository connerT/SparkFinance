package com.sparkfinance.util;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;


public class StateConstantsTest {

    StateConstants sc;

    @BeforeTest
    void setup() {
        sc = new StateConstants();
    }

    @Test
    public void testAbbreviationToFullName() {
        String fullName = sc.abbreviationToFullName("WV");
        assertEquals(fullName, "West Virginia");
    }

}