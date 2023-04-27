package com.example.meteoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;
    private static final int REGIONAL_INDICATOR_OFFSET = 0x1F1A5;

    private TextView textLatLong;
    private TextView temp;
    private TextView WindSpeed;
    private TextView WindDirection;
    private String Color_bg;

    private int WindDirectionSave=0;
    private TextView Weathercode;

    private TextView Ville;

    private ProgressBar progressBar;

    private double latitude;
    private double longitute;

    private LocationCallback locationCallback;
    private View MainAcc_view;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textLatLong = (TextView) findViewById(R.id.textLatLong);
        temp = (TextView) findViewById(R.id.Temperature);
        Weathercode = (TextView) findViewById(R.id.Weathercode);
        WindDirection = (TextView) findViewById(R.id.WindDirection);
        WindSpeed= (TextView) findViewById(R.id.Windspeed);
        Ville=(TextView) findViewById(R.id.Ville);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        MainAcc_view = findViewById(R.id.myView);


        if (ContextCompat.checkSelfPermission(
                getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE_LOCATION_PERMISSION
            );
        } else {
            getCurrentLocation();
        }


        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    Charge();
                    latitude= location.getLatitude();
                    longitute= location.getLongitude();

                    try {
                        getMeteo();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Affiche();
                }
            }
        };

    }


    @Override
    protected void onResume() {
        super.onResume();
        getCurrentLocation();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                Toast.makeText(this, "La Permission a était refusée!", Toast.LENGTH_SHORT).show();
                latitude = 47.66;
                longitute = -2.76;
            }
        }
    }


    private void getCurrentLocation() {

        Charge();
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.getFusedLocationProviderClient(MainActivity.this)
                .requestLocationUpdates(locationRequest, new LocationCallback() {

                    @Override
                    public void onLocationResult(@NonNull LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        LocationServices.getFusedLocationProviderClient(MainActivity.this)
                                .removeLocationUpdates(this);
                        if (locationResult != null && locationResult.getLocations().size() > 0) {
                            int latestLocationIndex = locationResult.getLocations().size() - 1;
                            double latitud = locationResult.getLocations().get(latestLocationIndex).getLatitude();
                            double longitude = locationResult.getLocations().get(latestLocationIndex).getLongitude();
                            //textLatLong.setText(String.format(
                           //         "Lat : %s\n,Lon : %s\n", latitude, longitude
                            //));

                            latitude=latitud;
                            longitute=longitude;
                            try {
                                getMeteo();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                        }
                        Affiche();


                    }
                }, Looper.getMainLooper());







    }




    public void getMeteo() throws IOException {
        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                "https://api.open-meteo.com/v1/forecast?latitude="+latitude+"&longitude="+longitute+"&current_weather=true", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject newjsonObject=null;
                    try {
                         newjsonObject = new JSONObject(response.getString("current_weather"));
                    }catch (JSONException err){
                        Toast.makeText(getApplicationContext(), "la requete n'as pas pu se réaliser!", Toast.LENGTH_SHORT).show();

                    }
                    String temperature;
                    String windspeed;
                    String winddirection;
                    String weathercode;
                    temperature = (String)(newjsonObject.getString("temperature"));
                    windspeed = (String)(newjsonObject.getString("windspeed"));
                    winddirection = (String)(newjsonObject.getString("winddirection"));
                    weathercode = (String)(newjsonObject.getString("weathercode"));

                    MeteoData donnee = new MeteoData(temperature,windspeed,winddirection,weathercode);
                    temp.setText(donnee.getTemperature());
                    Weathercode.setText(donnee.getWeathercode());
                    WindDirection.setText(donnee.getWinddirection());
                    WindDirectionSave=donnee.getWindDirectionDegree();
                    WindSpeed.setText(donnee.getWindspeed());

                    Color_bg = donnee.Color_bg;
                    Color_bg = Color_bg.toUpperCase();
                    MainAcc_view.setBackgroundColor(Color.parseColor(Color_bg));


                    try {
                        getVille();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "The request did not work", Toast.LENGTH_SHORT).show();
            }
        });


        requestQueue.add(jsonObjectRequest);

    }

    public void getVille() throws IOException{
        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                "https://api.bigdatacloud.net/data/reverse-geocode-client?latitude="+latitude+"&longitude="+longitute+"&localityLanguage=en", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                     Ville.setText(response.getString("city") +" "+ getCountryEmoji(response.getString("countryCode")));
                }catch (JSONException err){
                    Toast.makeText(getApplicationContext(), "The request to retieve the Town does not end well", Toast.LENGTH_SHORT).show();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "The request to retieve the Town crashed", Toast.LENGTH_SHORT).show();
            }
        });


        requestQueue.add(jsonObjectRequest);
    }


    public String getCountryEmoji(String countryCode){
        String unicode="";
        for (char lettre: countryCode.toCharArray()) {
            unicode+=getRegionalIndicatorSymbol(lettre);
        }
        return unicode;
    }

    public static String getRegionalIndicatorSymbol(final char character) {
        if (character < 'A' || character > 'Z') {
            throw new IllegalArgumentException("Invalid character: you must use A-Z");
        }
        return String.valueOf(Character.toChars(REGIONAL_INDICATOR_OFFSET + character));
    }



    public void Charge(){
        findViewById(R.id.btn_search).setVisibility(View.GONE);
        findViewById(R.id.btn_about).setVisibility(View.GONE);
        findViewById(R.id.imageView).setVisibility(View.GONE);

        findViewById(R.id.Weathercode).setVisibility(View.GONE);
        findViewById(R.id.Windspeed).setVisibility(View.GONE);
        findViewById(R.id.Temperature).setVisibility(View.GONE);

        findViewById(R.id.WindDirection).setVisibility(View.GONE);
        findViewById(R.id.WindDirImage).setVisibility(View.GONE);


        findViewById(R.id.Ville).setVisibility(View.GONE);

        findViewById(R.id.progressBar).setVisibility(View.VISIBLE);



    }

    public void Affiche(){
        ImageView WinDir=findViewById(R.id.WindDirImage);
        rotateImage(WinDir,WindDirectionSave);

        findViewById(R.id.btn_search).setVisibility(View.VISIBLE);
        findViewById(R.id.btn_about).setVisibility(View.VISIBLE);
        //findViewById(R.id.imageView).setVisibility(View.VISIBLE);

        findViewById(R.id.Weathercode).setVisibility(View.VISIBLE);
        findViewById(R.id.Windspeed).setVisibility(View.VISIBLE);
        findViewById(R.id.Temperature).setVisibility(View.VISIBLE);




        WinDir.setVisibility(View.VISIBLE);



        findViewById(R.id.Ville).setVisibility(View.VISIBLE);


        findViewById(R.id.progressBar).setVisibility(View.GONE);


    }


    private void rotateImage(ImageView imageView,float deg) {

        Matrix matrix = new Matrix();
        matrix.postRotate(deg);

        Bitmap myImg = BitmapFactory.decodeResource(getResources(), R.drawable.image_wind_direction);
        Bitmap rotated = Bitmap.createBitmap(myImg, 0, 0, myImg.getWidth(), myImg.getHeight(),
                matrix, true);
        imageView.setImageBitmap(rotated);
        imageView.setRotation(deg); // ne marche pas totalement fait uniquement Haut , Droit , Bas , Gauche
    }




}