package com.example.lokalizacja;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;;
import java.io.OutputStreamWriter;
import java.util.List;

public class MainActivity<newW> extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    private String path = Environment.getExternalStorageDirectory().toString() + "/Location/Save";
    int NUMBER = 1;
    public static final int DEFAULT_UPDATE_INTERVAL = 5;
    public static final int FAST_UPDATE_INTERVAL = 1;
    private static final int PERMISSION_FINE_LOCATION = 99;
    public static int NEWWAY = 0;
    private TextView tv_lat, tv_lon, tv_sensor, tv_address;
    private Switch sw_locationsupdates, sw_gps;
    Button btn_newWaypoint, btn_showMap, btn_send, btn_background;

    Location currentlocation;

    List<Location> savedLocations;

    LocationRequest locationRequest;
    LocationCallback locationCallBack;
    FusedLocationProviderClient fusedLocationProviderClient;



    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_lat = (TextView) findViewById(R.id.tv_lat);
        tv_lon = (TextView) findViewById(R.id.tv_lon);
        tv_sensor = (TextView) findViewById(R.id.tv_sensor);
        tv_address = (TextView) findViewById(R.id.tv_address);
        sw_locationsupdates = (Switch) findViewById(R.id.sw_locationsupdates);
        sw_gps = (Switch) findViewById(R.id.sw_gps);
        btn_newWaypoint = (Button) findViewById(R.id.btn_newWayPoint);
        btn_showMap = (Button)findViewById(R.id.btn_showMap);

        btn_send = (Button) findViewById(R.id.btn_send);
        btn_background = (Button) findViewById(R.id.btn_background);
        locationRequest = new LocationRequest();
        locationRequest.setInterval(1000 * DEFAULT_UPDATE_INTERVAL);
        locationRequest.setFastestInterval(1000 * FAST_UPDATE_INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);




        locationCallBack = new LocationCallback() {



            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                Location location = locationResult.getLastLocation();
                updateUIValues(location);
            }
        };



        btn_newWaypoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if(sw_locationsupdates.isChecked()) {
                        newwa();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "You have to turn on location update", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(),"You have to turn on location updates again", Toast.LENGTH_LONG).show();
                }
            }
        });

        btn_showMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try{
                    if(sw_locationsupdates.isChecked()){
                        showm();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "You have to turn on location", Toast.LENGTH_LONG).show();
                    }
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(),"You have to turn on location updates again", Toast.LENGTH_LONG).show();

                }



            }
        });




        btn_background.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    if(sw_locationsupdates.isChecked()) {
                        sendToB();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "You have to turn on location update", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(),"You have to turn on location updates again", Toast.LENGTH_LONG).show();
                }

            }
        });

        sw_gps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sw_gps.isChecked()) {
                    locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                    tv_sensor.setText("Using GPS sensors");
                } else {
                    locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
                    tv_sensor.setText("Using Towers");

                }
            }
        });

        sw_locationsupdates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sw_locationsupdates.isChecked()) {
                    showSettingForLocation();

                    startLocationUpdates();
                } else {
                    stopLocationUpdates();
                }
            }
        });


        updateGPS();




    }

    public void showm(){
        try {
            Intent intent = new Intent(MainActivity.this, MapsActivity.class);
            startActivity(intent);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void newwa(){
        try {
            if(currentlocation.getLatitude() == 0){
                Toast.makeText(getApplicationContext(),"", Toast.LENGTH_LONG).show();
            }else {
                MyApplication myApplication = (MyApplication) getApplicationContext();
                savedLocations = myApplication.getMyLocations();
                savedLocations.add(currentlocation);
                btn_showMap.setEnabled(true);
            }
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"You have to turn on location updates again", Toast.LENGTH_LONG).show();
        }

    }

public void sendToB(){
    Intent intent = new Intent(MainActivity.this, Background.class);
    Geocoder geocoder1 = new Geocoder(MainActivity.this);
    List<Address> addresses = null;
    try {
        addresses = geocoder1.getFromLocation(currentlocation.getLatitude(), currentlocation.getLongitude(), 1);
    } catch (IOException e) {
        e.printStackTrace();
    }
    intent.putExtra("adres", currentlocation.getLatitude());
    intent.putExtra("adres2", currentlocation.getLongitude());
    intent.putExtra("adres3", addresses.get(0).getAddressLine(0));
    startActivity(intent);
}



    public void btn_send(View view){
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.popup_menu);
        popupMenu.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.item3:

                try{
                    if(sw_locationsupdates.isChecked()){
                        sendEmail();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "You need to turn on location", Toast.LENGTH_LONG).show();
                    }
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(),"You need to turn on location updates again", Toast.LENGTH_LONG).show();

                }

                return true;

            case R.id.item4:

                try {
                    if(sw_locationsupdates.isChecked()) {
                        sendSMS();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "You need to turn on location update", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(),"You need to turn on location updates again", Toast.LENGTH_LONG).show();
                }
                return true;
            default:
                return false;
        }

    }

    public void sendEmail(){
        Geocoder geocoder1 = new Geocoder(MainActivity.this);
        List<Address> addresses = null;
        try {
            addresses = geocoder1.getFromLocation(currentlocation.getLatitude(), currentlocation.getLongitude(), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String Lokalizacja = "Your location:";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:"));
            intent.putExtra(Intent.EXTRA_SUBJECT, Lokalizacja);
            intent.putExtra(Intent.EXTRA_TEXT, "Your adress: " + addresses.get(0).getAddressLine(0) + ". " + "Your latitude: " + currentlocation.getLatitude() + ", " + "longtitude: " + currentlocation.getLongitude() + ". ");
            startActivity(intent);
    }

    public void sendSMS(){
        Geocoder geocoder2 = new Geocoder(MainActivity.this);
        List<Address> addresses2 = null;
        try {
            addresses2 = geocoder2.getFromLocation(currentlocation.getLatitude(), currentlocation.getLongitude(), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Intent it = new Intent(Intent.ACTION_SENDTO, Uri.parse("sms:"));
        it.putExtra("sms_body", "Your adress: " + addresses2.get(0).getAddressLine(0) + ". " + "Your latitude: " + currentlocation.getLatitude() + ", " + "longtitude: " + currentlocation.getLongitude() + ". ");
        startActivity(it);
    }

    private void stopLocationUpdates() {
    //    tv_updates.setText("Location is not being tracked");
        tv_lat.setText("Not tracking location");
        tv_lon.setText("Not tracking location");
        tv_address.setText("Not tracking location");
        tv_sensor.setText("Not tracking location");

        fusedLocationProviderClient.removeLocationUpdates(locationCallBack);

    }

    private void startLocationUpdates() {
    //    tv_updates.setText("Location is being tracked");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallBack, null);
        updateGPS();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){

            case PERMISSION_FINE_LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                updateGPS();

                }
                else {
                    Toast.makeText(this,"This app requires permission to be granted in order to work properly", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;

        }
    }




    private void updateGPS(){

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MainActivity.this);
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        try{



                                updateUIValues(location);
                                currentlocation = location;

                        }catch (Exception e){
                        }

                    }
                });
            
        }
        else{
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.SEND_SMS},PERMISSION_FINE_LOCATION);
            }
        }
    }

    private void updateUIValues(Location location) {

        tv_lat.setText(String.valueOf(location.getLatitude()));
        tv_lon.setText(String.valueOf(location.getLongitude()));


        if (sw_gps.isChecked()) {
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            tv_sensor.setText("Using GPS sensors");
        } else {
            locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
            tv_sensor.setText("Using Towers");
        }

        Geocoder geocoder = new Geocoder(MainActivity.this);
        try{
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            tv_address.setText(addresses.get(0).getAddressLine(0));
        }catch (Exception e){
            tv_address.setText("Unable to get street adress");
        }

        MyApplication myApplication = (MyApplication) getApplicationContext();
        savedLocations = myApplication.getMyLocations();
try {
    btn_send.setEnabled(true);
    btn_newWaypoint.setEnabled(true);
    btn_background.setEnabled(true);
}catch (Exception d){
    Toast.makeText(getApplicationContext(),"You need to turn on location update", Toast.LENGTH_LONG).show();
}



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.example_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1:
                return true;
            case R.id.subitem1:
                Intent intent10 = new Intent(MainActivity.this, Accelerometer.class);
                startActivity(intent10);
                return true;
            case R.id.subitem2:
                Intent intent4 = new Intent(MainActivity.this, Gyroscope.class);
                startActivity(intent4);
                return true;

                case R.id.item2:


                    String FILE_NAME = "Location"+NUMBER+".txt";
                    try{
                            if(sw_locationsupdates.isChecked()){
                                Geocoder geocoder1 = new Geocoder(MainActivity.this);
                                List<Address> addresses = geocoder1.getFromLocation(currentlocation.getLatitude(), currentlocation.getLongitude(), 1);

                                String location = ("Your adress: " + addresses.get(0).getAddressLine(0) + ". " + "Your latitude: " + currentlocation.getLatitude() + ", "+ "longtitude: "+ currentlocation.getLongitude()+ ". ");
                                FileOutputStream fos = null;
                                try {
                                    if(NUMBER == NUMBER)
                                        NUMBER++;
                                    fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
                                    fos.write(location.getBytes());
                                    Toast.makeText(this, "Saved to " + getFilesDir() + "/" + FILE_NAME,
                                            Toast.LENGTH_LONG).show();
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                } finally {
                                    if (fos != null) {
                                        try {
                                            fos.close();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }
                            else {
                                Toast.makeText(getApplicationContext(), "You have to turn on location", Toast.LENGTH_LONG).show();
                            }
                    }catch (Exception e){
                        Toast.makeText(getApplicationContext(),"You have to turn on location", Toast.LENGTH_LONG).show();
                    }
                return true;
            case R.id.item5:
                Intent intent6 = new Intent(MainActivity.this, Load.class);
                startActivity(intent6);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    private void showSettingForLocation() {
        android.app.AlertDialog.Builder al=new AlertDialog.Builder(MainActivity.this);
        al.setTitle("Check location!");
        al.setMessage("Do you want to check if location is enabled?");
        al.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent=new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });
        al.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        al.show();
    }

}