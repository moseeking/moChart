package com.youdy.mo.mochart.model;


import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youdy.mo.mochart.R;
import com.youdy.mo.mochart.adapter.ChartRecyclerViewAdapter;
import com.youdy.mo.mochart.utils.DateUtils;
import com.youdy.mo.mochart.adapter.DividerItemDecoration;

import java.util.Date;
import java.util.List;

/**
 * Created by mo on 2017/6/19.
 */

public class ChartView extends LinearLayout {
    private Context context;
    private View root;
    private LinearLayout llChartHeader;//课程表头：显示星期几
    private RecyclerView rvChart;//课程表格

    private ChartRecyclerViewAdapter mAdapter;
    String[] week = { "星期一", "星期二", "星期三", "星期四", "星期五"};

    public ChartView(Context context) {
        this(context, null);
    }

    public ChartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        root = inflate(context, R.layout.chart_layout, this);
        llChartHeader = (LinearLayout) root.findViewById(R.id.ll_header);
        rvChart = (RecyclerView) root.findViewById(R.id.rv_chart);
    }

    /*
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ChartView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        this(context, attrs, defStyleAttr);
        Log.w("ScheduleView Warning", "ScheduleView: api >= Build.VERSION_CODES.LOLLIPOP");
    }
*/

    public void initChartView(Date date,List<Course> data){
        initHeader(date);
        initChart(context,data);
    }
    public void initHeader(Date date) {
        int[] startYMD ={DateUtils.getYear(date),DateUtils.getMonth(date),DateUtils.getFirstDay(date)};
        View v = inflate(context, R.layout.chart_header, null);
        llChartHeader.addView(v, new LayoutParams(0, LayoutParams.WRAP_CONTENT, 1));
        for (int index = 0; index < 5; index++) {
            View vItem = inflate(context, R.layout.chart_week_item, null);
            ((TextView) vItem.findViewById(R.id.tv_weekday)).setText(week[index]);
            ((TextView) vItem.findViewById(R.id.tv_date)).setText(DateUtils.getMMdd(startYMD[0], startYMD[1], startYMD[2], index));
            llChartHeader.addView(vItem, new LayoutParams(0, LayoutParams.WRAP_CONTENT, 3));
        }

    }
    private void initChart(Context mContext,List<Course> data){
        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvChart.setLayoutManager(linearLayoutManager);
        //设置适配器
        mAdapter = new ChartRecyclerViewAdapter(mContext,data);
        rvChart.setAdapter(mAdapter);
        //设置动画以及样式
        rvChart.setItemAnimator(new DefaultItemAnimator());
        rvChart.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.HORIZONTAL_LIST));
    }
}
