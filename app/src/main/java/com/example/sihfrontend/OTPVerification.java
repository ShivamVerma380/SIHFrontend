package com.example.sihfrontend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sihfrontend.register.RegisterActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OTPVerification extends AppCompatActivity {

    private String email;
    private String otp;

    private EditText otpBox1;
    private EditText otpBox2;
    private EditText otpBox3;
    private EditText otpBox4;
    private EditText otpBox5;
    private EditText otpBox6;

    private CountDownTimer mCountDownTimer;

    private boolean mTimerRunning;

    private long mTimeLeftInMillis = 35000;
    private  TextView resendOTP;

    private TextView mTextViewCountDown;

    private Button submitButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification);
        getSupportActionBar().hide();

        Intent intent = getIntent();
        email = intent.getStringExtra("email");
        otp = intent.getStringExtra("otp");

        otpBox1 = findViewById(R.id.etinputotp1);
        otpBox2 = findViewById(R.id.etinputotp2);
        otpBox3 = findViewById(R.id.etinputotp3);
        otpBox4 = findViewById(R.id.etinputotp4);
        otpBox5 = findViewById(R.id.etinputotp5);
        otpBox6 = findViewById(R.id.etinputotp6);

        mTextViewCountDown = findViewById(R.id.tvCountdown);

        resendOTP = findViewById(R.id.tvresend_otp);
        submitButton = findViewById(R.id.btnSubmitOtp);

        startTimer();
        setUpOTPInputs();



        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(otpBox1.getText().toString().isEmpty() || otpBox2.getText().toString().isEmpty() || otpBox3.getText().toString().isEmpty() || otpBox4.getText().toString().isEmpty() || otpBox5.getText().toString().isEmpty() || otpBox6.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"Please enter 6 digit otp",Toast.LENGTH_SHORT).show();
                }else{
                    String entered_otp = otpBox1.getText().toString()+otpBox2.getText().toString()+otpBox3.getText().toString()+otpBox4.getText().toString()+otpBox5.getText().toString()+otpBox6.getText().toString();
                    if(otp.equals(entered_otp)){
                        Intent registerIntent = new Intent(OTPVerification.this, RegisterActivity.class);
                        registerIntent.putExtra("email",email);
                        startActivity(registerIntent);
                    }else{
                        Toast.makeText(getApplicationContext(),"Please enter correct otp",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        resendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTimer();
                sendOTPAgain();
            }
        });



    }



    private  void setUpOTPInputs(){
        otpBox1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty()){
                    otpBox2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        otpBox2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty()){
                    otpBox3.requestFocus();
                }else{
                    otpBox1.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        otpBox3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty()){
                    otpBox4.requestFocus();
                }else{
                    otpBox2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        otpBox4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty()){
                    otpBox5.requestFocus();
                }else{
                    otpBox3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        otpBox5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty()){
                    otpBox6.requestFocus();
                }else{
                    otpBox4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        otpBox6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().trim().isEmpty()){
                    otpBox5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void sendOTPAgain() {
        try{
            OkHttpClient client = new OkHttpClient();
            RequestBody formBody = new FormBody.Builder()
                    .add("email",email)
                    .build();
            Request request = new Request.Builder()
                    .url("http://ec2-35-169-161-33.compute-1.amazonaws.com:8080/verify-email")
                    .post(formBody)
                    .build();

            Log.d("Before Response",request.toString());

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if(response.isSuccessful()){
                        try {
                            JSONObject jsonObject = new JSONObject(response.body().string());
                            String message = jsonObject.getString("message");
                            otp = jsonObject.getString("otp");
                            Log.d("message:",message);
                            Log.d("otp:",otp);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void startTimer() {
        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                mTimerRunning = false;
                mTimeLeftInMillis = 35000;
                resendOTP.setVisibility(View.VISIBLE);
            }

        }.start();
        mTimerRunning = true;
        resendOTP.setVisibility(View.INVISIBLE);
    }

    private void updateCountDownText() {
        int minutes = (int) (mTimeLeftInMillis / 1000) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

        mTextViewCountDown.setText(timeLeftFormatted);
    }

}