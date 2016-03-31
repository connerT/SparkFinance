package com.sparkfinance.service.impl;


import com.sparkfinance.util.StateConstants;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Iterator;


public class StateCalculator extends TaxCalculator {

    private JSONParser parser = new JSONParser();
    private StateConstants sc = new StateConstants();
    private StateResponse response =  new StateResponse();

    public StateResponse getStateData(String year, String stateAbbr) {

        if (!Arrays.asList(SUPPORTED_YEARS).contains(year)) {
            response.setSuccess(false);
            response.setReason("Invalid year");
        } else if (StringUtils.isBlank(stateAbbr)) {
            response.setSuccess(false);
            response.setReason("Invalid state");
        } else {
            String state = StringUtils.lowerCase(sc.abbreviationToFullName(stateAbbr));
            response.setStateName(state);
            state = StringUtils.replace(state, " ", "_");

            try {
                String fileName = state + ".json";
                ClassLoader classLoader = getClass().getClassLoader();
                File file = new File(classLoader.getResource("tax_tables/" + year + "/" + fileName).getFile());
                Object obj = parser.parse(new FileReader(file.getPath()));
                response.setSuccess(true);
                response.setData(obj);
            } catch (Exception ex) {
                response.setSuccess(false);
                response.setReason(ex.getMessage());
                System.out.println("Could not find json file for state: " + state);
            }
        }

        return response;

        }

    public void calculate(String year, BigDecimal payRate, int payPeriods, String filingStatus, String state) {
        BigDecimal income = payRate.multiply(BigDecimal.valueOf(payPeriods));
        BigDecimal stateTaxAmount = getStateTaxAmount(year, income, filingStatus, state);

    }

    private JSONObject getDataFromFilingStatus(JSONObject data, String filingStatus) {
        JSONObject targetTable = (JSONObject) data.get(filingStatus);
        return targetTable;
    }

    public BigDecimal getStateTaxAmount(String year, BigDecimal income, String filingStatus, String stateAbbr) {
        StateResponse sr = getStateData(year, stateAbbr);
        JSONObject jobj = (JSONObject) sr.getData();
        JSONObject targetTable = getDataFromFilingStatus(jobj, filingStatus);
        BigDecimal amount = BigDecimal.ZERO;
        BigDecimal deductionAmount = BigDecimal.ZERO;

        JSONArray deductions = (JSONArray) targetTable.get("deductions");
        if (deductions != null) {
            Iterator i = deductions.iterator();
            while (i.hasNext()) {
                JSONObject deduction = (JSONObject) i.next();
                Long stateDeductionAmount = (Long) deduction.get("deduction_amount");
                deductionAmount.add(new BigDecimal(stateDeductionAmount));
                System.out.println("The deduction amount is now: " + deductionAmount);
            }
        }

        // Not implemented yet
        BigDecimal excemptionAmount = BigDecimal.ZERO;
        JSONArray excemptions = (JSONArray) targetTable.get("excemptions");
        if (excemptions != null) {
            //$exemptions = $target_table->exemptions->personal;
        }

        BigDecimal adjustedIncome = income.subtract(deductionAmount).subtract(excemptionAmount);
        if (adjustedIncome.compareTo(BigDecimal.ZERO) < 0) {
            adjustedIncome = BigDecimal.ZERO;
        }

        String type = (String) targetTable.get("type");
        if (StringUtils.isNotBlank(type)
                && (StringUtils.equalsIgnoreCase("none", type)
                || StringUtils.equalsIgnoreCase(type, "TODO UNKNOWN!"))) {
                return amount;
        } else {
            JSONArray brackets = (JSONArray) targetTable.get("income_tax_brackets");
            for (int j = 0 ; j < brackets.size(); j++) {
                JSONObject taxBracket = (JSONObject) brackets.get(j);
                JSONObject nextTaxBracket = (JSONObject) brackets.get(j + 1);
                Long longBracketAmount = (Long) taxBracket.get("bracket");
                BigDecimal bracketAmount = new BigDecimal(longBracketAmount);
                Long longNextBracketAmount = (Long) nextTaxBracket.get("bracket");
                BigDecimal nextBracketAmount = new BigDecimal(longNextBracketAmount);
                double val = ((Number)taxBracket.get("marginal_rate")).doubleValue();
                BigDecimal marginalRate = new BigDecimal(val);

                if (j == (brackets.size() - 1)) {
                    BigDecimal adjIncomeMinuBracketAmt = adjustedIncome.subtract(bracketAmount);
                    BigDecimal marginalRateDecimal = marginalRate.divide(new BigDecimal("100.00"));
                    BigDecimal multiplier = adjIncomeMinuBracketAmt.multiply(marginalRateDecimal);
                    amount = amount.add(multiplier);
                } else if ((adjustedIncome.compareTo(nextBracketAmount) < 0)) {
                    BigDecimal adjIncomeMinuBracketAmt = adjustedIncome.subtract(bracketAmount);
                    BigDecimal marginalRateDecimal = marginalRate.divide(new BigDecimal("100.00"));
                    BigDecimal multiplier = adjIncomeMinuBracketAmt.multiply(marginalRateDecimal);
                    amount = amount.add(multiplier);
                    break;
                } else {
                    BigDecimal bracketSubtract = nextBracketAmount.subtract(bracketAmount);
                    BigDecimal marginalRateDecimal = marginalRate.divide(new BigDecimal("100.00"));
                    BigDecimal multiplier = bracketSubtract.multiply(marginalRateDecimal);
                    amount =  amount.add(multiplier);
                }
            }
        }

        amount = amount.setScale(2, RoundingMode.CEILING);
        return amount;
    }

}
