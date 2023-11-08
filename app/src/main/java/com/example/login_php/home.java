package com.example.login_php;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class home extends AppCompatActivity {

    // creating variables on below line.
    private TextView responseTV;
    private TextView  questionTV;
    private TextInputEditText queryEdt;


    private String url = "https://api.openai.com/v1/completions";
    private String phpUrl = "http://172.16.26.93:81/Logeo/historial.php"; // Reemplaza con la URL de tu servidor PHP

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        responseTV = findViewById(R.id.responseTV);
        questionTV = findViewById(R.id.questionTV);
        queryEdt = findViewById(R.id.queryEdt);

        queryEdt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    responseTV.setText("Cargando...");

                    if (queryEdt.getText().toString().length() > 0) {
                        String query = queryEdt.getText().toString();
                        getResponseAndSaveData(query);
                    } else {
                        Toast.makeText(home.this, "Por favor ingresa tu consulta..", Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }
                return false;
            }
        });
    }

    private void getResponseAndSaveData(String query) {
        questionTV.setText(query);
        queryEdt.setText("");

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("model", "text-davinci-003");
            jsonObject.put("prompt", query);
            jsonObject.put("temperature", 0);
            jsonObject.put("max_tokens", 100);
            jsonObject.put("top_p", 1);
            jsonObject.put("frequency_penalty", 0.0);
            jsonObject.put("presence_penalty", 0.0);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String responseMsg = response.getJSONArray("choices").getJSONObject(0).getString("text");
                            responseTV.setText(responseMsg);

                            // Ahora, envía la pregunta y la respuesta al servidor PHP
                            sendQuestionAndResponseToServer(query, responseMsg);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("TAGAPI", "Error is : " + error.getMessage() + "\n" + error);
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json");
                params.put("Authorization", "Bearer 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");
                return params;
            }
        };

        postRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {
                // Método vacío, no se realiza ningún reintento.
            }
        });

        queue.add(postRequest);
    }

    private void sendQuestionAndResponseToServer(String question, String response) {
       // questionTV.setText("");
        queryEdt.setText("");
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        StringRequest postRequest = new StringRequest(Request.Method.POST, phpUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Maneja la respuesta del servidor si es necesario
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("TAGAPI", "Error al enviar datos al servidor: " + error.getMessage());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("question", question);
                params.put("response", response);
                return params;
            }
        };

        queue.add(postRequest);
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.itemHitorial:
                Toast.makeText(this, "Historial", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(home.this, historial.class);
                startActivity(intent);
                break;

            case R.id.itemPerfil:
                Toast.makeText(this, "Editar Perfil", Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent(home.this, perfil.class);
                startActivity(intent1);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    }


