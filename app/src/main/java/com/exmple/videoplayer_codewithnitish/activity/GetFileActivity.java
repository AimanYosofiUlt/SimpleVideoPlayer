package com.exmple.videoplayer_codewithnitish.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.exmple.videoplayer_codewithnitish.Parsers.Str;
import com.exmple.videoplayer_codewithnitish.Parsers.StrParser;
import com.exmple.videoplayer_codewithnitish.R;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class GetFileActivity extends AppCompatActivity {
    TextView textView, textViewC, textViewsubtitle;
    Button button, next, prev;

    ArrayList<Str> strs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_file);

        textView = findViewById(R.id.textView);
        textViewC = findViewById(R.id.textViewC);
        textViewsubtitle = findViewById(R.id.textViewsubtitle);
        button = findViewById(R.id.button);
        next = findViewById(R.id.next);
        prev = findViewById(R.id.prev);

        button.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            startActivityForResult(intent, 1);
        });

        next.setOnClickListener(v -> {
            if (i < strs.size() - 1) {
                i = strs.size() - 1;
                setSub();
            }
        });

        prev.setOnClickListener(v -> {
            if (i > 0) {
                i--;
                setSub();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            Context context = GetFileActivity.this;

            String path = RealPathUtils.getRealPath(context, uri);
            textView.setText("Uri = " + uri.toString() + "\n\n" + "path = " + path);
            File file = new File(path);
            StringBuilder myDataString = new StringBuilder();
            try {
                FileInputStream fis = new FileInputStream(file);
                DataInputStream in = new DataInputStream(fis);
                BufferedReader br = new BufferedReader(new InputStreamReader(in));

                String strLine;
                while ((strLine = br.readLine()) != null) {
                    myDataString.append(strLine).append("\n");
                }
                br.close();
                in.close();
                fis.close();
            } catch (IOException e) {
                Toast.makeText(GetFileActivity.this, "Fiel error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            textViewC.setText(myDataString.toString());
            strs = StrParser.parse(myDataString.toString());
            i = 0;
            setSub();
        }
    }

    int i = 0;

    void setSub() {
        textViewsubtitle.setText(
                strs.get(i).getNum() + "\n" +
                        strs.get(i).getText() + "\n" +
                        strs.get(i).getStartTime() + " --> " +
                        strs.get(i).getEndTime());
    }

}