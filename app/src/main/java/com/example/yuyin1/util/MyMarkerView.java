package com.example.yuyin1.util;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.widget.TextView;

import com.example.yuyin1.R;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

import org.litepal.crud.DataSupport;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class MyMarkerView extends MarkerView {
    private TextView mContentTv;
    private String[] x;
    private DecimalFormat format;
    private int type;
    
    public MyMarkerView(Context context, int layoutResource, String[] xAxis,int type) {

        super(context, layoutResource);
        mContentTv = (TextView) findViewById(R.id.tv_content_marker_view);
        this.x = xAxis;
        this.type = type;
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        if(type == 0){
            ArrayList<String> go = new ArrayList<>();
            Cursor a  = DataSupport.findBySQL("select goods from Message where substr(date,1,2)='"+x[(int)e.getX()]+"' group by goods");
            if(a.moveToFirst()){
                do{
                    String name = a.getString(a.getColumnIndex("goods"));

                    Log.d("eee1",name);
                    go.add(name);

                }while(a.moveToNext());
            }
            a.close();
            Log.d("eee1", String.valueOf(go.size()));
            String res = new String();
            for(int i = 0;i < go.size();i++){
                res += go.get(i)+",";
            }
            mContentTv.setText("出现的违禁品有: " +res);
        }else if(type == 1){
            ArrayList<String> go = new ArrayList<>();
            Cursor a  = DataSupport.findBySQL("select substr(date,1,2) as date from Message where goods='"+x[(int)e.getX()]+"' group by substr(date,1,2)");
            if(a.moveToFirst()){
                do{
                    String name = a.getString(a.getColumnIndex("date"));

                    Log.d("eee1",name);
                    go.add(name);

                }while(a.moveToNext());
            }
            a.close();
            Log.d("eee1", String.valueOf(go.size())+"  "+x[(int)e.getX()]+" "+ e.getX());
            String res = new String();
            for(int i = 0;i < go.size();i++){
                res += go.get(i).substring(1)+"月,";
            }
            mContentTv.setText("出现月份有: " +res);
        }else if(type == 2){
            ArrayList<String> go = new ArrayList<>();
            Cursor a  = DataSupport.findBySQL("select goods from Message where flightid='"+x[(int)e.getX()]+"' group by goods");
            if(a.moveToFirst()){
                do{
                    String name = a.getString(a.getColumnIndex("goods"));

                    Log.d("eee1",name);
                    go.add(name);

                }while(a.moveToNext());
            }
            a.close();
            Log.d("eee1", String.valueOf(go.size()));
            String res = new String();
            for(int i = 0;i < go.size();i++){
                res += go.get(i)+",";
            }
            mContentTv.setText("出现的违禁品有: " +res);
        }

        super.refreshContent(e, highlight);

    }
    @Override
    public MPPointF getOffset() {
        // Log.e("ddd", "width:" + (-(getWidth() / 2)) + "height:" + (-getHeight()));
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }



}

