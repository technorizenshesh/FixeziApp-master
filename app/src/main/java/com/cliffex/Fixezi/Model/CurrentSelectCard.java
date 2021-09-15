package com.cliffex.Fixezi.Model;

public class CurrentSelectCard {

    /**
     * result : successfully
     * cust_id : cus_IgXeTuTN4jcXaa
     * card_id : card_1I5AZLACrbcTjoAni3LHf8Sz
     * plan_type : PayPerJob
     * status : 1
     * message : successfull
     */

    private String result;
    private String cust_id;
    private String card_id;
    private String plan_type;
    private int status;
    private String message;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getCust_id() {
        return cust_id;
    }

    public void setCust_id(String cust_id) {
        this.cust_id = cust_id;
    }

    public String getCard_id() {
        return card_id;
    }

    public void setCard_id(String card_id) {
        this.card_id = card_id;
    }

    public String getPlan_type() {
        return plan_type;
    }

    public void setPlan_type(String plan_type) {
        this.plan_type = plan_type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
