package com.example.login_php;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class perfil extends AppCompatActivity {

    private TextView txtName, txtEmail, txtPassword, txtSchool, txtid;
    private Button btnEdit;

    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        txtName = findViewById(R.id.txtName);
        txtEmail = findViewById(R.id.txtEmail);
        txtPassword = findViewById(R.id.txtPassword);
        txtSchool = findViewById(R.id.txtSchool);
        txtid = findViewById(R.id.txtid);
        btnEdit = findViewById(R.id.btnEdit);

        SharedPreferences preferences = getSharedPreferences("MY_PREFS", MODE_PRIVATE);
        String userID = preferences.getString("USER_ID", ""); // Obtén el ID del usuario desde SharedPreferences

        String url = "http://172.16.26.93:81/Logeo/bus.php?id=" + userID;
        RequestQueue queue = Volley.newRequestQueue(this);

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    String id = response.getString("id");
                                    String email = response.getString("email");
                                    String password = response.getString("password");
                                    String name = response.getString("name");
                                    String school = response.getString("school");

                                    // Actualiza los EditText con los datos del usuario
                                    txtid.setText(id);
                                    txtEmail.setText(email);
                                    txtPassword.setText(password);
                                    txtName.setText(name);
                                    txtSchool.setText(school);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // Maneja errores de solicitud
                                Toast.makeText(perfil.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                queue.add(request);
            }

        });
    }
}
