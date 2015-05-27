package com.example.q.camara.Statistics;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.RecyclerView;

import com.example.q.camara.R;

import java.util.ArrayList;
import java.util.List;



public class StatisticsActivity extends ActionBarActivity {

    RecyclerView recyclerView;
    Handler h;
    HttpThread thread1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        recyclerView = (RecyclerView) findViewById(R.id.rView_statistics);

        List<StatisticsInfo> statisticsInfo = new ArrayList<>();
        h = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                String s = (String)msg.obj;
                Log.e("++++", "-------"+s);
            }
        };
        thread1 = new HttpThread("GET","users/1/games",null,h);
        thread1.start();

        /*statisticsInfo.add(new StatisticsInfo("H","1",false));
        statisticsInfo.add(new StatisticsInfo("E","2",true));
        statisticsInfo.add(new StatisticsInfo("R","3",true));
        statisticsInfo.add(new StatisticsInfo("G","4",true));
        statisticsInfo.add(new StatisticsInfo("T","5",true));
        statisticsInfo.add(new StatisticsInfo("B","6",true));
        statisticsInfo.add(new StatisticsInfo("J","7",true));
        statisticsInfo.add(new StatisticsInfo("L","1",true));
        statisticsInfo.add(new StatisticsInfo("Ñ","2",true));*/


        StatisticsInfoAdapter statisticsInfoAdapter = new StatisticsInfoAdapter(statisticsInfo,this);
        recyclerView.setAdapter(statisticsInfoAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_statistics, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
