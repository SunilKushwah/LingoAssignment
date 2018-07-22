package com.example.sunil.lingo_assignment.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LessonList {

    @SerializedName("lesson_data")
    @Expose
    private List<Lesson> lessons = null;

    /**
     * No args constructor for use in serialization
     *
     */
    public LessonList() {
    }

    /**
     *
     * @param lessons
     */
    public LessonList(List<Lesson> lessons) {
        super();
        this.lessons = lessons;
    }

    public List<Lesson> getLessons() {
        return lessons;
    }

    public void setLessons(List<Lesson> lessons) {
        this.lessons = lessons;
    }

}