package com.casestudy.discovernewdishes.Models;

import java.util.Date;

public class FoodSchedule {
    private int id;
    private String recipe_id;
    private String date_sched;
    private String api;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRecipe_id() {
        return recipe_id;
    }

    public void setRecipe_id(String recipe_id) {
        this.recipe_id = recipe_id;
    }

    public String getDate_sched() {
        return date_sched;
    }

    public void setDate_sched(String date_sched) {
        this.date_sched = date_sched;
    }

    public String getApi() {
        return api;
    }

    public void setApi(String api) {
        this.api = api;
    }
}
