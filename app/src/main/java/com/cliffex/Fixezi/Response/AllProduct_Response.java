package com.cliffex.Fixezi.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AllProduct_Response {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("post_code")
    @Expose
    private String postCode;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("home_phone")
    @Expose
    private String homePhone;
    @SerializedName("work_phone")
    @Expose
    private String workPhone;
    @SerializedName("mobile_phone")
    @Expose
    private String mobilePhone;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("tradesman")
    @Expose
    private String tradesman;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("street")
    @Expose
    private String street;
    @SerializedName("housenoo")
    @Expose
    private String housenoo;
    @SerializedName("person_site")
    @Expose
    private String personSite;
    @SerializedName("home_no")
    @Expose
    private String homeNo;
    @SerializedName("registration_id")
    @Expose
    private String registrationId;
    @SerializedName("ios_registration_id")
    @Expose
    private String iosRegistrationId;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("home_address")
    @Expose
    private String homeAddress;
    @SerializedName("user_image")
    @Expose
    private String userImage;
    @SerializedName("date_time")
    @Expose
    private String dateTime;
    @SerializedName("image_status")
    @Expose
    private String imageStatus;
    @SerializedName("forword")
    @Expose
    private List<Object> forword = null;
    @SerializedName("problem")
    @Expose
    private List<Problem> problem = null;

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getHomePhone() {
        return homePhone;
    }

    public void setHomePhone(String homePhone) {
        this.homePhone = homePhone;
    }

    public String getWorkPhone() {
        return workPhone;
    }

    public void setWorkPhone(String workPhone) {
        this.workPhone = workPhone;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTradesman() {
        return tradesman;
    }

    public void setTradesman(String tradesman) {
        this.tradesman = tradesman;
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

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getHousenoo() {
        return housenoo;
    }

    public void setHousenoo(String housenoo) {
        this.housenoo = housenoo;
    }

    public String getPersonSite() {
        return personSite;
    }

    public void setPersonSite(String personSite) {
        this.personSite = personSite;
    }

    public String getHomeNo() {
        return homeNo;
    }

    public void setHomeNo(String homeNo) {
        this.homeNo = homeNo;
    }

    public String getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(String registrationId) {
        this.registrationId = registrationId;
    }

    public String getIosRegistrationId() {
        return iosRegistrationId;
    }

    public void setIosRegistrationId(String iosRegistrationId) {
        this.iosRegistrationId = iosRegistrationId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(String homeAddress) {
        this.homeAddress = homeAddress;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getImageStatus() {
        return imageStatus;
    }

    public void setImageStatus(String imageStatus) {
        this.imageStatus = imageStatus;
    }

    public List<Object> getForword() {
        return forword;
    }

    public void setForword(List<Object> forword) {
        this.forword = forword;
    }

    public List<Problem> getProblem() {
        return problem;
    }

    public void setProblem(List<Problem> problem) {
        this.problem = problem;
    }

}
