package com.example.yuyin1.action;

import android.content.Intent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.Button;
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
        Button add = this.findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,AddActivity.class);
                startActivity(intent);
            }
        });
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
                recyclerView.addItemDecoration(new DividerItemDecoration(getBaseContext(),
                        DividerItemDecoration.VERTICAL));
                recyclerView.setAdapter(adapter);
                return false;
            }
        });

        sp = (Spinner) findViewById(R.id.spinner);

    }
    private void initMessage(){
        messageList = DataSupport.findAll(Message.class);
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
}
