package com.cliffex.Fixezi.Model;

/**
 * Created by ritesh on 23/9/16.
 */
public class PendingRequestBean {

    private String id;
    private String business_name;
    private String trading_name;
    private String office_no;
    private String mobile_no;
    private String email;
    private String website_url;
    private String after_hours;
    private String select_trade;
    private Problem problem;





    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBusiness_name() {
        return business_name;
    }

    public void setBusiness_name(String business_name) {
        this.business_name = business_name;
    }

    public String getTrading_name() {
        return trading_name;
    }

    public void setTrading_name(String trading_name) {
        this.trading_name = trading_name;
    }

    public String getOffice_no() {
        return office_no;
    }

    public void setOffice_no(String office_no) {
        this.office_no = office_no;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    public String getWebsite_url() {
        return website_url;
    }

    public void setWebsite_url(String website_url) {
        this.website_url = website_url;
    }

    public String getAfter_hours() {
        return after_hours;
    }

    public void setAfter_hours(String after_hours) {
        this.after_hours = after_hours;
    }

    public String getSelect_trade() {
        return select_trade;
    }

    public void setSelect_trade(String select_trade) {
        this.select_trade = select_trade;
    }



    public Problem getProblem() {
        return problem;
    }

    public void setProblem(Problem problem) {
        this.problem = problem;
    }

    public static class Problem {


        private String id;
        private String date;
        private String time;
        private String IsDateFlexible;
        private String IsTimeFlexible;
        private String PersonOnSite;
        private String Job_Address;
        private String Home_Number;
        private String Mobile_Number;
        private String Job_Request;
        private String user_id;
        private String Tradesman_id;
        private String problem;
        private String order_status;
        private String sub_status;
        private String schedule;
        private String IsTimeFlexible_value;
        private String reason;
        private String housenoo;
        private String street;
        private String city;

        public String getHousenoo() {
            return housenoo;
        }

        public void setHousenoo(String housenoo) {
            this.housenoo = housenoo;
        }

        public String getStreet() {
            return street;
        }

        public void setStreet(String street) {
            this.street = street;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }

        public String getSub_status() {
            return sub_status;
        }

        public void setSub_status(String sub_status) {
            this.sub_status = sub_status;
        }

        public String getIsTimeFlexible_value() {
            return IsTimeFlexible_value;
        }

        public void setIsTimeFlexible_value(String isTimeFlexible_value) {
            IsTimeFlexible_value = isTimeFlexible_value;
        }

        public String getSchedule() {
            return schedule;
        }

        public void setSchedule(String schedule) {
            this.schedule = schedule;
        }

        public String getOrder_status() {
            return order_status;
        }

        public void setOrder_status(String order_status) {
            this.order_status = order_status;
        }

        public String getTradesman_id() {
            return Tradesman_id;
        }

        public void setTradesman_id(String tradesman_id) {
            Tradesman_id = tradesman_id;
        }

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
            return IsDateFlexible;
        }

        public void setIsDateFlexible(String isDateFlexible) {
            IsDateFlexible = isDateFlexible;
        }

        public String getIsTimeFlexible() {
            return IsTimeFlexible;
        }

        public void setIsTimeFlexible(String isTimeFlexible) {
            IsTimeFlexible = isTimeFlexible;
        }

        public String getPersonOnSite() {
            return PersonOnSite;
        }

        public void setPersonOnSite(String personOnSite) {
            PersonOnSite = personOnSite;
        }

        public String getJob_Address() {
            return Job_Address;
        }

        public void setJob_Address(String job_Address) {
            Job_Address = job_Address;
        }

        public String getHome_Number() {
            return Home_Number;
        }

        public void setHome_Number(String home_Number) {
            Home_Number = home_Number;
        }

        public String getMobile_Number() {
            return Mobile_Number;
        }

        public void setMobile_Number(String mobile_Number) {
            Mobile_Number = mobile_Number;
        }

        public String getJob_Request() {
            return Job_Request;
        }

        public void setJob_Request(String job_Request) {
            Job_Request = job_Request;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getProblem() {
            return problem;
        }

        public void setProblem(String problem) {
            this.problem = problem;
        }
    }


}
