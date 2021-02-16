package com.example.lokalizacja;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Environment;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;


public class Load extends AppCompatActivity {
    int NUMBER = 1;

    TextView load;
    String FILE_NAME = "Location" +NUMBER+".txt";
    private String interlude = "\n\n\n////---------/////-------\n\n\n";
    private String path = "data/user/0/com.example.lokalizacja/files/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);

        load = (TextView) findViewById(R.id.load);
        load.setText(getAllContent());
    }


    private String getAllContent() {
        try {
            File file = new File(path);
            String[] paths = file.list(new FilenameFilter() {
                @Override
                public boolean accept(File file, String s) {
                    return s.endsWith(".txt");
                }
            });
            StringBuilder sb = new StringBuilder();
            for(int i = 0; i < paths.length; i++){
                sb.append(Files.toString(new File(path+paths[i]), Charsets.UTF_8));
                sb.append(interlude);
            }
            return sb.toString();
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), "The folder is empty",Toast.LENGTH_LONG).show();
            return "";
        }
    }


}