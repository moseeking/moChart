package com.youdy.mo.mochart.adapter;


import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.youdy.mo.mochart.R;
import com.youdy.mo.mochart.model.ChartView;
import com.youdy.mo.mochart.model.Course;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CustomRecyclerViewAdapter extends PagerAdapter {
    private Context mContext;
    private List<Course>[] data;
    private Date date;
    private List<String> mItems = new ArrayList<>();

    public CustomRecyclerViewAdapter(Context mContext,List<Course>[] data,Date date) {
        this.mContext = mContext;
        this.data = data;
        this.date = date;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.activity_main_rv_course,container, false);
        ChartView cv = (ChartView)view.findViewById(R.id.cv_main_page);

        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DAY_OF_MONTH, 7 * position);
        Date d = c.getTime();
        cv.initChartView(d,data[position]);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public String getPageTitle(int position) {
        return mItems.get(position);
    }

    public void addAll(List<String> items) {
        mItems = new ArrayList<>(items);
    }

}