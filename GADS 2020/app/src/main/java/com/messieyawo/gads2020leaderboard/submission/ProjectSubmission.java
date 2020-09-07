package com.messieyawo.gads2020leaderboard.submission;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.messieyawo.gads2020leaderboard.MainActivity;
import com.messieyawo.gads2020leaderboard.R;
import com.messieyawo.gads2020leaderboard.model.GADS2020;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.List;

public class ProjectSubmission extends AppCompatActivity {

    private JsonArrayRequest request ;
    private RequestQueue requestQueue ;

    EditText fn,ln,em,link;
    String FirstName;
    String LastName ;
    String Email;
    String projectLink;
    String URL_SUBMIT_POST= "https://docs.google.com/forms/d/e/1FAIpQLSf9d1TcNU6zc6KR8bSEM41Z1g1zl35cwZr2xyjIhaMAz8WChQ/formResponseqwer";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_submission);
        Button btn_submit = findViewById(R.id.btn_submit);
        Toolbar toolbar3 = findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar3);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        }
      fn = findViewById(R.id.editTextTextPersonName);
        ln = findViewById(R.id.editTextTextPersonName5);
        em = findViewById(R.id.editTextTextPersonName3);
        link = findViewById(R.id.editTextTextPersonName4);




        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(fn.getText().toString().trim().isEmpty()){
                    Toast.makeText(ProjectSubmission.this,"First Name required",Toast.LENGTH_SHORT).show();
                }else if( ln.getText().toString().trim().isEmpty()){
                    Toast.makeText(ProjectSubmission.this,"Last Name required",Toast.LENGTH_SHORT).show();
                }else if(em.getText().toString().trim().isEmpty()){
                    Toast.makeText(ProjectSubmission.this,"Email address required",Toast.LENGTH_SHORT).show();
                }else if(link.getText().toString().trim().isEmpty()){
                    Toast.makeText(ProjectSubmission.this,"Github link required",Toast.LENGTH_SHORT).show();

                }else {

                    FirstName = fn.getText().toString();

                    LastName = ln.getText().toString();
                    Email = em.getText().toString();
                    projectLink = link.getText().toString();

                    SutmitFolder();
                }



            }
        });

    }

    private void SutmitFolder() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ProjectSubmission.this);
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        alertDialog.getWindow().setLayout(700, 700);
        LayoutInflater inflater = ProjectSubmission.this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.layout_name, null);
        alertDialog.getWindow().setContentView(dialogView);
        Button btnSubmit = dialogView.findViewById(R.id.btn_sure);
        ImageView tv = dialogView.findViewById(R.id.close);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                jsonrequest();
            }
        });



    }

    private void jsonrequest() {


        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Processing submission...");
        progressDialog.setIcon(R.drawable.upload);
        progressDialog.setMessage("Please wait a moment ");
        progressDialog.show();

        try {
            RequestQueue requestQueue = Volley.newRequestQueue(ProjectSubmission.this);

            JSONObject jsonBody = new JSONObject();
            jsonBody.put("entry.1877115667",FirstName);
            jsonBody.put("entry.2006916086",LastName);
            jsonBody.put("entry.1824927963",Email);
            jsonBody.put("entry.284483984",projectLink);

            final String requestBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST,  URL_SUBMIT_POST, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    LayoutInflater inflater = getLayoutInflater();

                    View view = inflater.inflate(R.layout.my_toast,
                            (ViewGroup) findViewById(R.id.mylayout));

                    Toast custToast = new Toast(ProjectSubmission.this);
                    custToast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                    custToast.setDuration(Toast.LENGTH_LONG);
                    custToast.setView(view);
                    custToast.show();

                   // Log.i("VOLLEY", response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    //Log.e("VOLLEY", error.toString());
                    LayoutInflater inflater = getLayoutInflater();
                    View view = inflater.inflate(R.layout.my_toast_fail,
                            (ViewGroup) findViewById(R.id.mylayout_fail));

                    Toast custToast = new Toast(ProjectSubmission.this);
                    custToast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                    custToast.setGravity(Gravity.FILL_HORIZONTAL, 0, 0);
                    custToast.setDuration(Toast.LENGTH_LONG);
                    custToast.setView(view);
                    custToast.show();
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return requestBody == null ? null : requestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                        return null;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                }
                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    String responseString = "";
                    if (response != null) {
                        responseString = String.valueOf(response.statusCode);
                        // can get more details such as response.headers
                    }
                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                }
            };
            requestQueue.add(stringRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}