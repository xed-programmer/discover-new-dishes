package com.casestudy.discovernewdishes.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

class Instruction {
    @SerializedName("step")
    private String[] steps;

    public String[] getSteps() {
        return steps;
    }

}
