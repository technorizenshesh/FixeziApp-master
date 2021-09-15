package com.cliffex.Fixezi.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class JObDetails_Response {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("cust_id")
    @Expose
    private String custId;
    @SerializedName("business_name")
    @Expose
    private String businessName;
    @SerializedName("trading_name")
    @Expose
    private String tradingName;
    @SerializedName("office_no")
    @Expose
    private String officeNo;
    @SerializedName("mobile_no")
    @Expose
    private String mobileNo;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("website_url")
    @Expose
    private String websiteUrl;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("after_hours")
    @Expose
    private String afterHours;
    @SerializedName("select_trade")
    @Expose
    private String selectTrade;
    @SerializedName("license_pic")
    @Expose
    private String licensePic;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("work")
    @Expose
    private String work;
    @SerializedName("profile_pic")
    @Expose
    private String profilePic;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("bzy_status")
    @Expose
    private String bzyStatus;
    @SerializedName("work_location")
    @Expose
    private String workLocation;
    @SerializedName("registration_id")
    @Expose
    private String registrationId;
    @SerializedName("company_detail_upload")
    @Expose
    private String companyDetailUpload;
    @SerializedName("company_detail")
    @Expose
    private String companyDetail;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("company_profile_ratio")
    @Expose
    private String companyProfileRatio;
    @SerializedName("company_upload_ratio")
    @Expose
    private String companyUploadRatio;
    @SerializedName("employee_count")
    @Expose
    private String employeeCount;
    @SerializedName("hour_min")
    @Expose
    private String hourMin;
    @SerializedName("hour_max")
    @Expose
    private String hourMax;
    @SerializedName("contractor_img")
    @Expose
    private String contractorImg;
    @SerializedName("businessAddress")
    @Expose
    private String businessAddress;
    @SerializedName("businessOwnerrnName")
    @Expose
    private String businessOwnerrnName;
    @SerializedName("ios_registration_id")
    @Expose
    private String iosRegistrationId;
    @SerializedName("plan_type")
    @Expose
    private String planType;
    @SerializedName("exp_date")
    @Expose
    private String expDate;
    @SerializedName("job_cancellations")
    @Expose
    private String jobCancellations;
    @SerializedName("quotes_accepted_declined")
    @Expose
    private String quotesAcceptedDeclined;
    @SerializedName("rating")
    @Expose
    private Rating_job rating;
    @SerializedName("exp_status")
    @Expose
    private String expStatus;
    @SerializedName("problem")
    @Expose
    private List<Problem> problem = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getTradingName() {
        return tradingName;
    }

    public void setTradingName(String tradingName) {
        this.tradingName = tradingName;
    }

    public String getOfficeNo() {
        return officeNo;
    }

    public void setOfficeNo(String officeNo) {
        this.officeNo = officeNo;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getAfterHours() {
        return afterHours;
    }

    public void setAfterHours(String afterHours) {
        this.afterHours = afterHours;
    }

    public String getSelectTrade() {
        return selectTrade;
    }

    public void setSelectTrade(String selectTrade) {
        this.selectTrade = selectTrade;
    }

    public String getLicensePic() {
        return licensePic;
    }

    public void setLicensePic(String licensePic) {
        this.licensePic = licensePic;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getWork() {
        return work;
    }

    public void setWork(String work) {
        this.work = work;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
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

    public String getBzyStatus() {
        return bzyStatus;
    }

    public void setBzyStatus(String bzyStatus) {
        this.bzyStatus = bzyStatus;
    }

    public String getWorkLocation() {
        return workLocation;
    }

    public void setWorkLocation(String workLocation) {
        this.workLocation = workLocation;
    }

    public String getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(String registrationId) {
        this.registrationId = registrationId;
    }

    public String getCompanyDetailUpload() {
        return companyDetailUpload;
    }

    public void setCompanyDetailUpload(String companyDetailUpload) {
        this.companyDetailUpload = companyDetailUpload;
    }

    public String getCompanyDetail() {
        return companyDetail;
    }

    public void setCompanyDetail(String companyDetail) {
        this.companyDetail = companyDetail;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCompanyProfileRatio() {
        return companyProfileRatio;
    }

    public void setCompanyProfileRatio(String companyProfileRatio) {
        this.companyProfileRatio = companyProfileRatio;
    }

    public String getCompanyUploadRatio() {
        return companyUploadRatio;
    }

    public void setCompanyUploadRatio(String companyUploadRatio) {
        this.companyUploadRatio = companyUploadRatio;
    }

    public String getEmployeeCount() {
        return employeeCount;
    }

    public void setEmployeeCount(String employeeCount) {
        this.employeeCount = employeeCount;
    }

    public String getHourMin() {
        return hourMin;
    }

    public void setHourMin(String hourMin) {
        this.hourMin = hourMin;
    }

    public String getHourMax() {
        return hourMax;
    }

    public void setHourMax(String hourMax) {
        this.hourMax = hourMax;
    }

    public String getContractorImg() {
        return contractorImg;
    }

    public void setContractorImg(String contractorImg) {
        this.contractorImg = contractorImg;
    }

    public String getBusinessAddress() {
        return businessAddress;
    }

    public void setBusinessAddress(String businessAddress) {
        this.businessAddress = businessAddress;
    }

    public String getBusinessOwnerrnName() {
        return businessOwnerrnName;
    }

    public void setBusinessOwnerrnName(String businessOwnerrnName) {
        this.businessOwnerrnName = businessOwnerrnName;
    }

    public String getIosRegistrationId() {
        return iosRegistrationId;
    }

    public void setIosRegistrationId(String iosRegistrationId) {
        this.iosRegistrationId = iosRegistrationId;
    }

    public String getPlanType() {
        return planType;
    }

    public void setPlanType(String planType) {
        this.planType = planType;
    }

    public String getExpDate() {
        return expDate;
    }

    public void setExpDate(String expDate) {
        this.expDate = expDate;
    }

    public String getJobCancellations() {
        return jobCancellations;
    }

    public void setJobCancellations(String jobCancellations) {
        this.jobCancellations = jobCancellations;
    }

    public String getQuotesAcceptedDeclined() {
        return quotesAcceptedDeclined;
    }

    public void setQuotesAcceptedDeclined(String quotesAcceptedDeclined) {
        this.quotesAcceptedDeclined = quotesAcceptedDeclined;
    }

    public Rating_job getRating() {
        return rating;
    }

    public void setRating(Rating_job rating) {
        this.rating = rating;
    }

    public String getExpStatus() {
        return expStatus;
    }

    public void setExpStatus(String expStatus) {
        this.expStatus = expStatus;
    }

    public List<Problem> getProblem() {
        return problem;
    }

    public void setProblem(List<Problem> problem) {
        this.problem = problem;
    }
}
