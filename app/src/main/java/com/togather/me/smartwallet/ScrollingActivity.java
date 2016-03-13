//package com.togather.me.smartwallet;
//
//import android.bluetooth.BluetoothAdapter;
//import android.bluetooth.BluetoothDevice;
//import android.bluetooth.BluetoothSocket;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.os.Bundle;
//import android.os.Handler;
//import android.support.design.widget.FloatingActionButton;
//import android.support.design.widget.Snackbar;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.DefaultItemAnimator;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.support.v7.widget.Toolbar;
//import android.view.LayoutInflater;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.LinearLayout;
//import android.widget.ListView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Random;
//import java.util.Set;
//import java.util.UUID;
//
//
//public class ScrollingActivity extends AppCompatActivity {
//
//    private RecyclerView busRidesView;
//    private Adapter mAdapter;
//    private List<CashFlow> cashFlowList = new ArrayList<>();
//
//    private Button onBtn;
//    private Button offBtn;
//    private TextView text;
//    private Button findBtn;
//    private Button listBtn;
//    OutputStream mmOutputStream;
//    InputStream mmInputStream;
//    Thread workerThread;
//    byte[] readBuffer;
//    int readBufferPosition;
//    int counter;
//    volatile boolean stopWorker;
//
//    BluetoothSocket mmSocket;
//    BluetoothDevice mmDevice;
//
//    private Set<BluetoothDevice> pairedDevices;
//    private ListView myListView;
//    private ArrayAdapter<String> BTArrayAdapter;
//    private BluetoothAdapter myBluetoothAdapter;
//    private static final int REQUEST_ENABLE_BT = 1;
//
//    protected int getLayoutId() {
//        return R.layout.activity_listofitems;
//    }
//
//
//    public static int randInt(int min, int max) {
//
//        // NOTE: This will (intentionally) not run as written so that folks
//        // copy-pasting have to think about how to initialize their
//        // Random instance.  Initialization of the Random instance is outside
//        // the main scope of the question, but some decent options are to have
//        // a field that is initialized once and then re-used as needed or to
//        // use ThreadLocalRandom (if using at least Java 1.7).
//        Random rand = new Random();
//
//        // nextInt is normally exclusive of the top value,
//        // so add 1 to make it inclusive
//        int randomNum = rand.nextInt((max - min) + 1) + min;
//
//        return randomNum;
//    }
//
//
//
//    @Override
//    protected void onPostCreate(Bundle savedInstanceState) {
//        super.onPostCreate(savedInstanceState);
//        LinearLayout llLayoutContainer = (LinearLayout) findViewById(R.id.ll_layout_container);
//        View layoutView = LayoutInflater.from(this).inflate(getLayoutId(), null);
//        llLayoutContainer.addView(layoutView);
//        initViews();
//    }
//
//    protected void initViews() {
//
//        busRidesView = (RecyclerView) findViewById(R.id.rv_activity_listofitems);
//
//        for (int i=0; i< 15; i++){
//            CashFlow a = new CashFlow();
//            a.ampm = "PM";
//            a.desp = "Lent to Rahul";
//            a.time_hours = randInt(1,9);
//            a.time_minutes = randInt(11,59);
//            a.amt = randInt(1, 1000);
//
//            if( i% 3 == 0){
//                a.latitude = 40.722543;
//                a.longitude = -73.998585;
//            } else if ( i%3 ==1 ){
//                a.latitude = 40.7577;
//                a.longitude = -73.9857;
//            } else if ( i%3 ==2 ){
//                a.latitude = 40.7057;
//                a.longitude = -73.9964;
//            } else if ( i%3 ==3 ){
//                a.latitude = 40.7064;
//                a.longitude = -74.0094;
//            }
//
//            cashFlowList.add(a);
//        }
//
//        initRunningRoutesList();
//    }
//
//    private void initRunningRoutesList() {
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
//        busRidesView = (RecyclerView) findViewById(R.id.rv_activity_listofitems);
//        busRidesView.setLayoutManager(layoutManager);
//        mAdapter = new Adapter(this, cashFlowList);
//        busRidesView.setItemAnimator(new DefaultItemAnimator());
//        busRidesView.setAdapter(mAdapter);
//        System.out.println("InitRunning");
//    }
//
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_scrolling);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
//
//        initializeBluetooth();
//
//    }
//
//    protected void initializeBluetooth() {
//        System.out.println("reached here");
//        myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//        text = (TextView) findViewById(R.id.text);
//        onBtn = (Button)findViewById(R.id.turnOn);
//        offBtn = (Button)findViewById(R.id.turnOff);
//        listBtn = (Button)findViewById(R.id.paired);
//        findBtn = (Button)findViewById(R.id.search);
//        myListView = (ListView)findViewById(R.id.listView12);
//// create the arrayAdapter that contains the BTDevices, and set it to the ListView
//        BTArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
//
//        if (myBluetoothAdapter == null) {
//            System.out.println("reached here2");
//            // Device does not support Bluetooth
//            onBtn.setEnabled(false);
//            offBtn.setEnabled(false);
//            findBtn.setEnabled(false);
//            listBtn.setEnabled(false);
//
//            text.setText("Status: not supported");
//            Toast.makeText(getApplicationContext(),"Your device does not support Bluetooth",
//                    Toast.LENGTH_LONG).show();
//            return;
//        }
//        else {
//            System.out.println("reached here3");
//            onBtn.setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    on(v);
//                }
//            });
//            offBtn.setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    off(v);
//                }
//            });
//            listBtn.setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    list(v);
//                }
//            });
//            findBtn.setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    find(v);
//                }
//            });
//
//
//            myListView.setAdapter(BTArrayAdapter);
//        }
//    }
//
//    public void list(View view){
//        // get paired devices
//        pairedDevices = myBluetoothAdapter.getBondedDevices();
//
//        // put it's one to the adapter
//        for(BluetoothDevice device : pairedDevices) {
//            if (device.getName().equals("BlueLINK")) {
//                BTArrayAdapter.add("Device found" + "\n" + device.getName() + "\n" + device.getAddress());
//                mmDevice = device;
//
//                System.out.println("device is " + mmDevice);
//                openBT();
//                break;
//            }
//            //BTArrayAdapter.add(device.getName() + "\n" + device.getAddress());
//        }
//        Toast.makeText(getApplicationContext(),"Show Paired Devices",
//                Toast.LENGTH_SHORT).show();
//
//
//    }
//
//    void openBT()
//    {
////        UUID uuid = UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");
//        UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); //Standard SerialPortService ID
//        try {
//            if(mmDevice.getBondState()==mmDevice.BOND_BONDED) {
//                System.out.println("i am here");
//                mmSocket = mmDevice.createInsecureRfcommSocketToServiceRecord(uuid);
//            }
//            else {
//                System.out.println("i am not here");
//                mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
//            }
//            try {
//                mmSocket.connect();
//            } catch(IOException e) {
//                System.out.println("entered here");
//                mmSocket = (BluetoothSocket) mmDevice.getClass().getMethod("createRfcommSocket", new Class[] {int.class}).invoke(mmDevice,1);
//                mmSocket.connect();
//            }
//            mmOutputStream = mmSocket.getOutputStream();
//            mmInputStream = mmSocket.getInputStream();
//            System.out.println("##D##" + mmSocket);
//        } catch(Exception e) {
//            System.out.println("scope "+ e);
//        }
//        beginListenForData();
//        text.setText("Bluetooth Opened");
//    }
//
//    void beginListenForData()
//    {
//        final Handler handler = new Handler();
//        final byte delimiter = 10; //This is the ASCII code for a newline character
//
//        stopWorker = false;
//        readBufferPosition = 0;
//        readBuffer = new byte[1024];
//        workerThread = new Thread(new Runnable()
//        {
//            public void run()
//            {
//                while(!Thread.currentThread().isInterrupted() && !stopWorker)
//                {
//                    try
//                    {
//                        int bytesAvailable = mmInputStream.available();
//                        if(bytesAvailable > 0)
//                        {
//                            byte[] packetBytes = new byte[bytesAvailable];
//                            mmInputStream.read(packetBytes);
//                            for(int i=0;i<bytesAvailable;i++)
//                            {
//                                byte b = packetBytes[i];
//                                if(b == delimiter)
//                                {
//                                    byte[] encodedBytes = new byte[readBufferPosition];
//                                    System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);
//                                    final String data = new String(encodedBytes, "US-ASCII");
//                                    readBufferPosition = 0;
//
//                                    handler.post(new Runnable()
//                                    {
//                                        public void run()
//                                        {
//                                            System.out.println("FUCK YEAHH " +data);
//                                            text.setText(data);
//                                        }
//                                    });
//                                }
//                                else
//                                {
//                                    readBuffer[readBufferPosition++] = b;
//                                }
//                            }
//                        }
//                    }
//                    catch (IOException ex)
//                    {
//                        stopWorker = true;
//                    }
//                }
//            }
//        });
//
//        workerThread.start();
//    }
//
//
//    public void find(View view) {
//        if (myBluetoothAdapter.isDiscovering()) {
//            // the button is pressed when it discovers, so cancel the discovery
//            myBluetoothAdapter.cancelDiscovery();
//        }
//        else {
//            BTArrayAdapter.clear();
//            myBluetoothAdapter.startDiscovery();
//
//            registerReceiver(bReceiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
//        }
//    }
//
//    public void on(View view){
//        if (!myBluetoothAdapter.isEnabled()) {
//            Intent turnOnIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//            startActivityForResult(turnOnIntent, REQUEST_ENABLE_BT);
//
//            Toast.makeText(getApplicationContext(),"Bluetooth turned on" ,
//                    Toast.LENGTH_LONG).show();
//        } else{
//            Toast.makeText(getApplicationContext(),"Bluetooth is already on",
//                    Toast.LENGTH_LONG).show();
//        }
//    }
//
//     public void off(View view){
//        myBluetoothAdapter.disable();
//        text.setText("Status: Disconnected");
//
//        Toast.makeText(getApplicationContext(),"Bluetooth turned off",
//                Toast.LENGTH_LONG).show();
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if(requestCode == REQUEST_ENABLE_BT){
//            if(myBluetoothAdapter.isEnabled()) {
//                text.setText("Status: Enabled");
//            } else {
//                text.setText("Status: Disabled");
//            }
//        }
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//    final BroadcastReceiver bReceiver = new BroadcastReceiver() {
//        public void onReceive(Context context, Intent intent) {
//            String action = intent.getAction();
//            // When discovery finds a device
//            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
//                // Get the BluetoothDevice object from the Intent
//                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
//                // add the name and the MAC address of the object to the arrayAdapter
//                BTArrayAdapter.add(device.getName() + "\n" + device.getAddress());
//                BTArrayAdapter.notifyDataSetChanged();
//            }
//        }
//    };
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        //unregisterReceiver(bReceiver);
//    }
//}

// aaaaaaaaaaaaaaaaaaaaa
//

/*
package com.togather.me.smartwallet;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.ParcelUuid;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;


public class ScrollingActivity extends ActionBarActivity {

    private Button led ;
    private String btAddress;

    private UUID id ;

    private BluetoothAdapter adapter = null;
    private BluetoothDevice btDevice = null;
    private BluetoothSocket btSocket = null;

    private BtTransmission btTransmit;

    private Boolean sendSuccess ;
    private Boolean readSuccess ;
    private Boolean ledState = false;
    Handler handler = null;
    ReaderThread reader = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);
        led = (Button) findViewById(R.id.led); //rew
        Intent Control = getIntent();
        btAddress = Control.getStringExtra(BTdeviceSelect.EXTRA_DEVICE_ADDRESS);
        adapter = BluetoothAdapter.getDefaultAdapter();
        btDevice = adapter.getRemoteDevice(btAddress);
        ParcelUuid[] uuid = btDevice.getUuids();
        id = UUID.fromString(uuid[0].toString());
        System.out.println("id is "+id);
        BtConnectThread connect = new BtConnectThread();

        connect.start();

        handler = new Handler();
        //ConnectBT connect = new ConnectBT();
        //connect.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_control, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onStop(){
        super.onStop();
        btTransmit.isRunning=false;
        showMsg("Stopping!");
        btTransmit.isRunning=false;
        if(btSocket != null){
            try {
                btSocket.close();
            } catch (IOException e){
                System.out.println("loc 3");

            }
            btSocket = null;
        }

    }

    private void showMsg(String s){
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();

    }
    private void goBack(){
        try {
            btSocket.close();
        } catch (IOException e){
            System.out.println("loc 2");
        }
        Intent intent = new Intent(ScrollingActivity.this,BTdeviceSelect.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
    //============================ On Click Listeners ===================================
    public void led_onclick(View v){
        if(btSocket.isConnected() && btSocket!=null) {
            if (ledState) {
                ledState = false;
                btTransmit.write("S".getBytes());
                System.out.println("Printing S");
            } else {
                ledState = true;

                btTransmit.write("B".getBytes());
                System.out.println("Printing B");

            }
        }
        else {
            showMsg("Not connected, please try again!");
        }
    }
    public void me(View v){
        DialogFragment d = new Me();
        d.show(getFragmentManager(),"me");
    }
    public void disconnect(View v){
        goBack();
    }
    //================================================================================

    //========================Threads=================================================
    private class BtConnectThread extends Thread {
        public Boolean isConnected = false;
        @Override
        public void run(){


            try {
                btSocket = btDevice.createInsecureRfcommSocketToServiceRecord(id);
                btTransmit = new BtTransmission();
                btTransmit.inStream = btSocket.getInputStream();
                btTransmit.outStream = btSocket.getOutputStream();
                btTransmit.start();

                adapter.cancelDiscovery();
                btSocket.connect();
                isConnected = true;
                reader = new ReaderThread();
                reader.start();
            }
            catch (IOException e) {
                System.out.println("loc 1 "+ e);
                System.out.println("loc 1:"+btTransmit.inStream);
                System.out.println("loc 1:"+btTransmit.outStream);
                goBack();
            }
            if(isConnected=true) {
//                System.out.println("listening starting");
                return;
            }
        }

    }

    private class ReaderThread extends Thread {
        public Boolean isConnected = false;
        Thread workerThread;
        byte[] readBuffer;
        int readBufferPosition;
        int counter;
        boolean stopWorker;
        @Override
        public void run() {
//            final Handler handler = new Handler();
            final byte delimiter = 10; //This is the ASCII code for a newline character



            stopWorker = false;
            readBufferPosition = 0;
            readBuffer = new byte[1024];
            if(isConnected=true) {
                System.out.println("listening starting");
                while (!Thread.currentThread().isInterrupted() && !stopWorker) {
                    try {
                        int bytesAvailable = btTransmit.inStream.available();
                        if (bytesAvailable > 0) {
                            byte[] packetBytes = new byte[bytesAvailable];
                            btTransmit.inStream.read(packetBytes);
                            for (int i = 0; i < bytesAvailable; i++) {
                                byte b = packetBytes[i];
                                System.out.println("received " + b + " " + (char)b);
                                if (b == delimiter) {
                                    byte[] encodedBytes = new byte[readBufferPosition];
                                    System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);
                                    final String data = new String(encodedBytes, "US-ASCII");
                                    readBufferPosition = 0;

                                    handler.post(new Runnable() {
                                        public void run() {
                                            System.out.println("FUCK YEAHH " + data);
                                            // text.setText(data);
                                        }
                                    });
                                } else {
                                    readBuffer[readBufferPosition++] = b;
                                }
                            }
                        }
                    } catch (IOException ex) {
                        System.out.println("loc 4");

                        stopWorker = true;
                    }
                }
            }
            else {

                System.out.println("not connected yet");
            }
        }
    }






    private class BtTransmission extends Thread {

        private InputStream inStream;
        private OutputStream outStream;;
        public Boolean isRunning = true ;
        private byte[] Write ;
        public void run() {
            if (Write != null) {
                try {
                    outStream.write(Write);
                    sendSuccess = true;
                    Write = null;
                } catch (IOException e) {
                    System.out.println("loc 5");

                    sendSuccess = false;
                }
            }
            if(isRunning=false) return;

        }


        public void write(byte[] w){
            Write = w;
        }
        public void cancel() {
            try {
                btSocket.close();
            } catch (IOException e) {
                System.out.println("loc 6");

            }
        }

    }

    //==========================================================================

    //=================================Dialogs==================================

    public class Me extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(R.string.switchOnBT)
                    .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                        }
                    });
            // Create the AlertDialog object and return it
            return builder.create();
        }
    }
    //==========================================================================
}
*/

package com.togather.me.smartwallet;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
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
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import android.os.ParcelUuid;

public class ScrollingActivity extends AppCompatActivity {

    private RecyclerView busRidesView;
    private Adapter mAdapter;
    private List<CashFlow> cashFlowList = new ArrayList<>();

    private Button onBtn;
    private Button offBtn;
    private TextView text;
    private Button findBtn;
    private Button listBtn;
    OutputStream mmOutputStream;
    InputStream mmInputStream;
    Thread workerThread;
    byte[] readBuffer;
    int readBufferPosition;
    int counter;
    volatile boolean stopWorker;

    BluetoothSocket mmSocket;
    BluetoothDevice mmDevice;

    private Set<BluetoothDevice> pairedDevices;
    private ListView myListView;
    private ArrayAdapter<String> BTArrayAdapter;
    private BluetoothAdapter myBluetoothAdapter;
    private static final int REQUEST_ENABLE_BT = 1;


    private String btAddress;
    private UUID id ;

    private BluetoothAdapter adapter = null;
    Handler handler = null;
    private BluetoothSocket btSocket = null;
    private BluetoothDevice btDevice = null;
    ReaderThread reader = null;
    private BtTransmission btTransmit;
    private Boolean sendSuccess ;
    private Boolean readSuccess ;


    protected int getLayoutId() {
        return R.layout.activity_listofitems;
    }
    private void showMsg(String s){
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();

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

        Intent Control = getIntent();
        btAddress = Control.getStringExtra(BTdeviceSelect.EXTRA_DEVICE_ADDRESS);
        adapter = BluetoothAdapter.getDefaultAdapter();
        btDevice = adapter.getRemoteDevice(btAddress);
        ParcelUuid[] uuid = btDevice.getUuids();
        id = UUID.fromString(uuid[0].toString());
        System.out.println("id is " + id);
        BtConnectThread connect = new BtConnectThread();
        System.out.println("connect is " + connect);
        connect.start();

        handler = new Handler();

    }

    private class BtConnectThread extends Thread {
        public Boolean isConnected = false;
        @Override
        public void run(){


            try {
                btSocket = btDevice.createInsecureRfcommSocketToServiceRecord(id);
                btTransmit = new BtTransmission();
                btTransmit.inStream = btSocket.getInputStream();
                btTransmit.outStream = btSocket.getOutputStream();
                btTransmit.start();

                adapter.cancelDiscovery();
                btSocket.connect();
                isConnected = true;
                reader = new ReaderThread();
                reader.start();
            }
            catch (IOException e) {
                System.out.println("loc 1" + e);
                System.out.println("loc 1:"+btTransmit.inStream);
                System.out.println("loc 1:"+btTransmit.outStream);
//                goBack();
            }
            if(isConnected=true) {
//                System.out.println("listening starting");
                return;
            }
        }

    }

    private class BtTransmission extends Thread {

        private InputStream inStream;
        private OutputStream outStream;;
        public Boolean isRunning = true ;
        private byte[] Write ;
        public void run() {
            if (Write != null) {
                try {
                    outStream.write(Write);
                    sendSuccess = true;
                    Write = null;
                } catch (IOException e) {
                    System.out.println("loc 5");

                    sendSuccess = false;
                }
            }
            if(isRunning=false) return;

        }


        public void write(byte[] w){
            Write = w;
        }
        public void cancel() {
            try {
                btSocket.close();
            } catch (IOException e) {
                System.out.println("loc 6");

            }
        }

    }


    private class ReaderThread extends Thread {
        public Boolean isConnected = false;
        Thread workerThread;
        byte[] readBuffer;
        int readBufferPosition;
        int counter;
        boolean stopWorker;
        @Override
        public void run() {
//            final Handler handler = new Handler();
            final byte delimiter = 10; //This is the ASCII code for a newline character



            stopWorker = false;
            readBufferPosition = 0;
            readBuffer = new byte[1024];
            if(isConnected=true) {
                System.out.println("listening starting");
                while (!Thread.currentThread().isInterrupted() && !stopWorker) {
                    try {
                        int bytesAvailable = btTransmit.inStream.available();
                        if (bytesAvailable > 0) {
                            byte[] packetBytes = new byte[bytesAvailable];
                            btTransmit.inStream.read(packetBytes);
                            for (int i = 0; i < bytesAvailable; i++) {
                                byte b = packetBytes[i];
                                System.out.println("received " + b + " " + (char)b);
                                if (b == delimiter) {
                                    byte[] encodedBytes = new byte[readBufferPosition];
                                    System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);
                                    final String data = new String(encodedBytes, "US-ASCII");
                                    readBufferPosition = 0;

                                    handler.post(new Runnable() {
                                        public void run() {
                                            System.out.println("FUCK YEAHH " + data);
                                            // text.setText(data);
                                        }
                                    });
                                } else {
                                    readBuffer[readBufferPosition++] = b;
                                }
                            }
                        }
                    } catch (IOException ex) {
                        System.out.println("loc 4");

                        stopWorker = true;
                    }
                }
            }
            else {

                System.out.println("not connected yet");
            }
        }
    }


    @Override
    public void onStop(){
        super.onStop();
        btTransmit.isRunning=false;
//        showMsg("Stopping!");
        btTransmit.isRunning=false;
        if(btSocket != null){
            try {
                btSocket.close();
            } catch (IOException e){
                System.out.println("loc 3");

            }
            btSocket = null;
        }

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
        //unregisterReceiver(bReceiver);
    }
}
