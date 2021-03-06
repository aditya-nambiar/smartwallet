package com.togather.me.smartwallet;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.ParcelUuid;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import android.os.AsyncTask;


public class ScrollingActivity extends AppCompatActivity implements ConnectionCallbacks,
        OnConnectionFailedListener,
        LocationListener {

    private RecyclerView busRidesView;
    private Adapter mAdapter;
    public static List<CashFlow> cashFlowList = new ArrayList<>();

    //Define a request code to send to Google Play services
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private double currentLatitude;
    private double currentLongitude;


    private Button onBtn;
    private Button offBtn;
    private ImageView conn_img;
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

    public static String filename = "cashlogs9.txt";


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
        try {
            initViews();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    protected void initViews() throws FileNotFoundException {

        busRidesView = (RecyclerView) findViewById(R.id.rv_activity_listofitems);


        try {
            FileInputStream cashlogs_input = openFileInput(filename);
            System.out.println("###File EXISTs");
            try (BufferedReader br = new BufferedReader(new InputStreamReader(cashlogs_input))) {
                String line;
                cashFlowList.clear();
                while ((line = br.readLine()) != null) {
                    System.out.println("###"+line);

                    String[] parts = line.split("#");
                /*
                     int time_hours;
                        int time_minutes;
                        String ampm;
                        String desp;
                        int amt;
                        double latitude;
                        double longitude;

                 */

                    String part1 = parts[0]; // 004
                    String part2 = parts[1]; // 034556
                    String part3 = parts[2]; // 004
                    String part4 = parts[3]; // 034556 String part1 = parts[0]; // 004
                    String part5 = parts[4]; // 034556
                    String part6 = parts[5]; // 034556 String part1 = parts[0]; // 004
                    String part7 = parts[6]; // 034556
                    CashFlow a = new CashFlow();
                    a.time_hours = Integer.parseInt(part1);
                    a.time_minutes = Integer.parseInt(part2);
                    a.ampm = part3;
                    a.desp = part4;
                    a.amt = Integer.parseInt(part5);
                    a.latitude = Double.parseDouble(part6);
                    a.longitude = Double.parseDouble(part7);
                    cashFlowList.add(a);

                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (FileNotFoundException e) {
            System.out.println("###Adding Rahul");
            for (int i = 0; i < 1; i++) {
                CashFlow a = new CashFlow();
                a.ampm = "PM";
                a.desp = "Lent to Rahul";
                a.time_hours = randInt(1, 9);
                a.time_minutes = randInt(11, 59);
                a.amt = randInt(1, 1000);

                if (i % 3 == 0) {
                    a.latitude = 40.722543;
                    a.longitude = -73.998585;
                } else if (i % 3 == 1) {
                    a.latitude = 40.7577;
                    a.longitude = -73.9857;
                } else if (i % 3 == 2) {
                    a.latitude = 40.7057;
                    a.longitude = -73.9964;
                } else if (i % 3 == 3) {
                    a.latitude = 40.7064;
                    a.longitude = -74.0094;
                }

                cashFlowList.add(a);
                refresh_file();
            }
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
    protected void onResume() {
        super.onResume();
        //Now lets connect to the API
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.v(this.getClass().getSimpleName(), "onPause()");

        //Disconnect from API onPause()
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }


    }


    class MyAsyncTask extends AsyncTask {
        @Override
        protected Integer doInBackground(Object... params) {
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
            return 1;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        conn_img = (ImageView) findViewById(R.id.connection_status);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                // The next two lines tell the new client that “this” current class will handle connection stuff
                .addConnectionCallbacks((GoogleApiClient.ConnectionCallbacks) this)
                .addOnConnectionFailedListener((GoogleApiClient.OnConnectionFailedListener) this)
                        //fourth line adds the LocationServices API endpoint from GooglePlayServices
                .addApi(LocationServices.API)
                .build();

        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        //Intent serviceIntent = new Intent(this, BluetoothService.class);
        //startService(serviceIntent);
         new MyAsyncTask().execute();
//        Intent Control = getIntent();
//        btAddress = Control.getStringExtra(BTdeviceSelect.EXTRA_DEVICE_ADDRESS);
//        adapter = BluetoothAdapter.getDefaultAdapter();
//        btDevice = adapter.getRemoteDevice(btAddress);
//        ParcelUuid[] uuid = btDevice.getUuids();
//        id = UUID.fromString(uuid[0].toString());
//        System.out.println("id is " + id);
//        BtConnectThread connect = new BtConnectThread();
//        System.out.println("connect is " + connect);
//        connect.start();
//
//        handler = new Handler();

    }

    /**
     * If connected get lat and long
     *
     */
    @Override
    public void onConnected(Bundle bundle) {
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (location == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

        } else {
            //If everything went fine lets get latitude and longitude
            currentLatitude = location.getLatitude();
            currentLongitude = location.getLongitude();

            Toast.makeText(this, currentLatitude + " WORKS " + currentLongitude + "", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
            /*
             * Google Play services can resolve some errors it detects.
             * If the error has a resolution, try sending an Intent to
             * start a Google Play services activity that can resolve
             * error.
             */
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
                    /*
                     * Thrown if Google Play services canceled the original
                     * PendingIntent
                     */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
                /*
                 * If no resolution is available, display a dialog to the
                 * user with the error.
                 */
            Log.e("Error", "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    /**
     * If locationChanges change lat and long
     *
     *
     * @param location
     */
    @Override
    public void onLocationChanged(Location location) {
        currentLatitude = location.getLatitude();
        currentLongitude = location.getLongitude();

        Toast.makeText(this, currentLatitude + " WORKS " + currentLongitude + "", Toast.LENGTH_LONG).show();
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
                ScrollingActivity.this.runOnUiThread(new Runnable() {

                    public void run() {
                        conn_img.setImageResource(R.drawable.ic_bluetooth_connected_black_24dp);
                    }
                });

            }
            catch (Exception e) {
                ScrollingActivity.this.runOnUiThread(new Runnable() {

                    public void run() {
                        conn_img.setImageResource(R.drawable.ic_bluetooth_disabled_black_24dp);

                    }
                });


            }
            if(isConnected=true) {
                System.out.println("listening starting");
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
                String data = "";
                while ( !Thread.currentThread().isInterrupted() && !stopWorker) {
                    try {
                        int bytesAvailable = btTransmit.inStream.available();
                        if (bytesAvailable > 0) {
                            byte[] packetBytes = new byte[bytesAvailable];
                            btTransmit.inStream.read(packetBytes);
                            for (int i = 0; i < bytesAvailable; i++) {
                                byte b = packetBytes[i];
                                System.out.println("received " + b + " " + (char)b);
                                if(!(b == 'z' || b == 'y'))
                                data = data + (char)b;

                                if (b == 'z'|| b =='y') {
                                    System.out.println("###################################");


//                                    handler.post(new Runnable() {
//                                        public void run() {

                                    System.out.println(data);
//                                     data  = data.substring(2);
                                    String temp12 = "";
                                    for(int k=0; k< data.length(); k++){
                                        if( data.charAt(k) >= '0' && data.charAt(k) <= '9')
                                            temp12 = temp12 + data.charAt(k);
                                    }
                                    int amt = Integer.parseInt(temp12);

                                    data = "";

                                    CashFlow temp = new CashFlow();
                                    temp.amt = amt;
                                    if ( b == 'z')
                                        temp.desp = "Given to XXX";
                                    if ( b == 'y')
                                        temp.desp = "Taken from XXX";
                                    DateFormat df1 = new SimpleDateFormat("HH");
                                    DateFormat df2 = new SimpleDateFormat("mm");

                                    Date dateobj = new Date();

                                    temp.time_hours = Integer.parseInt(df1.format(dateobj));
                                    temp.time_minutes = Integer.parseInt(df2.format(dateobj));
                                    if ( temp.time_hours > 12){
                                        temp.time_hours = temp.time_hours -12;
                                        temp.ampm = "PM";
                                    } else temp.ampm = "AM";

                                    temp.longitude = currentLongitude;
                                    temp.latitude = currentLatitude;
                                    System.out.println(currentLongitude);
                                    System.out.println(currentLatitude);

                                    cashFlowList.add(temp);
                                    refresh_file();
                                    System.out.println("556");

                                    // mAdapter.setItems(cashFlowList);
                                    mAdapter.mItems.add(temp);
                                    System.out.println("559");

                                    ScrollingActivity.this.runOnUiThread(new Runnable() {

                                        public void run() {
                                            mAdapter.notifyDataSetChanged();
                                        }
                                    });
                                    System.out.println("564");

                                    ScrollingActivity.this.runOnUiThread(new Runnable() {

                                        public void run() {
                                            busRidesView.scrollToPosition(cashFlowList.size()-1);

                                        }
                                    });
                                    // text.setText(data);
                                    //    }
                                    //  });
//                                } else {
//                                    readBuffer[readBufferPosition++] = b;
//                                }
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


    public  void refresh_file() {
        File dir = getFilesDir();
        File file = new File(dir, filename);
        boolean deleted = file.delete();
        System.out.println("Dlete files " + deleted);
        FileOutputStream cashlogs_output = null;
        try {
            cashlogs_output = openFileOutput(filename, Context.MODE_APPEND);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(cashlogs_output));

        System.out.println("Refreshing File");

        for (int i = 0; i < cashFlowList.size(); i++) {

            CashFlow temp = cashFlowList.get(i);;
            try {
                bw.write(temp.time_hours + "#"+ temp.time_minutes + "#" + temp.ampm + "#" +temp.desp+ "#" + temp.amt+ "#"+ temp.latitude +"#"+ temp.longitude);
                bw.newLine();
                System.out.println("##WRITNG TO FLE");

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileInputStream cashlogs_input = null;
        try {
            cashlogs_input = openFileInput(filename);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(cashlogs_input));
            String line;
        try {
            while ((line = br.readLine()) != null) {
                System.out.println("###" + line);
            }
        } catch (IOException e) {
            e.printStackTrace();
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
                conn_img.setImageResource(R.drawable.ic_bluetooth_connected_black_24dp);

            } else {
                conn_img.setImageResource(R.drawable.ic_bluetooth_disabled_black_24dp);
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
