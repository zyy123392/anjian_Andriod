package com.example.yuyin1.action;

import android.content.Context;
import android.content.Intent;

import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Spinner;

import com.example.yuyin1.entity.Message;
import com.example.yuyin1.adapter.MessageAdapter;
import com.example.yuyin1.R;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Message> messageList = new ArrayList<>();
    MessageAdapter adapter;
    private SearchView mSearchView;
    private Spinner sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         //初始化数据
        initMessage();
        //初始化语音识别
        SpeechUtility.createUtility(MainActivity.this, SpeechConstant.APPID + "=5ce20495");
        //动态表单使用的是RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);
        DefaultItemAnimator defaultItemAnimator = new DefaultItemAnimator();
        defaultItemAnimator.setAddDuration(1000);
        defaultItemAnimator.setRemoveDuration(1000);
        recyclerView.setItemAnimator(defaultItemAnimator);

        mSearchView = (SearchView) findViewById(R.id.searchView);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){

            @Override
            public boolean onQueryTextSubmit(String s) {
                Log.d("search",s);
                if(((String) sp.getSelectedItem()).equals("航班号")){
                    messageList = DataSupport.where("flightId = ?",s).find(Message.class);
                }else if(((String) sp.getSelectedItem()).equals("姓名")){
                    messageList = DataSupport.where("name = ?",s).find(Message.class);
                }else if(((String) sp.getSelectedItem()).equals("物品品名")){
                    messageList = DataSupport.where("goods = ?",s).find(Message.class);
                }else if(((String) sp.getSelectedItem()).equals("开机员")){
                    messageList = DataSupport.where("starter = ?",s).find(Message.class);
                }else if(((String) sp.getSelectedItem()).equals("开箱员")){
                    messageList = DataSupport.where("boxer = ?",s).find(Message.class);
                }
                adapter = new MessageAdapter(messageList);
                RecyclerView recyclerView = findViewById(R.id.recycler_view);
                LinearLayoutManager layoutManager = new LinearLayoutManager(getBaseContext());
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.addItemDecoration(new DividerItemDecoration(getBaseContext(),
                        DividerItemDecoration.VERTICAL));
                recyclerView.setAdapter(adapter);
                DefaultItemAnimator defaultItemAnimator = new DefaultItemAnimator();
                defaultItemAnimator.setAddDuration(1000);
                defaultItemAnimator.setRemoveDuration(1000);
                recyclerView.setItemAnimator(defaultItemAnimator);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                messageList = DataSupport.findAll(Message.class);
                adapter = new MessageAdapter(messageList);
                RecyclerView recyclerView = findViewById(R.id.recycler_view);
                LinearLayoutManager layoutManager = new LinearLayoutManager(getBaseContext());
                recyclerView.setLayoutManager(layoutManager);

                DividerItemDecoration decoration = new DividerItemDecoration(getBaseContext(), DividerItemDecoration.VERTICAL);
                Drawable drawable = getResources().getDrawable(R.drawable.divider_recyclerview);
                recyclerView.addItemDecoration(new DividerItemDecoration(getBaseContext(),
                        DividerItemDecoration.VERTICAL));
                recyclerView.setAdapter(adapter);
                return false;
            }

        });

        sp = (Spinner) findViewById(R.id.spinner);

    }
    private void initMessage(){
        messageList = DataSupport.order("date desc").find(Message.class);
        adapter = new MessageAdapter(messageList);
    }
//重新返回此界面时会调用onRestart生命周期函数
    @Override
    protected void onRestart() {
        super.onRestart();
        initMessage();

        //adapter.notifyDataSetChanged();
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);

        DefaultItemAnimator defaultItemAnimator = new DefaultItemAnimator();
        defaultItemAnimator.setAddDuration(1000);
        defaultItemAnimator.setRemoveDuration(1000);
        recyclerView.setItemAnimator(defaultItemAnimator);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_add) {
            Intent intent = new Intent(MainActivity.this,AddActivity.class);
            startActivity(intent);
            return true;
        }else if(id == R.id.action_tongji){
            Intent intent = new Intent(MainActivity.this,StatisticActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    public  boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = { 0, 0 };
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

}
