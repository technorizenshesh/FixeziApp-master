package com.cliffex.Fixezi.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Rating_Income {
    @SerializedName("easily_contacted")
    @Expose
    private String easilyContacted;
    @SerializedName("ease_of_job")
    @Expose
    private String easeOfJob;
    @SerializedName("payment_on_completion")
    @Expose
    private String paymentOnCompletion;

    public String getEasilyContacted() {
        return easilyContacted;
    }

    public void setEasilyContacted(String easilyContacted) {
        this.easilyContacted = easilyContacted;
    }

    public String getEaseOfJob() {
        return easeOfJob;
    }

    public void setEaseOfJob(String easeOfJob) {
        this.easeOfJob = easeOfJob;
    }

    public String getPaymentOnCompletion() {
        return paymentOnCompletion;
    }

    public void setPaymentOnCompletion(String paymentOnCompletion) {
        this.paymentOnCompletion = paymentOnCompletion;
    }
}
