package com.youdy.mo.mochart.activity;

import com.youdy.mo.mochart.model.Course;

/**
 * Created by mo on 2017/6/22.
 */

public class TestData {
    public static Course[] newData(Course[] data){
        Course c1 = new Course(0,"发送扽好似","A203","周树人",2,new int[]{2,4});
        Course c2 = new Course(0,"发送扽好似","A203","周树人",2,new int[]{6,6});
        data[0] = c1;
        data[1] = c2;
        return data;
    }
}
