package com.cliffex.Fixezi.Model;


public class WorkerUserList {


    private String id;

    private String name;

    private String username;

    private String password;

    private String tradesmanId;

    private String deviceToken;

    private String iosRegistrationId;

    private Integer problemCount;

    private String result;

    public WorkerUserList(String id, String name, String username, String password, String tradesmanId, String deviceToken, String iosRegistrationId, Integer problemCount, String result) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.password = password;
        this.tradesmanId = tradesmanId;
        this.deviceToken = deviceToken;
        this.iosRegistrationId = iosRegistrationId;
        this.problemCount = problemCount;
        this.result = result;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTradesmanId() {
        return tradesmanId;
    }

    public void setTradesmanId(String tradesmanId) {
        this.tradesmanId = tradesmanId;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getIosRegistrationId() {
        return iosRegistrationId;
    }

    public void setIosRegistrationId(String iosRegistrationId) {
        this.iosRegistrationId = iosRegistrationId;
    }

    public Integer getProblemCount() {
        return problemCount;
    }

    public void setProblemCount(Integer problemCount) {
        this.problemCount = problemCount;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

}

