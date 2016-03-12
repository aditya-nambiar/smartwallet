package com.togather.me.smartwallet;

import android.bluetooth.BluetoothAdapter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.content.Context;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;
import android.view.View.OnClickListener;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import java.util.Set;

public class ScrollingActivity extends AppCompatActivity {

    private RecyclerView busRidesView;
    private Adapter mAdapter;
    private List<CashFlow> cashFlowList = new ArrayList<>();

    private Button onBtn;
    private Button offBtn;
    private TextView text;
    private Button findBtn;
    private Button listBtn;

    private Edit2 edit_dialog2;


    private Set<BluetoothDevice> pairedDevices;
    private ListView myListView;
    private ArrayAdapter<String> BTArrayAdapter;
    private BluetoothAdapter myBluetoothAdapter;
    private static final int REQUEST_ENABLE_BT = 1;

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

            if( i% 3 == 0){
                a.latitude = 40.722543;
                a.longitude = -73.998585;
            } else if ( i%3 ==1 ){
                a.latitude = 40.7577;
                a.longitude = -73.9857;
            } else if ( i%3 ==2 ){
                a.latitude = 40.7057;
                a.longitude = -73.9964;
            } else if ( i%3 ==3 ){
                a.latitude = 40.7064;
                a.longitude = -74.0094;
            }

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
        System.out.println("InitRunning");
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

        initializeBluetooth();

    }

    protected void initializeBluetooth() {
        System.out.println("reached here");
        myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        text = (TextView) findViewById(R.id.text);
        onBtn = (Button)findViewById(R.id.turnOn);
        offBtn = (Button)findViewById(R.id.turnOff);
        listBtn = (Button)findViewById(R.id.paired);
        findBtn = (Button)findViewById(R.id.search);
        myListView = (ListView)findViewById(R.id.listView12);
// create the arrayAdapter that contains the BTDevices, and set it to the ListView
        BTArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);

        if (myBluetoothAdapter == null) {
            System.out.println("reached here2");
            // Device does not support Bluetooth
            onBtn.setEnabled(false);
            offBtn.setEnabled(false);
            findBtn.setEnabled(false);
            listBtn.setEnabled(false);

            text.setText("Status: not supported");
            Toast.makeText(getApplicationContext(),"Your device does not support Bluetooth",
                    Toast.LENGTH_LONG).show();
            return;
        }
        else {
            System.out.println("reached here3");
            onBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    on(v);
                }
            });
            offBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    off(v);
                }
            });
            listBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    list(v);
                }
            });
            findBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    find(v);
                }
            });


            myListView.setAdapter(BTArrayAdapter);
        }
    }

    public void list(View view){
        // get paired devices
        pairedDevices = myBluetoothAdapter.getBondedDevices();

        // put it's one to the adapter
        for(BluetoothDevice device : pairedDevices)
            BTArrayAdapter.add(device.getName()+ "\n" + device.getAddress());

        Toast.makeText(getApplicationContext(),"Show Paired Devices",
                Toast.LENGTH_SHORT).show();


        edit_dialog2 = new Edit2(getApplicationContext());
        edit_dialog2.setCancelable(false);
        edit_dialog2.setCanceledOnTouchOutside(false);
        edit_dialog2.show();
        Window window = edit_dialog2.getWindow();
        DisplayMetrics displaymetrics = new DisplayMetrics();
        WindowManager wm = (WindowManager)getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        display.getMetrics(displaymetrics);
        int height = (int) (displaymetrics.heightPixels * 0.8);

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, height);

    }

    public void find(View view) {
        if (myBluetoothAdapter.isDiscovering()) {
            // the button is pressed when it discovers, so cancel the discovery
            myBluetoothAdapter.cancelDiscovery();
        }
        else {
            BTArrayAdapter.clear();
            myBluetoothAdapter.startDiscovery();

            registerReceiver(bReceiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
        }
    }

    public void on(View view){
        if (!myBluetoothAdapter.isEnabled()) {
            Intent turnOnIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnOnIntent, REQUEST_ENABLE_BT);

            Toast.makeText(getApplicationContext(),"Bluetooth turned on" ,
                    Toast.LENGTH_LONG).show();
        } else{
            Toast.makeText(getApplicationContext(),"Bluetooth is already on",
                    Toast.LENGTH_LONG).show();
        }
    }

     public void off(View view){
        myBluetoothAdapter.disable();
        text.setText("Status: Disconnected");

        Toast.makeText(getApplicationContext(),"Bluetooth turned off",
                Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_ENABLE_BT){
            if(myBluetoothAdapter.isEnabled()) {
                text.setText("Status: Enabled");
            } else {
                text.setText("Status: Disabled");
            }
        }
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

    final BroadcastReceiver bReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // add the name and the MAC address of the object to the arrayAdapter
                BTArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                BTArrayAdapter.notifyDataSetChanged();
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(bReceiver);
    }
}
