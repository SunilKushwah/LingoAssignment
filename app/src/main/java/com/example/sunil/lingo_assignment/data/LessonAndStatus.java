package com.example.sunil.lingo_assignment.data;

public class LessonAndStatus {
    Lesson lesson;
    boolean isCompleted;

    public LessonAndStatus(Lesson lesson, boolean isCompleted) {
        this.lesson = lesson;
        this.isCompleted = isCompleted;
    }

    public Lesson getLesson() {
        return lesson;
    }

    public void setLesson(Lesson lesson) {
        this.lesson = lesson;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }
}
