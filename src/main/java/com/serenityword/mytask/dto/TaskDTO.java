package com.serenityword.mytask.dto;

import java.util.Date;

public class TaskDTO {
    private String title;
    private String description;
    private Date dateTask;

    // Constructeur, getters et setters

    public TaskDTO(String title, String description, Date dateTask) {
        this.title = title;
        this.description = description;
        this.dateTask = dateTask;
    }

    public TaskDTO() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDateTask() {
        return dateTask;
    }

    public void setDateTask(Date dateTask) {
        this.dateTask = dateTask;
    }
}
