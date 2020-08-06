package com.example.arspapp_ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;
import java.util.Map;

public class signup extends AppCompatActivity {

    public MaterialEditText input_signup_id,input_signup_pw, input_signup_name, input_signup_email, input_signup_birth, input_signup_phone;
    Button register;
    public static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        input_signup_id = findViewById(R.id.input_signup_id);
        input_signup_pw = findViewById(R.id.input_signup_pw);
        input_signup_name = findViewById(R.id.input_signup_name);
        input_signup_email = findViewById(R.id.input_signup_email);
        input_signup_birth = findViewById(R.id.input_signup_birth);
        input_signup_phone = findViewById(R.id.input_signup_phone);
        register = findViewById(R.id.signup_button);
        context=this;

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String txtInput_signup_id = input_signup_id.getText().toString();
                String txtInput_signup_pw = input_signup_pw.getText().toString();
                String txtInput_signup_name= input_signup_name.getText().toString();
                String txtInput_signup_email = input_signup_email.getText().toString();
                String txtInput_signup_birth = input_signup_birth.getText().toString();
                String txtInput_signup_phone = input_signup_phone.getText().toString();

                if (TextUtils.isEmpty(txtInput_signup_id) || TextUtils.isEmpty(txtInput_signup_pw) || TextUtils.isEmpty(txtInput_signup_name) || TextUtils.isEmpty(txtInput_signup_email) || TextUtils.isEmpty(txtInput_signup_birth)
                        || TextUtils.isEmpty(txtInput_signup_phone)) {
                    Toast.makeText(signup.this, "All fields required", Toast.LENGTH_SHORT).show();
                }else {

                    registerNewAccount(txtInput_signup_id, txtInput_signup_pw, txtInput_signup_name, txtInput_signup_email, txtInput_signup_birth, txtInput_signup_phone);
                }
            }
        });
    }

    private void registerNewAccount(final String input_signup_id, final String input_signup_pw, final String input_signup_name, final String input_signup_email, final String input_signup_birth,
                                    final String input_signup_phone) {

        final ProgressDialog progressDialog = new ProgressDialog(signup.this);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(false);
        progressDialog.setTitle("회원가입");
        progressDialog.show();

        String uRl = "http://13.124.25.195//loginregister/register.php";
        StringRequest request = new StringRequest(Request.Method.POST, uRl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                if (response.equals("회원가입이 완료되었습니다")) {
                    progressDialog.dismiss();
                    Toast.makeText(signup.this, response, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(signup.this, MainActivity.class));
                    finish();
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(signup.this, response, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                progressDialog.dismiss();
                Toast.makeText(signup.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                HashMap<String, String> param = new HashMap<>();
                param.put("input_signup_id", input_signup_id);
                param.put("input_signup_pw", input_signup_pw);
                param.put("input_signup_name", input_signup_name);
                param.put("input_signup_phone", input_signup_phone);
                param.put("input_signup_birth", input_signup_birth);
                param.put("input_signup_email", input_signup_email);

                return param;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getmInstance(signup.this).addToRequestQueue(request);
    }
}