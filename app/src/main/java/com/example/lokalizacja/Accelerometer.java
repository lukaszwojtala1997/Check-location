package com.example.lokalizacja;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class Accelerometer extends AppCompatActivity implements SensorEventListener {
    private static final String TAG = "Accelerometr";
    private SensorManager sensorManager;
    Sensor accelerometer;
    TextView textview1, textView2, textView3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accelerometer);

        textview1 = (TextView) findViewById(R.id.textView1);
        textView2 = (TextView) findViewById(R.id.textView2);
        textView3 = (TextView) findViewById(R.id.textView3);

        Log.d(TAG, "onCreate: Initializing Sensor Services");
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        if(accelerometer != null){
            sensorManager.registerListener(Accelerometer.this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
            Log.d(TAG, "onCreate: Registered accelerometer listener");

        }
        else {
            textview1.setText("Accelometer not Supported");
            textView2.setText("Accelometer not Supported");
            textView3.setText("Accelometer not Supported");
        }


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor sensor = sensorEvent.sensor;
        if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            Log.d(TAG, "onSensorChanged: X:" + sensorEvent.values[0] + "Y" + sensorEvent.values[1] + "Z" + sensorEvent.values[2]);
            textview1.setText("X: " + sensorEvent.values[0]);
            textView2.setText("Y: " + sensorEvent.values[1]);
            textView3.setText("Z: " + sensorEvent.values[2]);
        }
    }
    public void accelerometer_back(View view) {
        Intent intent = new Intent(Accelerometer.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}