package com.example.assistance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.DatabaseRegistrar;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import cz.msebera.android.httpclient.Header;

public class translator extends AppCompatActivity{
    private static final String TAG = translator.class.getSimpleName();
    private TextView t,textverify;
    Button b1,log,verify,summery,syno,dict,chatbot,search;
    private TextView t1, temp, t3,st;
    FirebaseFirestore fstore;
    ImageView i1;
    View view;
     private TextView t2;
     String JSON,type="",uid;
    public final static String MESSAGE_KEY ="ganeshannt.senddata.message_key";
    String CITY="Kolkata ,in";
    String address="";
    private LocationManager locationManager;
    int PERMISSION_ID = 44;
    String email="";
    DocumentReference dref;
    HashMap<String,Object>map=new HashMap<>();
    private FusedLocationProviderClient mFusedLocationClient;
   private double longitde;
    private double latitude;
    String API="057c8a885b20fcb411dd3eb41ca4d7d1";
    SwipeRefreshLayout swipe;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // full screen
        setContentView(R.layout.activity_translator);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

            boolean internet = checkInternetConnection();
            if (internet == false) {
                Toast.makeText(translator.this, "Please turn on your internet connection", Toast.LENGTH_SHORT).show();
                opennointernet();
            } else {
                swipe=findViewById(R.id.swipe);
                swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        finish();
                        Intent i =new Intent(translator.this,translator.class);
                        startActivity(i);

                    }
                });
                t = findViewById(R.id.date);
                textverify = findViewById(R.id.verifytext);
                verify = findViewById(R.id.verify);
                view = findViewById(R.id.view);
                FirebaseAuth auth = FirebaseAuth.getInstance();
                FirebaseUser user = auth.getCurrentUser();
                fstore=FirebaseFirestore.getInstance();
                uid=auth.getUid();
                 dref=fstore.collection("users").document(uid);

                if (!user.isEmailVerified()) {
                    textverify.setVisibility(View.VISIBLE);
                    verify.setVisibility(View.VISIBLE);
                    view.setVisibility(View.INVISIBLE);
                    verify.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(translator.this, "Verification mail sent", Toast.LENGTH_SHORT).show();
                                    openrelogin();
                                }

                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("tag", "Email not received " + e.getMessage());
                                    Toast.makeText(translator.this, e.getMessage() + "or try relogin", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                }

                // current date
                String currentDateTimeString = DateFormat.getDateInstance().format(new Date());
                t.setText(currentDateTimeString);
                b1 = findViewById(R.id.bt1);
                b1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openlaguage();
                    }
                });
                dict=findViewById(R.id.dictionary);
                dict.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i=new Intent(translator.this,dictionary.class);
                        startActivity(i);
                    }
                });
                t2 = findViewById(R.id.bt3);
                t2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        opentemperature();
                    }
                });
                summery=findViewById(R.id.summery_button);
                summery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i=new Intent(translator.this,summery.class);
                        startActivity(i);

                    }
                });
                search=findViewById(R.id.search);
                search.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(translator.this,"This feature is not available yet :-(",Toast.LENGTH_SHORT).show();
                    }
                });
                syno=findViewById(R.id.syno);
                syno.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i=new Intent(translator.this,antonym.class);
                        startActivity(i);
                    }
                });
                chatbot=findViewById(R.id.chatbot);
                chatbot.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String url="https://translator-chatbot-iem.herokuapp.com/";
                        Intent i=new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);

                    }
                });
                //getting current longitude and latitude
                mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

                getLastLocation();

                st = findViewById(R.id.state);

                t1 = findViewById(R.id.type);
                //weather
                temp = findViewById(R.id.temp);
                i1 = findViewById(R.id.image_icom);
                //i1.setImageResource(R.drawable.partly);
                // new weatherTask().execute();
                log = findViewById(R.id.logout);
                log.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FirebaseAuth.getInstance().signOut();
                        openrelogin();

                    }
                });
            }
        }

    }
    public void openrelogin(){
        Intent i =new Intent(this,relogin.class);
        startActivity(i);
    }
    public boolean checkInternetConnection(){
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        }
        else
            connected = false;
        return  connected;
    }

    @Override
    public void onBackPressed() {

        moveTaskToBack(true);
    }

    public void openlaguage() {
        Intent i = new Intent(this, language_translate.class);
        type="language translator";
        map.put("type",type);
        dref.set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(translator.this,"added to history",Toast.LENGTH_SHORT).show();

            }
        });
        startActivity(i);
    }

    public void opennointernet(){
        Intent i=new Intent(this,nointerner.class);
        startActivity(i);
    }
    public void opentemperature(){
        Intent i =new Intent(this,temperature.class);
        Bundle extras=new Bundle();
        extras.putString("JSON",JSON);
        extras.putString("address",address);
        i.putExtras(extras);
        type="temperature";
        map.put("type",type);
        dref.set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(translator.this,"added to history",Toast.LENGTH_SHORT).show();

            }
        });
        startActivity(i);
    }

    // location

    @SuppressLint("MissingPermission")
    private void getLastLocation(){
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.getLastLocation().addOnCompleteListener(
                        new OnCompleteListener<Location>() {
                            @Override
                            public void onComplete(@NonNull Task<Location> task) {
                                Location location = task.getResult();
                                if (location == null) {
                                    requestNewLocationData();
                                } else {
                                    longitde=location.getLongitude();
                                    latitude= location.getLatitude();
                                    //st.setText(latitude);
                                    new weatherTask().execute();

                                }
                            }
                        }
                );
            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            requestPermissions();
        }
    }


    @SuppressLint("MissingPermission")
    private void requestNewLocationData(){

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(0);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                Looper.myLooper()
        );

    }

    private LocationCallback mLocationCallback  = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            longitde=Math.toRadians(mLastLocation.getLongitude());
            latitude=Math.toRadians(mLastLocation.getLatitude());
           //st.setText(mLastLocation.getLatitude()+"");
            new weatherTask().execute();
        }
    };

    private boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_ID
        );
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        if (checkPermissions()) {
            getLastLocation();
        }

    }

    // get current address
    public void getCurrentaddress(double lat,double lon){
        Geocoder geo=new Geocoder(translator.this,Locale.getDefault());
        try{
            List<Address> address1=geo.getFromLocation(lat,lon,1);
                 address=address1.get(0).getSubLocality();
                st.setText(address);
                Log.d(TAG,lon+" lon");
                Log.d(TAG,address+" address");


        }catch (Exception e){
            Toast.makeText(translator.this,e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }


    // weather


    class weatherTask extends AsyncTask<String,Void,String>{

        double t;
        @Override
        protected void onPreExecute() {
            getCurrentaddress(latitude,longitde);
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            String response=Httprequest.excuteGet("https://api.openweathermap.org/data/2.5/weather?lat="+latitude+"&lon="+longitde+"&appid="+API);
            Log.d(TAG,longitde+" The longitude");
            Log.d(TAG,latitude+" The latitude");
            Log.d(TAG,""+response);
            JSON=response;
            return response;
        }
        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject jsonObj = new JSONObject(result);
                JSONObject main = jsonObj.getJSONObject("main");
                JSONObject sys = jsonObj.getJSONObject("sys");
                JSONObject wind = jsonObj.getJSONObject("wind");
                JSONObject weather = jsonObj.getJSONArray("weather").getJSONObject(0);

                Long updatedAt = jsonObj.getLong("dt");
                String updatedAtText = "Updated at: " + new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(new Date(updatedAt * 1000));
                String temp1 = main.getString("temp");
                t=Double.parseDouble(temp1);
                t=t-273;
                temp1=""+t;
                temp1=temp1.charAt(0)+""+temp1.charAt(1)+"°C";

                String weatherDescription = weather.getString("description");
                String mains=weather.getString("main");
                 Log.d(TAG,"mains"+mains);



                String address = jsonObj.getString("name") + ", " + sys.getString("country");

               if(mains.equals("Clear")){
                   i1.setImageResource(R.drawable.sun);
               }
               if(weatherDescription.equals("few clouds")){
                   i1.setImageResource(R.drawable.partly);

               }
                if(mains.equals("Clouds")){
                    i1.setImageResource(R.drawable.cloud);

                }
                if(mains.equals("Drizzle")){
                    i1.setImageResource(R.drawable.shower);

                }
                if(mains.equals("Rain")){
                    i1.setImageResource(R.drawable.rain);

                }
                if(mains.equals("Thunderstorm")){
                    i1.setImageResource(R.drawable.thundrr);

                }
                if(mains.equals("Snow")){
                    i1.setImageResource(R.drawable.snow);

                }
                if(mains.equals("Mist")||mains.equals("Smoke")||mains.equals("Haze")||mains.equals("Dust")||mains.equals("Fog")||mains.equals("Sand")||mains.equals("Ash")||mains.equals("Squall")||mains.equals("Tornado")){
                    i1.setImageResource(R.drawable.mist);

                }
                if(weatherDescription.equals("broken clouds")){
                    i1.setImageResource(R.drawable.cloud);
                }


                temp.setText(temp1);
               //st.setText(address);
               t1.setText(weatherDescription);





            } catch (JSONException e) {

            }
        }



    }




}
