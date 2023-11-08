package com.example.login_php;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class login extends AppCompatActivity {

    EditText email, contraseña;
    Button btn_login;

    String str_email,str_password;
    String url = "http://172.16.26.93:81/Logeo/logeoen.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        email = findViewById(R.id.etemail);
        contraseña = findViewById(R.id.etcontraseña);
        btn_login = findViewById(R.id.btn_login);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(email.getText().toString().equals("")){
                    Toast.makeText(login.this, "Enter Email", Toast.LENGTH_SHORT).show();
                }
                else if(contraseña.getText().toString().equals("")){
                    Toast.makeText(login.this, "Enter Password", Toast.LENGTH_SHORT).show();
                }
                else{
                    Login();
                    Intent intent = new Intent(login.this, home.class);
                    startActivity(intent);
                }

            }
        });



    }
    public void Login() {



            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Por favor espera...");

            progressDialog.show();

            str_email = email.getText().toString().trim();
            str_password = contraseña.getText().toString().trim();


            StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    progressDialog.dismiss();

                    if (response.contains("Ingresaste correctamente")) {
                        // Dividir la respuesta para obtener el mensaje y el ID del usuario
                        String[] separated = response.split("\\|");
                        String successMessage = separated[0];
                        String userID = separated[1];

                        // Resto de tu lógica

                        if (successMessage.equals("Ingresaste correctamente")) {
                            // Realiza las operaciones con el ID del usuario
                            // Puedes utilizar la variable 'userID' aquí
                            // Por ejemplo, para guardar el ID del usuario en SharedPreferences:
                            SharedPreferences preferences = getSharedPreferences("MY_PREFS", MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("USER_ID", userID);
                            editor.apply();

                            email.setText("");
                            contraseña.setText("");

                            Intent intent = new Intent(login.this, home.class);
                            startActivity(intent);
                            Toast.makeText(login.this, successMessage, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(login.this, response, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            },new Response.ErrorListener(){

                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    Toast.makeText(login.this, error.getMessage().toString(), Toast.LENGTH_SHORT).show();
                }
            }

            ){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> params = new HashMap<String, String>();
                    params.put("email",str_email);
                    params.put("password",str_password);
                    return params;

                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(login.this);
            requestQueue.add(request);


        }

    public void moveToRegistration(View view) {
        startActivity(new Intent(getApplicationContext(), registrar.class));
        finish();
    }
}