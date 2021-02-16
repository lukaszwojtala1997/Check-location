package com.example.lokalizacja;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class Gyroscope extends AppCompatActivity implements SensorEventListener {
    private static final String TAG = "Gyroscop";

    private SensorManager sensorManager;
    Sensor  mGyro;
    TextView textView4, textView5, textView6;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gyroscope);

        textView4 = (TextView) findViewById(R.id.textView4);
        textView5 = (TextView) findViewById(R.id.textView5);
        textView6 = (TextView) findViewById(R.id.textView6);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mGyro = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        if(mGyro != null){
            sensorManager.registerListener(Gyroscope.this, mGyro, SensorManager.SENSOR_DELAY_NORMAL);
            Log.d(TAG, "onCreate: Registered gyroscope listener");

        }
        else {
            textView4.setText("Gyroscope not Supported");
            textView5.setText("Gyroscope not Supported");
            textView6.setText("Gyroscope not Supported");
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor sensor = sensorEvent.sensor;
        if(sensor.getType() == Sensor.TYPE_GYROSCOPE){
            Log.d(TAG, "onSensorChanged: X:" + sensorEvent.values[0] + "Y" + sensorEvent.values[1] + "Z" + sensorEvent.values[2]);
            textView4.setText("XG: " + sensorEvent.values[0]);
            textView5.setText("YG: " + sensorEvent.values[1]);
            textView6.setText("ZG: " + sensorEvent.values[2]);
        }
    }

    public void gyroscope_back(View view) {
        Intent intent = new Intent(Gyroscope.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}