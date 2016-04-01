package com.sparkfinance.controller;

import com.google.gson.Gson;
import com.sparkfinance.App;
import com.sparkfinance.model.SalaryResults;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import spark.Spark;
import spark.utils.IOUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.testng.Assert.*;
import static spark.Spark.awaitInitialization;


public class FinanceControllerTest {

    @BeforeTest
    public void beforeTest() {
        App.main(null);
        awaitInitialization();
    }

    @AfterTest
    public void afterTest() {
        Spark.stop();
    }

    @Test
    public void testGetFederalTax() {
        TestResponse res = request("GET", "/federal/100000");
        SalaryResults sr = res.json();
        assertEquals(200, res.status);
        BigDecimal fedTax = sr.getFederalTax();
        BigDecimal expectedResult = new BigDecimal("28000.00");
        assertTrue(fedTax.compareTo(expectedResult) == 0, "Expected to get " +
                expectedResult + " but got " + fedTax + " instead.");
    }

    @Test
    public void testGetStateTax() {
        TestResponse res = request("GET", "/state/ca/100000/single");
        SalaryResults sr = res.json();
        assertEquals(200, res.status);
        BigDecimal stateTax = sr.getStateTax();
        BigDecimal expectedResult =  new BigDecimal("6862.50");
        assertTrue(stateTax.compareTo(expectedResult) == 0, "Expected to get " +
                expectedResult + " but got " + stateTax + " instead.");
    }

    @Test
    public void testGetRealSalary() {
        TestResponse res = request("GET", "/ca/100000/single/4/year");
        SalaryResults sr = res.json();
        assertEquals(200, res.status);
        BigDecimal income = sr.getRealIncome();
        BigDecimal expectedResult = new BigDecimal("62629.50");
        assertTrue(income.compareTo(expectedResult) == 0, "Expected to get " +
                expectedResult + " but got " + income + " instead.");
    }

    private TestResponse request(String method, String path) {
        try {
            URL url = new URL("http://localhost:4567" + path);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(method);
            connection.setDoOutput(true);
            connection.connect();
            String body = IOUtils.toString(connection.getInputStream());
            return new TestResponse(connection.getResponseCode(), body);
        } catch (IOException e) {
            e.printStackTrace();
            fail("Sending request failed: " + e.getMessage());
            return null;
        }
    }

    private static class TestResponse {

        public final String body;
        public final int status;

        public TestResponse(int status, String body) {
            this.status = status;
            this.body = body;
        }

        public SalaryResults json() {
            return new Gson().fromJson(body, SalaryResults.class);
        }
    }
}