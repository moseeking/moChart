package com.youdy.mo.mochart.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youdy.mo.mochart.R;
import com.youdy.mo.mochart.model.Course;
import com.youdy.mo.mochart.utils.DisplayParams;
import com.youdy.mo.mochart.utils.DisplayUtils;

import java.util.List;

/**
 * Created by mo on 2017/6/18.
 */

public class ChartRecyclerViewAdapter extends RecyclerView.Adapter {
    private static int TYPE_INDEX = 0;
    private static int TYPE_COURSE = 1;

    private Context mContext;
    private int index = 0;
    private List<Course> data;
    public List<Course> getData() {
        return data;
    }

    public void setData(List<Course> data) {
        this.data = data;
    }


    public ChartRecyclerViewAdapter(Context mContext){
        this.mContext = mContext;
        //假数据的数组
        //titles= context.getResources().getStringArray(R.array.recyclerView_data);
    }


    public ChartRecyclerViewAdapter(Context mContext,List<Course> data){
        this.mContext = mContext;
        this.data = data;
        //假数据的数组
        //titles= context.getResources().getStringArray(R.array.recyclerView_data);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        DisplayParams displayParams = DisplayParams.getInstance(mContext);

        if(viewType == TYPE_INDEX) {
            View viewIndex = LayoutInflater.from(mContext).inflate(R.layout.chart_recyclerview_item_index, parent, false);
            ViewGroup viewGroup = (ViewGroup) viewIndex.findViewById(R.id.ll_index);
            LinearLayout.LayoutParams numParams=new LinearLayout.LayoutParams(DisplayUtils.dip2px(35,displayParams.fontScale), ViewGroup.LayoutParams.WRAP_CONTENT);
            LinearLayout.LayoutParams lineParams=new LinearLayout.LayoutParams(DisplayUtils.dip2px(35,displayParams.fontScale),DisplayUtils.dip2px(1,displayParams.fontScale));
            for (int i = 1; i <= 12; i++){
                TextView tv = new TextView(mContext);
                tv.setGravity(Gravity.CENTER);
                tv.setPadding(0, DisplayUtils.dip2px(15, displayParams.fontScale), 0, DisplayUtils.dip2px(15, displayParams.fontScale));
                tv.setLayoutParams(numParams);
                tv.setBackgroundColor(Color.parseColor("#4DD0E1"));
                tv.setText(i+"");
                viewGroup.addView(tv);
                View v = new View(mContext);
                v.setLayoutParams(lineParams);
                v.setBackgroundColor(Color.WHITE);
                viewGroup.addView(v);
             }
             return new ChartViewHolder(viewIndex);
/*
    <TextView
        android:layout_width="35dp"
        android:layout_height="wrap_content"
        android:gravity="center"
        android:paddingTop="15dp"
        android:paddingBottom="15dp"
        android:text="1"/>
 */

        }else {
            View viewCourse = LayoutInflater.from(mContext).inflate(R.layout.chart_recyclerview_item_course, parent, false);
            ViewGroup viewGroup = (ViewGroup) viewCourse.findViewById(R.id.ll_course);


                while (data.size() - 1 >= index) {
                    if (data.get(index).getWeek() == viewType) {
                        int[] wide = data.get(index).getTime();
                        LinearLayout.LayoutParams courseParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (wide[1] - wide[0] + 1) * DisplayUtils.dip2px(50, displayParams.fontScale));
                        if (index == 0) {
                            courseParams.setMargins(0, (wide[0] - 1) * DisplayUtils.dip2px(50, displayParams.fontScale), 0, 0);
                        } else if (data.get(index).getWeek() != data.get(index - 1).getWeek()) {
                            courseParams.setMargins(0, (wide[0] - 1) * DisplayUtils.dip2px(50, displayParams.fontScale), 0, 0);
                        } else
                            courseParams.setMargins(0, (wide[0] - data.get(index - 1).getTime()[1] - 1) * DisplayUtils.dip2px(50, displayParams.fontScale), 0, 0);
                        TextView tv = new TextView(mContext);
                        tv.setGravity(Gravity.CENTER);
            //            tv.setPadding(0, DisplayUtils.dip2px(15, displayParams.fontScale), 0, DisplayUtils.dip2px(15, displayParams.fontScale));
                        tv.setLayoutParams(courseParams);

                        tv.setBackgroundColor(Color.parseColor("#B2EBF2"));
                        tv.setText(data.get(index).getName() + "\n" + data.get(index).getPlace() + "\n" + data.get(index).getTeacher());
                        viewGroup.addView(tv);
                    }
                    index++;
                }

                index = 0;


            return new ChartViewHolder(viewCourse);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 6;
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0)
            return TYPE_INDEX;
        else
            return position;
    }

    public static class ChartViewHolder extends RecyclerView.ViewHolder{

        public ChartViewHolder(View view){
            super(view);
        }
    }
}
