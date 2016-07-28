package com.example.ziyan.lyz_svm;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.TextView;

import com.example.ziyan.lyz_svm.svm.*;
import com.example.ziyan.lyz_svm.util.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;

import libsvm.svm;
import libsvm.svm_model;

public class MainActivity extends AppCompatActivity {
    private static final int animTime = 2000;
    private static final int sleepTime = 2000;

    public Button startButton;
    public TextView aText, resultText;
    public boolean isStart = false;
    public SensorManager sensorManager;
    public MySensorListener sensorListener;
    public MySVM svmTest;
    public int rangeID = R.raw.range;
    public int modelID = R.raw.model;

    /*protected void onStart() {
        super.onStart();
        new Thread(new Runnable() {
            @Override
            public void run() {
                threadSleep(sleepTime);
                setContentView(R.layout.activity_main);

                startButton = (Button)findViewById(R.id.start);
                aText = (TextView)findViewById(R.id.A);
                resultText = (TextView)findViewById(R.id.Result);

                InputStream isRange = getResources().openRawResource(rangeID);
                String[] rulArr= SvmUtil.myReadFileToArr(isRange);
                InputStream isModel = getResources().openRawResource(modelID);
                BufferedReader brModel = new BufferedReader(new InputStreamReader(isModel));
                // 读取SVM模型
                try {
                    svm_model svmModel = svm.svm_load_model(brModel);
                    svmTest = new MySVM(rulArr, svmModel);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
/*        final View view = View.inflate(
                this, R.layout.activity_splash, null);
        setContentView(view);
        AlphaAnimation animation = new AlphaAnimation(0.3f, 1.0f);
        animation.setDuration(animTime);
        view.startAnimation(animation);*/
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

//        final Intent intent = new Intent();
//        Timer timer = new Timer();
//        TimerTask task = new TimerTask() {
//            @Override
//            public void run() {
//                intent.setClass(MainActivity.this, JumpActivity.class);
//                MainActivity.this.startActivity(intent);
//            }
//        };
//        timer.schedule(task, animTime);
    }

    public void threadSleep(long timeout) {
        try {
            Thread.sleep(timeout);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void startClick(View v) {
        if (!isStart) {
            sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
            sensorListener = new MySensorListener();
            sensorManager.registerListener(sensorListener,
                    sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                    sensorManager.SENSOR_DELAY_FASTEST);
            isStart = true;
            startButton.setText("Stop");
        } else {
            sensorManager.unregisterListener(sensorListener);
            isStart = false;
            startButton.setText("Start");
        }
    }

    public class MySensorListener implements SensorEventListener {
        public double[] accArr = new double[128];
        public int currentIndex = 0;
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            if(sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
                float x = sensorEvent.values[0];
                float y = sensorEvent.values[1];
                float z = sensorEvent.values[2];
                double a = Math.sqrt((double)(x*x+y*y+z*z));
                DecimalFormat df = new DecimalFormat("0.00");
                aText.setText("A:" + df.format(a));
                if(currentIndex >= 128){
                    String[] data = SvmUtil.dataToFeaturesArr(accArr.clone());
                    double code = svmTest.myPredict(data);
                    resultText.setText("预测:"+Constant.actMapFromCode.get(code));
                    currentIndex = 0;
                }
                accArr[currentIndex++] = a;
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    }
}
