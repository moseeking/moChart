package com.youdy.mo.mochart.model;

/**
 * Created by mo on 2017/6/18.
 */

public class Course{
    private int id;
    private String name;
    private String place;
    private String teacher;
    private int week;
    private int[] time;
    private int[] weekLast;
    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public Course(int id,String name,String place,String teacher,int week,int[] time){
        this.id  = id;
        this.place = place;
        this.teacher = teacher;
        this.name = name;
        this.week = week;
        this.time = time;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getPlace() {
        return place;
    }
    public void setPlace(String place) {
        this.place = place;
    }

    public int[] getTime() {
        return time;
    }
    public void setTime(int[] time) {
        this.time = time;
    }

    public String getTeacher() {
        return teacher;
    }
    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }


    public int[] getWeekLast() {
        return weekLast;
    }

    public void setWeekLast(int[] weekLast) {
        this.weekLast = weekLast;
    }

}
