package com.cliffex.Fixezi.Model;

import java.io.Serializable;

public class ModelEmployeesTrade implements Serializable {

    private String id;

    private String name;

    private String username;

    private String password;

    private String tradesman_id;

    private String device_token;

    private String ios_registration_id;

    private int problem_count;

    private String result;

    public void setId(String id){
        this.id = id;
    }
    public String getId(){
        return this.id;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return this.name;
    }
    public void setUsername(String username){
        this.username = username;
    }
    public String getUsername(){
        return this.username;
    }
    public void setPassword(String password){
        this.password = password;
    }
    public String getPassword(){
        return this.password;
    }
    public void setTradesman_id(String tradesman_id){
        this.tradesman_id = tradesman_id;
    }
    public String getTradesman_id(){
        return this.tradesman_id;
    }
    public void setDevice_token(String device_token){
        this.device_token = device_token;
    }
    public String getDevice_token(){
        return this.device_token;
    }
    public void setIos_registration_id(String ios_registration_id){
        this.ios_registration_id = ios_registration_id;
    }
    public String getIos_registration_id(){
        return this.ios_registration_id;
    }
    public void setProblem_count(int problem_count){
        this.problem_count = problem_count;
    }
    public int getProblem_count(){
        return this.problem_count;
    }
    public void setResult(String result){
        this.result = result;
    }
    public String getResult(){
        return this.result;
    }

}
