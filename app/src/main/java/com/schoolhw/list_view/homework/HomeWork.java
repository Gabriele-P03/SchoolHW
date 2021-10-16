package com.schoolhw.list_view.homework;

import com.schoolhw.list_view.subject.Subject;

import java.util.Date;

public class HomeWork {

    private final Subject subject;
    private final Date date;
    private String note;

    public HomeWork(Subject subject, Date date) {
        this(subject, date, "");
    }

    public HomeWork(Subject subject, Date date, String note) {
        this.subject = subject;
        this.date = date;
        this.note = note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Subject getSubject() {
        return subject;
    }



    public Date getDate() {
        return date;
    }

    public String getNote() {
        return note;
    }

    @Override
    public String toString() {
        return this.subject.getSubjectName() + " - " + this.date.toString() + ":\n" + this.note;
    }
}
