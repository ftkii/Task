package com.example.task;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private Button logoutButton;
    private TextView jsonTextView;
    private RequestQueue requestQueue;

    @Override
    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logoutButton = findViewById(R.id.logoutButton);
        jsonTextView = findViewById(R.id.jsonTextView);

        // إعداد Queue لـ Volley
        requestQueue = Volley.newRequestQueue(this);

        // تحميل بيانات الـ JSON من الرابط
        loadJSONFromAPI();

        logoutButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }

    private void loadJSONFromAPI() {
        String url = "https://jsonplaceholder.typicode.com/todos/1";


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            // استخراج القيم من الـ JSON
                            int userId = response.getInt("userId");
                            int id = response.getInt("id");
                            String title = response.getString("title");
                            boolean completed = response.getBoolean("completed");


                            String jsonData = "User ID: " + userId + "\nID: " + id +
                                    "\nTitle: " + title + "\nCompleted: " + completed;
                            jsonTextView.setText(jsonData);


                            Log.d("MainActivity", "JSON Response: " + response.toString());
                        } catch (Exception e) {
                            e.printStackTrace();

                            Log.e("MainActivity", "Error parsing JSON: " + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // في حال حدوث خطأ
                        Toast.makeText(MainActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();

                        Log.e("MainActivity", "Volley error: " + error.getMessage());
                    }
                });

        requestQueue.add(jsonObjectRequest);
    }
}
