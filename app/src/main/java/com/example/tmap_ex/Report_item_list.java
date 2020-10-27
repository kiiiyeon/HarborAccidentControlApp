package com.example.tmap_ex;

import android.graphics.drawable.Drawable;

public class Report_item_list {
    // DB 사고 item list
    private Drawable category_image;
    private String report_category;
    private String report_location;
    private String report_date;
    private String report_state;
    private String resolved_date;

    public Drawable getCategory_image() { return category_image; }

    public void setCategory_image(Drawable category_image) {
        this.category_image = category_image;
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

    public Report_item_list(Drawable category_image, String report_category, String report_location, String report_date, String report_state, String resolved_date) {
        this.category_image = category_image;
        this.report_category = report_category;
        this.report_location = report_location;
        this.report_date = report_date;
        this.report_state = report_state;
        this.resolved_date = resolved_date;
    }

    public String getResolved_date() {
        return resolved_date;
    }

    public void setResolved_date(String resolved_date) {
        this.resolved_date = resolved_date;
    }
}

