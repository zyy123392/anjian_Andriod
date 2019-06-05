package com.example.yuyin1.action;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yuyin1.R;
import com.example.yuyin1.util.AnimationUtils;
import com.example.yuyin1.util.ChartUtils;
import com.example.yuyin1.util.MyMarkerView;
import com.example.yuyin1.util.ShowUtils;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StatisticActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView tvDate;
    private ImageView ivDate;
    private LineChart chart;
    private static final String[] dates = new String[]{"按月统计", "按物品类别统计", "按航班统计"};
    private List<String> dateList = Arrays.asList(dates);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initView();
    }
    private List<Entry> getData(int position) {
        if(position == 0){
            ArrayList<Integer> amount = new ArrayList<>();
            Cursor a  = DataSupport.findBySQL("select substr(date,1,2),count(*) from Message group by substr(date,1,2)");
            if(a.moveToFirst()){
                do{

                    String count = a.getString(a.getColumnIndex("count(*)"));
                    amount.add(Integer.parseInt(count));

                }while(a.moveToNext());
            }
            List<Entry> values = new ArrayList<>();
            for(int i = 0;i < amount.size();i++){
                values.add(new Entry(i, amount.get(i)));
            }
            return values;
        }else if(position == 1){
            ArrayList<Integer> amount = new ArrayList<>();
            Cursor a  = DataSupport.findBySQL("select goods,count(*) from Message group by goods ");
            if(a.moveToFirst()){
                do{

                    String count = a.getString(a.getColumnIndex("count(*)"));
                    amount.add(Integer.parseInt(count));

                }while(a.moveToNext());
            }
            a.close();
            List<Entry> values = new ArrayList<>();
            for(int i = 0;i < amount.size();i++){
                values.add(new Entry(i, amount.get(i)));
            }
            return values;
        }else if(position == 2){
            ArrayList<Integer> amount = new ArrayList<>();
            Cursor a  = DataSupport.findBySQL("select flightid,count(*) from Message group by flightid");
            if(a.moveToFirst()){
                do{

                    String count = a.getString(a.getColumnIndex("count(*)"));
                    amount.add(Integer.parseInt(count));

                }while(a.moveToNext());
            }
            a.close();
            List<Entry> values = new ArrayList<>();
            for(int i = 0;i < amount.size();i++){
                values.add(new Entry(i, amount.get(i)));
            }
            return values;
        }


        return null;
    }

    private void initView() {
        tvDate = (TextView) findViewById(R.id.tv_date);
        ivDate = (ImageView) findViewById(R.id.iv_date);
        chart = (LineChart) findViewById(R.id.chart);

        ivDate.setColorFilter(Color.WHITE);
        tvDate.setOnClickListener(this);
        ivDate.setOnClickListener(this);

        ChartUtils.initChart(chart);
        ArrayList<Integer> amount = new ArrayList<>();
        Cursor a  = DataSupport.findBySQL("select substr(date,1,2),count(*) from Message group by substr(date,1,2)");
        if(a.moveToFirst()){
            do{

                String count = a.getString(a.getColumnIndex("count(*)"));
                amount.add(Integer.parseInt(count));

            }while(a.moveToNext());
        }
        a.close();
        List<Entry> values = new ArrayList<>();
        for(int i = 0;i < amount.size();i++){
            values.add(new Entry(i, amount.get(i)));
        }

        ChartUtils.notifyDataSetChanged(chart, values, ChartUtils.dayValue);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_date:
            case R.id.iv_date:
                String data = tvDate.getText().toString();

                if (!ShowUtils.isPopupWindowShowing()) {
                    AnimationUtils.startModeSelectAnimation(ivDate, true);
                    ShowUtils.showPopupWindow(this, tvDate, 200, 166, dateList,
                            new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view,
                                                        int position, long id) {
                                    ShowUtils.updatePopupWindow(position);
                                    AnimationUtils.startModeSelectAnimation(ivDate, false);
                                    ShowUtils.popupWindowDismiss();
                                    tvDate.setText(dateList.get(position));
                                    // 更新图表

                                    ChartUtils.notifyDataSetChanged(chart, getData(position), position);
                                }
                            });
                } else {
                    AnimationUtils.startModeSelectAnimation(ivDate, false);
                    ShowUtils.popupWindowDismiss();
                }

                if (dateList.get(0).equals(data)) {
                    ShowUtils.updatePopupWindow(0);
                } else if (dateList.get(1).equals(data)) {
                    ShowUtils.updatePopupWindow(1);
                } else if (dateList.get(2).equals(data)) {
                    ShowUtils.updatePopupWindow(2);
                }
                break;

            default:
                break;
        }
    }
}



