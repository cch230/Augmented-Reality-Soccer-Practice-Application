package com.example.arspapp_ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
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

public class sineup extends AppCompatActivity {

    MaterialEditText input_sineup_id,input_sineup_pw, input_sineup_name, input_sineup_email, input_sineup_birth, input_sineup_phone;
    Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sineup);

        input_sineup_id = findViewById(R.id.input_sineup_id);
        input_sineup_pw = findViewById(R.id.input_sineup_pw);
        input_sineup_name = findViewById(R.id.input_sineup_name);
        input_sineup_email = findViewById(R.id.input_sineup_email);
        input_sineup_birth = findViewById(R.id.input_sineup_birth);
        input_sineup_phone = findViewById(R.id.input_sineup_phone);
        register = findViewById(R.id.sinup_button);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String txtInput_sineup_id = input_sineup_id.getText().toString();
                String txtInput_sineup_pw = input_sineup_pw.getText().toString();
                String txtInput_sineup_name= input_sineup_name.getText().toString();
                String txtInput_sineup_email = input_sineup_email.getText().toString();
                String txtInput_sineup_birth = input_sineup_birth.getText().toString();
                String txtInput_sineup_phone = input_sineup_phone.getText().toString();

                if (TextUtils.isEmpty(txtInput_sineup_id) || TextUtils.isEmpty(txtInput_sineup_pw) || TextUtils.isEmpty(txtInput_sineup_name) || TextUtils.isEmpty(txtInput_sineup_email) || TextUtils.isEmpty(txtInput_sineup_birth)
                        || TextUtils.isEmpty(txtInput_sineup_phone)) {
                    Toast.makeText(sineup.this, "All fields required", Toast.LENGTH_SHORT).show();
                }else {

                    registerNewAccount(txtInput_sineup_id, txtInput_sineup_pw, txtInput_sineup_name, txtInput_sineup_email, txtInput_sineup_birth, txtInput_sineup_phone);
                }
            }
        });
    }

    private void registerNewAccount(final String input_sineup_id, final String input_sineup_pw, final String input_sineup_name, final String input_sineup_email, final String input_sineup_birth,
                                    final String input_sineup_phone) {

        final ProgressDialog progressDialog = new ProgressDialog(sineup.this);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(false);
        progressDialog.setTitle("Registering New Account");
        progressDialog.show();

        String uRl = "http://13.124.25.195//loginregister/register.php";
        StringRequest request = new StringRequest(Request.Method.POST, uRl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                if (response.equals("회원가입이 완료되었습니다.")) {
                    progressDialog.dismiss();
                    Toast.makeText(sineup.this, response, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(sineup.this, MainActivity.class));
                    finish();
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(sineup.this, response, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                progressDialog.dismiss();
                Toast.makeText(sineup.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                HashMap<String, String> param = new HashMap<>();
                param.put("input_sineup_id", input_sineup_id);
                param.put("input_sineup_pw", input_sineup_pw);
                param.put("input_sineup_name", input_sineup_name);
                param.put("input_sineup_phone", input_sineup_phone);
                param.put("input_sineup_birth", input_sineup_birth);
                param.put("input_sineup_email", input_sineup_email);

                return param;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getmInstance(sineup.this).addToRequestQueue(request);

    }
}
