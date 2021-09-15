package com.cliffex.Fixezi.Model;

import java.io.Serializable;
import java.util.ArrayList;

public class ModelMyCards implements Serializable {

    private String object;
    private ArrayList<Data> data;
    private boolean has_more;
    private int total_count;
    private String url;

    public void setObject(String object){
        this.object = object;
    }
    public String getObject(){
        return this.object;
    }
    public void setData(ArrayList<Data> data){
        this.data = data;
    }
    public ArrayList<Data> getData(){
        return this.data;
    }
    public void setHas_more(boolean has_more){
        this.has_more = has_more;
    }
    public boolean getHas_more(){
        return this.has_more;
    }
    public void setTotal_count(int total_count){
        this.total_count = total_count;
    }
    public int getTotal_count(){
        return this.total_count;
    }
    public void setUrl(String url){
        this.url = url;
    }
    public String getUrl(){
        return this.url;
    }

    public class Data implements Serializable {

        private String id;

        private String object;

        private String address_city;

        private String address_country;

        private String address_line1;

        private String address_line1_check;

        private String address_line2;

        private String address_state;

        private String address_zip;

        private String address_zip_check;

        private String brand;

        private String country;

        private String customer;

        private String cvc_check;

        private String dynamic_last4;

        private int exp_month;

        private int exp_year;

        private String fingerprint;

        private String funding;

        private String last4;

        private Metadata metadata;

        private String name;

        private String tokenization_method;

        public void setId(String id){
            this.id = id;
        }
        public String getId(){
            return this.id;
        }
        public void setObject(String object){
            this.object = object;
        }
        public String getObject(){
            return this.object;
        }
        public void setAddress_city(String address_city){
            this.address_city = address_city;
        }
        public String getAddress_city(){
            return this.address_city;
        }
        public void setAddress_country(String address_country){
            this.address_country = address_country;
        }
        public String getAddress_country(){
            return this.address_country;
        }
        public void setAddress_line1(String address_line1){
            this.address_line1 = address_line1;
        }
        public String getAddress_line1(){
            return this.address_line1;
        }
        public void setAddress_line1_check(String address_line1_check){
            this.address_line1_check = address_line1_check;
        }
        public String getAddress_line1_check(){
            return this.address_line1_check;
        }
        public void setAddress_line2(String address_line2){
            this.address_line2 = address_line2;
        }
        public String getAddress_line2(){
            return this.address_line2;
        }
        public void setAddress_state(String address_state){
            this.address_state = address_state;
        }
        public String getAddress_state(){
            return this.address_state;
        }
        public void setAddress_zip(String address_zip){
            this.address_zip = address_zip;
        }
        public String getAddress_zip(){
            return this.address_zip;
        }
        public void setAddress_zip_check(String address_zip_check){
            this.address_zip_check = address_zip_check;
        }
        public String getAddress_zip_check(){
            return this.address_zip_check;
        }
        public void setBrand(String brand){
            this.brand = brand;
        }
        public String getBrand(){
            return this.brand;
        }
        public void setCountry(String country){
            this.country = country;
        }
        public String getCountry(){
            return this.country;
        }
        public void setCustomer(String customer){
            this.customer = customer;
        }
        public String getCustomer(){
            return this.customer;
        }
        public void setCvc_check(String cvc_check){
            this.cvc_check = cvc_check;
        }
        public String getCvc_check(){
            return this.cvc_check;
        }
        public void setDynamic_last4(String dynamic_last4){
            this.dynamic_last4 = dynamic_last4;
        }
        public String getDynamic_last4(){
            return this.dynamic_last4;
        }
        public void setExp_month(int exp_month){
            this.exp_month = exp_month;
        }
        public int getExp_month(){
            return this.exp_month;
        }
        public void setExp_year(int exp_year){
            this.exp_year = exp_year;
        }
        public int getExp_year(){
            return this.exp_year;
        }
        public void setFingerprint(String fingerprint){
            this.fingerprint = fingerprint;
        }
        public String getFingerprint(){
            return this.fingerprint;
        }
        public void setFunding(String funding){
            this.funding = funding;
        }
        public String getFunding(){
            return this.funding;
        }
        public void setLast4(String last4){
            this.last4 = last4;
        }
        public String getLast4(){
            return this.last4;
        }
        public void setMetadata(Metadata metadata){
            this.metadata = metadata;
        }
        public Metadata getMetadata(){
            return this.metadata;
        }
        public void setName(String name){
            this.name = name;
        }
        public String getName(){
            return this.name;
        }
        public void setTokenization_method(String tokenization_method){
            this.tokenization_method = tokenization_method;
        }
        public String getTokenization_method(){
            return this.tokenization_method;
        }

        public class Metadata {}
    }

}
