package com.sparkfinance.service.impl;

import junit.framework.Assert;
import org.testng.annotations.Test;

/**
 * Created by conner.tolley on 3/24/16.
 */
public class SalaryServiceTest {

    @Test
    public void salaryAfterTaxes() {
        SalaryService sal = new SalaryService();
        double salary = sal.getRealSalary("100,000", "TN", "4");
        Assert.assertEquals(salary, 100,000);
    }

    @Test()
    public void salaryIsNotANumber() {
        SalaryService sal = new SalaryService();
        double salary = sal.getRealSalary("ConnerSalary", "TN", "4");
        Assert.assertNotNull(salary);
    }

}