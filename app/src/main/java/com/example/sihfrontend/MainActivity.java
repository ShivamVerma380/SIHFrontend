package com.example.sihfrontend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity {

    private EditText email;
    private Button verify;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();


        email = findViewById(R.id.etInputMail);
        verify = findViewById(R.id.btnVerifyEmail);

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Verify Email",Toast.LENGTH_SHORT).show();
                if(email.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this,"Please enter your email!!",Toast.LENGTH_SHORT).show();
                }else{
                    //Intent verifyIntent = new Intent(MainActivity.this, VerifyEmail.class);
                    try {
                        //Map<String,String> map = new HashMap<>();
                        //map.put("email",email.getText().toString());

                        OkHttpClient client = new OkHttpClient();



                        RequestBody formBody = new FormBody.Builder()
                                .add("email", email.getText().toString())
                                .build();

                        Request request = new Request.Builder()
                                .url("http://ec2-35-169-161-33.compute-1.amazonaws.com:8080/verify-email")
                                .post(formBody)
                                .build();




                        Log.d("Before Response",request.toString());
                        Response response = client.newCall(request).execute();
                        Log.d("After response",response.toString());
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        Log.d("json object",jsonObject.toString());
                        String message = jsonObject.getString("message");
                        String otp = jsonObject.getString("otp");
                        Log.d("message",message);
                        Log.d("otp",otp);
                        if(otp!=null){
                            Intent verifyIntent = new Intent(getApplicationContext(), OTPVerification.class);
                            verifyIntent.putExtra("email",email.getText().toString());
                            verifyIntent.putExtra("otp",otp);
                            startActivity(verifyIntent);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                        Log.d("In catch:",""+e.getMessage());
                    }

                }
            }
        });

    }
}