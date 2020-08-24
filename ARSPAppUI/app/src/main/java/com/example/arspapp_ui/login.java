package com.example.arspapp_ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class login extends AppCompatActivity {

    MaterialEditText input_id, input_pw;
    TextView sign_text;
    Button login_in_login;
    SharedPreferences sharedPreferences;

    private ArrayList<PersonalData> mList = null;
    private Activity context = null;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPreferences = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        input_id = findViewById(R.id.input_id);
        input_pw = findViewById(R.id.input_pw);
        login_in_login = findViewById(R.id.login_in_login);
        sign_text = findViewById(R.id.sign_text);

        sign_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(login.this, signup.class));
                finish();
            }
        });

        login_in_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String txtInput_id = input_id.getText().toString();

                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString("ID", txtInput_id); // key 값, data 값
                editor.commit();

                String txtInput_pw = input_pw.getText().toString();
                if (TextUtils.isEmpty(txtInput_id) || TextUtils.isEmpty(txtInput_pw)) {
                    Toast.makeText(login.this, "아이디와 비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show();
                } else {
                    login(txtInput_id, txtInput_pw);
                }
            }
        });
    }

    private void login(final String input_id, final String input_pw) {
        final ProgressDialog progressDialog = new ProgressDialog(login.this);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(false);
        progressDialog.setTitle("Registering New Account");
        progressDialog.show();

        String uRl = "http://13.124.25.195//phpFiles/login.php";
        StringRequest request = new StringRequest(Request.Method.POST, uRl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                if (response.equals("로그인 되었습니다.")) {
                    progressDialog.dismiss();
                    Toast.makeText(login.this, response, Toast.LENGTH_SHORT).show();
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    editor.apply();
                    startActivity(new Intent(login.this, bottombar.class));
                    finish();
                }

                else {
                    progressDialog.dismiss();
                    Toast.makeText(login.this, response, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Toast.makeText(login.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                HashMap<String, String> param = new HashMap<>();

                param.put("input_id", input_id);
                param.put("input_pw", input_pw);

                return param;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getmInstance(login.this).addToRequestQueue(request);
    }
}
