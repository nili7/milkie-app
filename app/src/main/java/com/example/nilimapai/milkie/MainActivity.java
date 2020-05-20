package com.example.nilimapai.milkie;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    Button button1, button2;
    EditText value;
    TextView text;
    static final int READ_BLOCK_SIZE= 10000000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button1= ((Button)findViewById(R.id.button));
        button2= ((Button)findViewById(R.id.button2));
        value= ((EditText)findViewById(R.id.value1));
        text = (TextView) findViewById(R.id.textView);
        text.setMovementMethod(new ScrollingMovementMethod());

        try{
            FileInputStream filein= openFileInput("milkiefile.csv");
            InputStreamReader ipread= new InputStreamReader(filein);
            char[] inputBuffer= new char[READ_BLOCK_SIZE];
            String s= "";
            int charRead;

            while ((charRead= ipread.read(inputBuffer))>0){
                //char to string conversion
                String readstring= String.copyValueOf(inputBuffer, 0, charRead);
                s+= readstring;
                ipread.close();
                text.setText(s);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        addListeneronSave();
        sendEmail();

    }

    protected void sendEmail() {
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("Send email", "");

                Date now = new Date();
                String[] TO = {"pai.dilipm@gmail.com"};
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setData(Uri.parse("mailto:"));
                emailIntent.setType("text/plain");

                String sendText = "";
                try {
                    FileInputStream filein = openFileInput("milkiefile.csv");
                    InputStreamReader ipread = new InputStreamReader(filein);
                    char[] inputBuffer = new char[READ_BLOCK_SIZE];

                    int charRead;

                    while ((charRead = ipread.read(inputBuffer)) > 0) {
                        //char to string conversion
                        String readstring = String.copyValueOf(inputBuffer, 0, charRead);
                        sendText += readstring;
                        ipread.close();
                        }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Milkie data "+now);
                emailIntent.putExtra(Intent.EXTRA_TEXT, sendText);

                try {
                    startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                    finish();
                    Log.i("Finished sending email", "");
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(MainActivity.this,
                            "There is no email client installed.", Toast.LENGTH_SHORT).show();
                }
            }
            });
        }

    public void addListeneronSave()
    {
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String val= value.getText().toString();
                if (val!= "") {
                    SimpleDateFormat formatter = new SimpleDateFormat("dd_MM_yyyy");
                    Date now = new Date();
                    //String filename= formatter.format(now) + ".csv";

                    try {
                        String towrite = val + ", " + now + "\n";
                        FileOutputStream fileout = openFileOutput("milkiefile.csv", MainActivity.this.MODE_APPEND);
                        OutputStreamWriter opwriter = new OutputStreamWriter(fileout);
                        opwriter.append(towrite);
                        opwriter.close();

//                        File file = new File(getFilesDir(), "milkiefile.csv");
//                        FileOutputStream fileOutputStream = new FileOutputStream(file);
//                        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
//                        BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
//                        bufferedWriter.append(towrite);
                        Toast.makeText(MainActivity.this, "Saved successfully!", Toast.LENGTH_SHORT).show();

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this, "Not saved", Toast.LENGTH_SHORT).show();
                    }

                    try {
                        FileInputStream filein = openFileInput("milkiefile.csv");
                        InputStreamReader ipread = new InputStreamReader(filein);
                        char[] inputBuffer = new char[READ_BLOCK_SIZE];
                        String s = "";
                        int charRead;

                        while ((charRead = ipread.read(inputBuffer)) > 0) {
                            //char to string conversion
                            String readstring = String.copyValueOf(inputBuffer, 0, charRead);
                            s += readstring;
                            ipread.close();
                            text.setText(s);
                        }
                    } catch (Exception e) {
                        Toast.makeText(MainActivity.this, "Cannot find file", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
    }
}
