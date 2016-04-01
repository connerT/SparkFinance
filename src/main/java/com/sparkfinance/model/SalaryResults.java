package com.sparkfinance.model;


import java.math.BigDecimal;

public class SalaryResults {

    private BigDecimal federalTax;
    private BigDecimal fedTaxRate;
    private BigDecimal stateTax;
    private BigDecimal realIncome;
    private BigDecimal salary;
    private BigDecimal fourOoneKpercentage;
    private boolean success;
    private String reason;
    private String filingStatus;
    private String state;
    private String year;
    private String timePeriod;

    public BigDecimal getRealIncome() {
        return realIncome;
    }

    public void setRealIncome(BigDecimal realIncome) {
        this.realIncome = realIncome;
    }

    public BigDecimal getStateTax() {
        return stateTax;
    }

    public void setStateTax(BigDecimal stateTax) {
        this.stateTax = stateTax;
    }

    public BigDecimal getFederalTax() {
        return federalTax;
    }

    public void setFederalTax(BigDecimal federalTax) {
        this.federalTax = federalTax;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public BigDecimal getFedTaxRate() {
        return fedTaxRate;
    }

    public void setFedTaxRate(BigDecimal fedTaxRate) {
        this.fedTaxRate = fedTaxRate;
    }

    public String getFilingStatus() {
        return filingStatus;
    }

    public void setFilingStatus(String filingStatus) {
        this.filingStatus = filingStatus;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getTimePeriod() {
        return timePeriod;
    }

    public void setTimePeriod(String timePeriod) {
        this.timePeriod = timePeriod;
    }

    public BigDecimal getFourOoneKpercentage() {
        return fourOoneKpercentage;
    }

    public void setFourOoneKpercentage(BigDecimal fourOoneKpercentage) {
        this.fourOoneKpercentage = fourOoneKpercentage;
    }
}
