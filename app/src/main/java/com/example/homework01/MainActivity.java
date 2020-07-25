package com.example.homework01;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    Button calButton;
    TextView mintext,maxtext,averagetext,seekbarValue,tvmin,tvmax,tvavg;
    SeekBar seekBar;
    ProgressBar progressBar;
    Handler handler;
    ExecutorService threadPool;
    Double minimum,maximum,average;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        threadPool = Executors.newFixedThreadPool(2);
        calButton = findViewById(R.id.button);
        mintext = findViewById(R.id.valueMinimum);
        maxtext = findViewById(R.id.valueMaximum);
        averagetext = findViewById(R.id.valueAverage);
        tvmin = findViewById(R.id.textView3);
        tvmax = findViewById(R.id.textView4);
        tvavg = findViewById(R.id.textView5);
        seekBar = findViewById(R.id.seekBar);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        seekbarValue = findViewById(R.id.seekbarvalue);
        seekBar.setMax(10);
        seekBar.setMin(0);
        seekbarValue.setText(0 + " times");
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekbarValue.setText(progress + " " + "times");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        calButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int times = seekBar.getProgress();
                if (times > 0) {
                    threadPool.execute(new doCalculation(times));
                    progressBar.setVisibility(View.VISIBLE);
                    mintext.setVisibility(View.INVISIBLE);
                    maxtext.setVisibility(View.INVISIBLE);
                    averagetext.setVisibility(View.INVISIBLE);
                    tvmin.setVisibility(View.INVISIBLE);
                    tvmax.setVisibility(View.INVISIBLE);
                    tvavg.setVisibility(View.INVISIBLE);
                    handler = new Handler(new Handler.Callback() {
                        @Override
                        public boolean handleMessage(@NonNull Message msg) {
                            // logics
                            return false;
                        }
                    });
                } else
                    Toast.makeText(MainActivity.this, "Enter the value greater than zero", Toast.LENGTH_SHORT).show();

            }
        });
    }

        class doCalculation implements Runnable {
            int value;
            public doCalculation(int times) {
                value=times;
            }

            @Override
            public void run() {
                ArrayList<Double> getResult=new ArrayList<Double>();
                getResult=HeavyWork.getArrayNumbers(value);
                minimum=getMinimum(getResult);
                maximum=getMaximum(getResult);
                average=getAverage(getResult);
                // cre
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.INVISIBLE);
                        mintext.setText(""+minimum);
                        maxtext.setText(""+maximum);
                        averagetext.setText(""+average);
                        mintext.setVisibility(View.VISIBLE);
                        maxtext.setVisibility(View.VISIBLE);
                        averagetext.setVisibility(View.VISIBLE);
                        tvmin.setVisibility(View.VISIBLE);
                        tvmax.setVisibility(View.VISIBLE);
                        tvavg.setVisibility(View.VISIBLE);
                    }
                });
            }
        }

    private double getAverage(ArrayList<Double> doubles) {
        double sum=0.0;
        for(double value:doubles){
            sum = sum + value;
        }
        return sum/doubles.size();
    }

    private double getMaximum(ArrayList<Double> doubles) {
        double max=Double.MIN_VALUE;
        for(double value:doubles){
            if(max<value)
                max=value;
        }
        return  max;
    }

    private double getMinimum(ArrayList<Double> doubles) {
        double min=Double.MAX_VALUE;
        for(double value:doubles){
            if(min>value)
                min=value;
        }
        return  min;
    }



}
