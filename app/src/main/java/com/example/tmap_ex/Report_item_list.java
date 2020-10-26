package com.example.tmap_ex;

import android.graphics.drawable.Drawable;

public class Report_item_list {
    // DB 사고 item list
    private Drawable category_image;
    private String report_category;
    private String report_location;
    private String report_date;
    private String report_state;

    public Drawable getCategory_image() { return this.category_image; }

    public void setCategory_image(Drawable image) {
        this.category_image = image;
    }

    public String getReport_category() {
        return report_category;
    }

    public void setReport_category(String report_category) { this.report_category = report_category; }

    public String getReport_location() {
        return report_location;
    }

    public void setReport_location(String report_location) { this.report_location = report_location; }

    public String getReport_date() { return report_date; }

    public void setReport_date(String report_date) {
        this.report_date = report_date;
    }

    public String getReport_state() {
        return report_state;
    }

    public void setReport_state(String report_state) {
        this.report_state = report_state;
    }

    public Report_item_list(Drawable category_image, String report_category, String report_location, String report_date, String report_state) {
        this.category_image = category_image;
        this.report_category = report_category;
        this.report_location = report_location;
        this.report_date = report_date;
        this.report_state = report_state;
    }
}

