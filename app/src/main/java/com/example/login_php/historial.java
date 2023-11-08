package com.example.login_php;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class historial extends AppCompatActivity {

    private TextView textViewData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial);

        textViewData = findViewById(R.id.textViewData);

        // Ejecutar el AsyncTask para obtener los datos
        new GetDataFromServer().execute();
    }

    // AsyncTask para realizar la solicitud HTTP en segundo plano
    private class GetDataFromServer extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL("http://172.16.26.93:81/Logeo/hhhh.php");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                try {
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                    return result.toString();
                } finally {
                    urlConnection.disconnect();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                // Procesar los datos JSON y mostrar en el TextView
                try {
                    JSONArray jsonArray = new JSONArray(result);
                    StringBuilder data = new StringBuilder();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        // Suponiendo que 'nombre' es una clave en tu JSON
                        String question = jsonObject.getString("question");
                        String response = jsonObject.getString("response");
                        // Agregar el nombre al texto a mostrar
                        data.append(question).append("\n");
                        data.append(response).append("\n");
                    }

                    // Mostrar los datos en el TextView
                    textViewData.setText(data.toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                textViewData.setText("Error al obtener los datos");
            }
        }
    }
}
