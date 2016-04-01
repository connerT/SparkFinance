package com.sparkfinance; /**
 * Created by conner.tolley on 3/23/16.
 */

import com.sparkfinance.controller.FinanceController;
import com.sparkfinance.service.impl.SalaryService;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import static spark.Spark.*;

@Configuration
@ComponentScan({ "com.sparkinance"})
public class App {

    public static void main(String[] args) {

        new FinanceController(new SalaryService());

    }
}
