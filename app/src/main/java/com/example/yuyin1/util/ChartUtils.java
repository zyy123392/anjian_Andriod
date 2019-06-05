package com.example.yuyin1.util;

import android.database.Cursor;
import android.graphics.Color;
import android.util.Log;

import com.example.yuyin1.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 图表工具
 * Created by yangle on 2016/11/29.
 */
public class ChartUtils {

    public static int dayValue = 0;
    public static int weekValue = 1;
    public static int monthValue = 2;
    static XAxis xAxis;
    /**
     * 初始化图表
     *
     * @param chart 原始图表
     * @return 初始化后的图表
     */
    public static LineChart initChart(LineChart chart) {
        // 不显示数据描述
        chart.getDescription().setEnabled(false);
        // 没有数据的时候，显示“暂无数据”
        chart.setNoDataText("暂无数据");
        // 不显示表格颜色
        chart.setDrawGridBackground(false);
        // 不可以缩放
        chart.setScaleEnabled(false);
        // 不显示y轴右边的值
        chart.getAxisRight().setEnabled(false);
        // 不显示图例
        Legend legend = chart.getLegend();
        legend.setEnabled(false);
        // 向左偏移15dp，抵消y轴向右偏移的30dp
        chart.setExtraLeftOffset(-15);

        xAxis = chart.getXAxis();
        // 不显示x轴
        xAxis.setDrawAxisLine(false);
        // 设置x轴数据的位置

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setTextSize(12);

        xAxis.setGranularityEnabled(true);

        xAxis.setGridColor(Color.parseColor("#30FFFFFF"));
        // 设置x轴数据偏移量
        xAxis.setYOffset(-12);

        YAxis yAxis = chart.getAxisLeft();
        // 不显示y轴
        yAxis.setDrawAxisLine(false);
        // 设置y轴数据的位置
        yAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        // 不从y轴发出横向直线
        yAxis.setDrawGridLines(false);
        yAxis.setTextColor(Color.WHITE);
        yAxis.setTextSize(12);
        // 设置y轴数据偏移量
        yAxis.setXOffset(30);
        yAxis.setYOffset(-3);
        yAxis.setAxisMinimum(0);
        yAxis.setGranularity(1f);
        //Matrix matrix = new Matrix();
        // x轴缩放1.5倍
        //matrix.postScale(1.5f, 1f);
        // 在图表动画显示之前进行缩放
        //chart.getViewPortHandler().refresh(matrix, chart, false);
        // x轴执行动画
        //chart.animateX(2000);
        // 设置是否可以触摸
        chart.setTouchEnabled(true);
        chart.setDragDecelerationFrictionCoef(0.9f);
        // 是否可以拖拽
        chart.setDragEnabled(true);//放大可拖拽
        //设置是否可以缩放
        chart.setScaleEnabled(false);
        chart.setDrawGridBackground(false);
        chart.setHighlightPerDragEnabled(true);
        chart.setPinchZoom(true);
//设置一页最大显示个数为6，超出部分就滑动
        float ratio = (float) xValuesProcess(1).length/(float) 6;
        //显示的时候是按照多大的比率缩放显示,1f表示不放大缩小
        chart.zoom(ratio,1f,0,0);

        return chart;
    }

    /**
     * 设置图表数据
     *
     * @param chart  图表
     * @param values 数据
     */
    public static void setChartData(LineChart chart, List<Entry> values) {
        LineDataSet lineDataSet;

        if (chart.getData() != null && chart.getData().getDataSetCount() > 0) {

            lineDataSet = (LineDataSet) chart.getData().getDataSetByIndex(0);
            lineDataSet.setValues(values);
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();
            chart.invalidate();
        } else {
            lineDataSet = new LineDataSet(values, "");
            // 设置曲线颜色
            lineDataSet.setColor(Color.parseColor("#FFFFFF"));
            // 设置平滑曲线
            //lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            // 不显示坐标点的小圆点
            lineDataSet.setDrawCircles(true);
            // 不显示坐标点的数据
            lineDataSet.setDrawValues(false);
            // 不显示定位线
            lineDataSet.setHighlightEnabled(true);

            LineData data = new LineData(lineDataSet);
            chart.setData(data);
            chart.invalidate();
        }
    }

    /**
     * 更新图表
     *
     * @param chart     图表
     * @param values    数据
     * @param valueType 数据类型
     */
    public static void notifyDataSetChanged(LineChart chart, List<Entry> values,
                                            final int valueType) {
        chart.getXAxis().setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                int index = (int) value;
                if (index < 0 || index >= xValuesProcess(valueType).length) {
                    return "";
                } else {
                    return xValuesProcess(valueType)[(int) value];
                }

            }
        });
        MarkerView mv = new MyMarkerView(chart.getContext(), R.layout.content_marker_view,xValuesProcess(valueType),valueType);
        chart.setMarkerView(mv);
        chart.invalidate();
        setChartData(chart, values);
    }

    /**
     * x轴数据处理
     *
     * @param valueType 数据类型
     * @return x轴数据
     */
    private static String[] xValuesProcess(int valueType) {


        if (valueType == dayValue) { // 按月份
            ArrayList<String> go = new ArrayList<>();
            Cursor a  = DataSupport.findBySQL("select substr(date,1,2) as date,count(*) from Message group by substr(date,1,2)");
            if(a.moveToFirst()){
                do{
                    String name = a.getString(a.getColumnIndex("date"));
                    String count = a.getString(a.getColumnIndex("count(*)"));
                    Log.d("way",name);
                    go.add(name);

                }while(a.moveToNext());
            }
            a.close();
            String[] goods = new String[go.size()];
            for(int i = 0;i < go.size();i++){

                goods[i] = go.get(i);
            }

            return goods;
        } else if (valueType == weekValue) { // 物品类型
            ArrayList<String> go = new ArrayList<>();
            Cursor a  = DataSupport.findBySQL("select goods,count(*) from Message group by goods ");
            if(a.moveToFirst()){
                do{
                    String name = a.getString(a.getColumnIndex("goods"));
                    String count = a.getString(a.getColumnIndex("count(*)"));
                    go.add(name);

                }while(a.moveToNext());
            }
            a.close();
            String[] goods = new String[go.size()];
            for(int i = 0;i < go.size();i++){

                goods[i] = go.get(i);
            }

            return goods;
        } else if (valueType == monthValue) { // 航班
            ArrayList<String> go = new ArrayList<>();
            Cursor a  = DataSupport.findBySQL("select flightid,count(*) from Message group by flightid ");
            if(a.moveToFirst()){
                do{
                    String name = a.getString(a.getColumnIndex("flightid"));
                    String count = a.getString(a.getColumnIndex("count(*)"));
                    go.add(name);

                }while(a.moveToNext());
            }
            a.close();
            String[] goods = new String[go.size()];
            for(int i = 0;i < go.size();i++){

                goods[i] = go.get(i);
            }

            return goods;

        }
        return new String[]{};
    }
}