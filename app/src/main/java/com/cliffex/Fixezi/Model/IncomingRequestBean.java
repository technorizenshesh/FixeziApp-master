package com.cliffex.Fixezi.Model;

/**
 * Created by ritesh on 23/9/16.
 */
public class IncomingRequestBean {

    private String id;
    private String name;
    private String first_name;
    private String last_name;
    private String post_code;
    private String city;
    private String state;
    private String home_phone;
    private String work_phone;
    private String mobile_phone;
    private String email;
    private String tradesman;
    private String username;
    private String street;
    private String housenoo;
    private String home_address;
    private Problem problem;
    private Rating rating;
    private String status;
    private String job_cancellations;
    private String quotes_accepted_declined;
    private String tradesmanName;
    private String after_hours;

    public String getAfter_hours() {
        return after_hours;
    }

    public void setAfter_hours(String after_hours) {
        this.after_hours = after_hours;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getHome_address() {
        return home_address;
    }

    public void setHome_address(String home_address) {
        this.home_address = home_address;
    }

    public String getJob_cancellations() {
        return job_cancellations;
    }

    public void setJob_cancellations(String job_cancellations) {
        this.job_cancellations = job_cancellations;
    }

    public String getQuotes_accepted_declined() {
        return quotes_accepted_declined;
    }

    public void setQuotes_accepted_declined(String quotes_accepted_declined) {
        this.quotes_accepted_declined = quotes_accepted_declined;
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

    public String getPost_code() {
        return post_code;
    }

    public void setPost_code(String post_code) {
        this.post_code = post_code;
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

    public String getHome_phone() {
        return home_phone;
    }

    public void setHome_phone(String home_phone) {
        this.home_phone = home_phone;
    }

    public String getWork_phone() {
        return work_phone;
    }

    public void setWork_phone(String work_phone) {
        this.work_phone = work_phone;
    }

    public String getMobile_phone() {
        return mobile_phone;
    }

    public void setMobile_phone(String mobile_phone) {
        this.mobile_phone = mobile_phone;
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

    public Problem getProblem() {
        return problem;
    }

    public void setProblem(Problem problem) {
        this.problem = problem;
    }

    public Rating getRating() {
        return rating;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }

    public void setTradesmanName(String tradesmanName) {

        this.tradesmanName = tradesmanName;
    }

    public String getTradesmanName() {
        return tradesmanName;
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
        private String problem;
        private String Status;
        private String home_address;
        private String sub_status;
        private String Schedule;
        private String IsTimeFlexible_value;
        private String housenoo;
        private String street;
        private String r_status;
        private String r_id;
        private String city;
        private String reason;
        private String order_status;
        private String r_Id_status;

        public String getR_id() {
            return r_id;
        }

        public void setR_id(String r_id) {
            this.r_id = r_id;
        }

        public String getHome_address() {
            return home_address;
        }

        public void setHome_address(String home_address) {
            this.home_address = home_address;
        }

        public String getR_Id_status() {
            return r_Id_status;
        }

        public void setR_Id_status(String r_Id_status) {
            this.r_Id_status = r_Id_status;
        }

        public String getR_status() {
            return r_status;
        }

        public void setR_status(String r_status) {
            this.r_status = r_status;
        }

        public String getOrder_status() {
            return order_status;
        }

        public void setOrder_status(String order_status) {
            this.order_status = order_status;
        }

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
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
            return Schedule;
        }

        public void setSchedule(String schedule) {
            Schedule = schedule;
        }

        public String getStatus() {
            return Status;
        }

        public void setStatus(String status) {
            Status = status;
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

    public static class Rating {
        private String ease_of_job;
        private String easily_contacted;
        private String payment_on_completion;

        public String getEase_of_job() {
            return ease_of_job;
        }

        public void setEase_of_job(String ease_of_job) {
            this.ease_of_job = ease_of_job;
        }

        public String getEasily_contacted() {
            return easily_contacted;
        }

        public void setEasily_contacted(String easily_contacted) {
            this.easily_contacted = easily_contacted;
        }

        public String getPayment_on_completion() {
            return payment_on_completion;
        }

        public void setPayment_on_completion(String payment_on_completion) {
            this.payment_on_completion = payment_on_completion;
        }
    }

}
