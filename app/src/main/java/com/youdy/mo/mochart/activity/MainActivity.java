package com.youdy.mo.mochart.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.nshmura.recyclertablayout.RecyclerTabLayout;
import com.youdy.mo.mochart.R;
import com.youdy.mo.mochart.adapter.CustomRecyclerViewAdapter;
import com.youdy.mo.mochart.model.Course;
import com.youdy.mo.mochart.utils.CSUConnect;
import com.youdy.mo.mochart.utils.DateUtils;
import com.youdy.mo.mochart.utils.ExcelHandler;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {

    private ViewPager viewPager;
    private CustomRecyclerViewAdapter customRecyclerViewAdapter;
    private RecyclerTabLayout recyclerTabLayout;
    private LoginDialog dialog;
    private CSUConnect csu;
    private ExcelHandler excelHandler;
    private FirstDaySetDialog fdsDialog;


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    dialog.setRandomCode((Bitmap) msg.obj);
                    break;
                case 2:
                    dialog.dismiss();
                    Toast.makeText(MainActivity.this,msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    dialog.dismiss();
                    Toast.makeText(MainActivity.this,R.string.wait, Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    try {
                        List<Course>[] newData = excelHandler.readExcel(MainActivity.this.openFileInput("network.xls"));
                        initTab(newData);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //toolbar config
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);//左上角返回
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //  View headerView = navigationView.getHeaderView(0);
          List<Course>[] courses = new ArrayList[20];


        viewPager = (ViewPager) findViewById(R.id.viewpager);
        recyclerTabLayout = (RecyclerTabLayout)findViewById(R.id.recycler_tab_layout);
        excelHandler =  new ExcelHandler();
        try {
            courses = excelHandler.readExcel(MainActivity.this.openFileInput("network.xls"));
        } catch (FileNotFoundException e) {
            for(int i  = 0;i < 20;i++)
                courses[i] = new ArrayList<>();
        }
        initTab(courses);
    }

    private void initTab(List[] courses){

        SharedPreferences sharedPreferences = getSharedPreferences("first_day",MODE_PRIVATE);
        int year = sharedPreferences.getInt("year",DateUtils.getYear())-1900;
        int month = sharedPreferences.getInt("month",DateUtils.getMonth())-1;
        int day = sharedPreferences.getInt("day",DateUtils.getDay());

        List<String> items = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            items.add(String.valueOf(i));
        }
        customRecyclerViewAdapter = new CustomRecyclerViewAdapter(this,courses,new Date(year,month,day));
        customRecyclerViewAdapter.addAll(items);
        viewPager.setAdapter(customRecyclerViewAdapter);
        //动态配置
        viewPager.setCurrentItem(DateUtils.getWeekNum(new Date(year,month,day),new Date()));
        recyclerTabLayout.setUpWithViewPager(viewPager);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_test) {

        } else if (id == R.id.nav_library) {

        } else if (id == R.id.nav_score) {

        } else if (id == R.id.nav_login) {
            csu = new CSUConnect(this,mHandler);
            dialog = new LoginDialog(MainActivity.this);
            dialog.setBtnCancel(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        dialog.dismiss();
                    }
            });
            dialog.setBtnLogin(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    csu.login(dialog.getName(),dialog.getPassword(),dialog.getRandomCode());
                }
            });
            Toast.makeText(this,dialog.getName(), Toast.LENGTH_LONG);
            csu.getNetRandomCode();
            dialog.show();
        } else if (id == R.id.nav_exit) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            android.os.Process.killProcess(android.os.Process.myPid());
        }else if(id == R.id.nav_first){
            fdsDialog = new FirstDaySetDialog(this);
            fdsDialog.setTitle("设置第一周星期一");
            fdsDialog.setBtnCancel(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fdsDialog.dismiss();
                }
            });
            fdsDialog.setBtnSetup(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences.Editor editor = getSharedPreferences("first_day", Context.MODE_PRIVATE).edit();
                    editor.putInt("year",fdsDialog.getYear());
                    editor.putInt("month",fdsDialog.getMonth());
                    editor.putInt("day",fdsDialog.getDay());
                    editor.commit();
                    try {
                        List<Course>[] newData = excelHandler.readExcel(MainActivity.this.openFileInput("network.xls"));
                        initTab(newData);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    fdsDialog.dismiss();
                }
            });
            fdsDialog.show();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
