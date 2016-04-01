package com.sparkfinance.controller;

import com.sparkfinance.model.SalaryResults;
import com.sparkfinance.service.impl.SalaryService;
import com.sparkfinance.util.ResponseError;

import java.math.BigDecimal;

import static com.sparkfinance.util.JsonUtil.json;
import static spark.Spark.*;

public class FinanceController {

    public FinanceController(final SalaryService salService) {


        get("/federal/:income", (req, res) -> {
            String income = req.params(":income");
            BigDecimal incomeBD = new BigDecimal(income);
            SalaryResults sr = new SalaryResults();
            sr.setSalary(incomeBD);
            SalaryResults returnSr = salService.getFedTax(sr);
            if (returnSr != null) {
                return returnSr;
            }
            res.status(400);
            return new ResponseError("Something went wrong calculation Federal tax for income of '%s'", income);
        }, json());

        get("/state/:stateAbbr/:income/:filingStatus", (request, response) -> {
            String stateAbbr = request.params(":stateAbbr");
            String income = request.params(":income");
            String filingStatus = request.params(":filingStatus");
            BigDecimal incomeBD = new BigDecimal(income);
            SalaryResults sr = salService.getStateIncomeTax(stateAbbr, incomeBD, filingStatus);
            if (sr != null) {
                return sr;
            }
            response.status(400);
            return new ResponseError("Something went wrong calculation Federal tax for income of '%s'", income);
        }, json());

        get("/:state/:income/:status/:401k/:payPeriod", (request, response) -> {
            String state = request.params(":state");
            String income =  request.params(":income");
            String status =  request.params(":status");
            String fourK = request.params(":401k");
            String payPeriod = request.params(":payPeriod");
            SalaryResults sr = salService.getRealSalary("2014", income, state, fourK, payPeriod, status);
            if (sr != null) {
                return sr;
            }
            response.status(400);
            return new ResponseError("Something went wrong calculation Federal tax for income of '%s'", income);
        }, json());

        after((req, res) -> {
            res.type("application/json");
        });
    }

}
