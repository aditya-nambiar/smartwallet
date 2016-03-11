package com.togather.me.smartwallet;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;



public class ScrollingActivity extends AppCompatActivity {

    private RecyclerView busRidesView;
    private Adapter mAdapter;
    private List<CashFlow> cashFlowList = new ArrayList<>();

    protected int getLayoutId() {
        return R.layout.activity_listofitems;
    }


    public static int randInt(int min, int max) {

        // NOTE: This will (intentionally) not run as written so that folks
        // copy-pasting have to think about how to initialize their
        // Random instance.  Initialization of the Random instance is outside
        // the main scope of the question, but some decent options are to have
        // a field that is initialized once and then re-used as needed or to
        // use ThreadLocalRandom (if using at least Java 1.7).
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }



    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        LinearLayout llLayoutContainer = (LinearLayout) findViewById(R.id.ll_layout_container);
        View layoutView = LayoutInflater.from(this).inflate(getLayoutId(), null);
        llLayoutContainer.addView(layoutView);
        initViews();
    }

    protected void initViews() {

        busRidesView = (RecyclerView) findViewById(R.id.rv_activity_listofitems);

        for (int i=0; i< 15; i++){
            CashFlow a = new CashFlow();
            a.ampm = "PM";
            a.desp = "Lent to Rahul";
            a.time_hours = randInt(1,9);
            a.time_minutes = randInt(11,59);
            a.amt = randInt(1, 1000);
            cashFlowList.add(a);
        }

        initRunningRoutesList();
    }

    private void initRunningRoutesList() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        busRidesView = (RecyclerView) findViewById(R.id.rv_activity_listofitems);
        busRidesView.setLayoutManager(layoutManager);
        mAdapter = new Adapter(this, cashFlowList);
        busRidesView.setItemAnimator(new DefaultItemAnimator());
        busRidesView.setAdapter(mAdapter);
        System.out.println("InitRunnin");
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
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
