package com.cliffex.Fixezi.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Rating {
    @SerializedName("punctual")
    @Expose
    private String punctual;
    @SerializedName("workmanship")
    @Expose
    private String workmanship;
    @SerializedName("affordability")
    @Expose
    private String affordability;

    public String getPunctual() {
        return punctual;
    }

    public void setPunctual(String punctual) {
        this.punctual = punctual;
    }

    public String getWorkmanship() {
        return workmanship;
    }

    public void setWorkmanship(String workmanship) {
        this.workmanship = workmanship;
    }

    public String getAffordability() {
        return affordability;
    }

    public void setAffordability(String affordability) {
        this.affordability = affordability;
    }
}
