package com.example.lokalizacja;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;



import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Collections;
import java.util.List;


public class Background extends AppCompatActivity {
    int NUMBER = 1;

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String SHARED_PREFS1 = "sharedPrefs";
    public static final String TEXT = "text";
    public static final String TEXT1 = "text1";
    public static final String SWITCH1 = "switch1";
    private String text, text1;
    private boolean switchOnOff;

    String FILE_SAVE = "location"+NUMBER+".txt";
    EditText edit_phone_number, edit_mail;
    TextView textphone, textmail;
    Button apply_text_button, save_button, apply_text_button1, save_button1;
    Switch switch1;
    int n_seconds, n_minutes, n_hour, n_day, n_sum;
    private static final String TAG = "Background";
    private Button buttonStartThread;
    private Handler mainHandler = new Handler();
    private volatile boolean stopThread = false;
    NumberPicker edit_text_input_back, edit_text_input_back_2, edit_text_input_back_3, edit_text_input_back_4;
    CheckBox check_sms, check_email, check_save;
    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_background);

        textphone = findViewById(R.id.textphone);
        textmail = findViewById(R.id.textmail);
        apply_text_button = findViewById(R.id.apply_text_button);
        save_button = findViewById(R.id.save_button);
        apply_text_button1 = findViewById(R.id.apply_text_button1);
        save_button1 = findViewById(R.id.save_button1);

        buttonStartThread = findViewById(R.id.button_start_thread);
        check_sms = findViewById(R.id.check_sms);
        check_email = findViewById(R.id.check_email);
        check_save = findViewById(R.id.check_save);

        edit_phone_number = findViewById(R.id.edit_phone_number);
        edit_mail = findViewById(R.id.edit_email);
        edit_text_input_back = (NumberPicker) findViewById(R.id.edit_text_input_back);
        edit_text_input_back.setMaxValue(60);
        edit_text_input_back.setMinValue(0);
        edit_text_input_back.setValue(0);
        edit_text_input_back_2 = (NumberPicker) findViewById(R.id.edit_text_input_back_2);
        edit_text_input_back_2.setMaxValue(60);
        edit_text_input_back_2.setMinValue(0);
        edit_text_input_back_2.setValue(0);
        edit_text_input_back_3 = (NumberPicker) findViewById(R.id.edit_text_input_back_3);
        edit_text_input_back_3.setMaxValue(24);
        edit_text_input_back_3.setMinValue(0);
        edit_text_input_back_3.setValue(0);
        edit_text_input_back_4 = (NumberPicker) findViewById(R.id.edit_text_input_back_4);
        edit_text_input_back_4.setMaxValue(999);
        edit_text_input_back_4.setMinValue(0);
        edit_text_input_back_4.setValue(0);

        apply_text_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textphone.setText(edit_phone_number.getText().toString());
            //    textmail.setText(edit_mail.getText().toString());
            }
        });
        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
            }
        });
        apply_text_button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textmail.setText(edit_mail.getText().toString());
            }
        });
        save_button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData1();
            }
        });
        edit_text_input_back.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
            n_seconds = i1;
            }
        });
        edit_text_input_back_2.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                n_minutes = 60 * i1;
            }
        });
        edit_text_input_back_3.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                n_hour = 3600 * i1;
            }
        });
        edit_text_input_back_4.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                n_day = 86400 * i1;
            }
        });

        check_sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        check_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        check_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        loadData();
        updateViews();
        loadData1();
        updateViews1();
    }

    private void updateViews1() {
         textmail.setText(text1);
    }

    private void loadData1() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS1, MODE_PRIVATE);
            text1 = sharedPreferences.getString(TEXT1, "");
    }

    private void saveData1() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS1, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(TEXT1, textmail.getText().toString());
        editor.apply();
        Toast.makeText(this, "Data saved", Toast.LENGTH_SHORT).show();
    }

    private void updateViews() {
        textphone.setText(text);
    }

    private void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        text = sharedPreferences.getString(TEXT, "");

    }

    private void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(TEXT, textphone.getText().toString());



        editor.apply();
        Toast.makeText(this, "Data saved", Toast.LENGTH_SHORT).show();
    }


    public void startThread(View view) {

        stopThread = false;
        n_sum = n_seconds + n_minutes + n_hour + n_day;
        if (n_sum == 0) {
            Toast.makeText(Background.this, "Please enter a positive number", Toast.LENGTH_SHORT).show();
            return;
        }
        ExampleRunnable runnable = new ExampleRunnable(n_sum);
        new Thread(runnable).start();
    }

    public void stopThread(View view) {
        stopThread = true;
        buttonStartThread.setEnabled(true);
    }



    public void background_back(View view) {
        Intent intent = new Intent(Background.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }


    class ExampleRunnable implements Runnable {
        int seconds;
        ExampleRunnable(int seconds) {
            this.seconds = seconds;
        }
        @Override
        public void run() {
            for (; ; ) {
                for (int i = 0; i < seconds; i++) {
                    if (stopThread)
                        return;
                    if (i == n_sum-1) {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(check_sms.isChecked() && textphone.length() > 0) {

                                    sendSMS();

                                }
                                if(check_sms.isChecked() && textphone.length() == 0){
                                    Toast.makeText(getApplicationContext(),"You have to write phone number", Toast.LENGTH_LONG).show();
                                }
                                if (check_save.isChecked()) {
                                    Intent intent = getIntent();
                                    Double lat = intent.getDoubleExtra("adres", 0);
                                    Double lon = intent.getDoubleExtra("adres2", 0);
                                    String adr = intent.getStringExtra("adres3");
                                    String location = ("Your adress: " + adr + ". " + "Your latitude: " + lat + ", " + "longtitude: " + lon+".");
                                    FileOutputStream fos = null;
                                    String FILE_NAME = "Saved location"+NUMBER+".txt";
                                    try {
                                        fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
                                        fos.write(location.getBytes());
                                        Toast.makeText(getApplicationContext(), "Saved to " + getFilesDir() + "/" + FILE_NAME,Toast.LENGTH_LONG).show();
                                        NUMBER++;
                                    } catch (FileNotFoundException e) {
                                        Toast.makeText(getApplicationContext(),"You have to turn on location", Toast.LENGTH_LONG).show();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    } finally {
                                        if (fos != null) {
                                            try {
                                                fos.close();
                                            } catch (IOException e) {
                                                Toast.makeText(getApplicationContext(),"You have to turn on location", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    }
                                }

                                if (check_email.isChecked() && textmail.length() > 0) {
                                    sendEmail();
                                }
                                if (check_email.isChecked() && textmail.length() == 0) {
                                    Toast.makeText(getApplicationContext(),"You have to write address email", Toast.LENGTH_LONG).show();
                                }


                                }


                        });
                    }
                    Log.d(TAG, "startThread: " + i);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        }


    public void sendSMS(){
        try {
            Intent intent = getIntent();
            Double lat = intent.getDoubleExtra("adres", 0);
            Double lon = intent.getDoubleExtra("adres2", 0);
            String adr = intent.getStringExtra("adres3");
            Intent it = new Intent(Intent.ACTION_SENDTO, Uri.parse("sms: " + textphone.getText().toString()));
            it.putExtra("sms_body", "Your adress: " + adr + ". " + "Your latitude: " + lat + ", " + "longtitude: " + lon+".");
            startActivity(it);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void sendEmail() {
        try {
            Intent intent = getIntent();
            Double lat = intent.getDoubleExtra("adres", 0);
            Double lon = intent.getDoubleExtra("adres2", 0);
            String adr = intent.getStringExtra("adres3");
            try {
                String Lokalizacja = "Your location:";
                Intent intent2 = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + textmail.getText().toString()));
                intent2.putExtra(Intent.EXTRA_SUBJECT, Lokalizacja);
                intent2.putExtra(Intent.EXTRA_TEXT, "Your address: " + adr + ". " + "Your latitude: " + lat + ", " + "longtitude: " + lon+".");
                startActivity(intent2);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}