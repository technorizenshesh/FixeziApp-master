package com.cliffex.Fixezi.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class  Problem {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("IsDateFlexible")
    @Expose
    private String isDateFlexible;
    @SerializedName("IsTimeFlexible")
    @Expose
    private String isTimeFlexible;
    @SerializedName("PersonOnSite")
    @Expose
    private String personOnSite;
    @SerializedName("Job_Address")
    @Expose
    private String jobAddress;
    @SerializedName("Home_Number")
    @Expose
    private String homeNumber;
    @SerializedName("housenoo")
    @Expose
    private String housenoo;
    @SerializedName("Mobile_Number")
    @Expose
    private String mobileNumber;
    @SerializedName("Job_Request")
    @Expose
    private String jobRequest;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("Tradesman_id")
    @Expose
    private String tradesmanId;
    @SerializedName("problem")
    @Expose
    private String problem;
    @SerializedName("order_status")
    @Expose
    private String orderStatus;
    @SerializedName("schedule")
    @Expose
    private String schedule;
    @SerializedName("noti_status")
    @Expose
    private String notiStatus;
    @SerializedName("IsTimeFlexible_value")
    @Expose
    private String isTimeFlexibleValue;
    @SerializedName("reason")
    @Expose
    private String reason;
    @SerializedName("street")
    @Expose
    private String street;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("sub_status")
    @Expose
    private String subStatus;
    @SerializedName("rate_by_tradesman")
    @Expose
    private String rateByTradesman;
    @SerializedName("date_time")
    @Expose
    private String dateTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getIsDateFlexible() {
        return isDateFlexible;
    }

    public void setIsDateFlexible(String isDateFlexible) {
        this.isDateFlexible = isDateFlexible;
    }

    public String getIsTimeFlexible() {
        return isTimeFlexible;
    }

    public void setIsTimeFlexible(String isTimeFlexible) {
        this.isTimeFlexible = isTimeFlexible;
    }

    public String getPersonOnSite() {
        return personOnSite;
    }

    public void setPersonOnSite(String personOnSite) {
        this.personOnSite = personOnSite;
    }

    public String getJobAddress() {
        return jobAddress;
    }

    public void setJobAddress(String jobAddress) {
        this.jobAddress = jobAddress;
    }

    public String getHomeNumber() {
        return homeNumber;
    }

    public void setHomeNumber(String homeNumber) {
        this.homeNumber = homeNumber;
    }

    public String getHousenoo() {
        return housenoo;
    }

    public void setHousenoo(String housenoo) {
        this.housenoo = housenoo;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getJobRequest() {
        return jobRequest;
    }

    public void setJobRequest(String jobRequest) {
        this.jobRequest = jobRequest;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTradesmanId() {
        return tradesmanId;
    }

    public void setTradesmanId(String tradesmanId) {
        this.tradesmanId = tradesmanId;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public String getNotiStatus() {
        return notiStatus;
    }

    public void setNotiStatus(String notiStatus) {
        this.notiStatus = notiStatus;
    }

    public String getIsTimeFlexibleValue() {
        return isTimeFlexibleValue;
    }

    public void setIsTimeFlexibleValue(String isTimeFlexibleValue) {
        this.isTimeFlexibleValue = isTimeFlexibleValue;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getSubStatus() {
        return subStatus;
    }

    public void setSubStatus(String subStatus) {
        this.subStatus = subStatus;
    }

    public String getRateByTradesman() {
        return rateByTradesman;
    }

    public void setRateByTradesman(String rateByTradesman) {
        this.rateByTradesman = rateByTradesman;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}
