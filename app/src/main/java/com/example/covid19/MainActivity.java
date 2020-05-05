package com.example.covid19;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    private TextView totalCases,activeCases,recoveryCases,stateName,stateCases,updated;
    ArrayList<String> states = new ArrayList<>();
    ArrayList<String> values  = new ArrayList<>();
    RecyclerView listView;
    Button refresh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        TODO : Step1 get api -- ok
//        TODO : step 2 install dependency volley ---> ok
//       TODO make a Json object an retrive the data -- > ok
//        Lets setup the TextView and othe nessary thing

        totalCases = findViewById(R.id.totalCount);
        activeCases = findViewById(R.id.activeCount);
        recoveryCases = findViewById(R.id.recoveryCount);
        stateCases = findViewById(R.id.stateCases);
        stateName = findViewById(R.id.stateName);
        listView = findViewById(R.id.list);
        refresh = findViewById(R.id.refresh);
        updated = findViewById(R.id.updated);
        LinearLayoutManager linearLayout = new LinearLayoutManager(getApplicationContext());
        listView.setLayoutManager(linearLayout);
        dataFetch();
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataFetch();
                Toast.makeText(getApplication(),"Refreshed",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void dataFetch() {
        String url  = "https://api.rootnet.in/covid19-in/stats/latest";
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                       Log.d("Response","Value"+response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            JSONObject data = new JSONObject(jsonObject.getString("data"));
                            JSONObject summary = new JSONObject(data.getString("summary"));
                            JSONArray unofficial_summary = data.getJSONArray("unofficial-summary");
                            JSONArray reg = data.getJSONArray("regional");
                            updated.setText(jsonObject.getString("lastRefreshed"));
                           String unSummary = unofficial_summary.get(0).toString();
                           JSONObject unsum = new JSONObject(unSummary);
                           Log.d("Val","Real :"+unsum.get("total"));
                           totalCases.setText(unsum.get("total").toString());
                           recoveryCases.setText(unsum.get("recovered").toString());
                           activeCases.setText(unsum.get("active").toString());
                            for(int i = 0;i<reg.length();i++) {
                                JSONObject index = reg.getJSONObject(i);
                                states.add(index.getString("loc"));
                                values.add(index.getString("totalConfirmed"));
                            }
                            Log.d("loc"," "+states);

                        }catch (Exception e){
                            Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
                        }

                        CustomAdapter customAdapter = new CustomAdapter(MainActivity.this,states,values);
                        listView.setAdapter(customAdapter);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Error :"+error,Toast.LENGTH_LONG).show();
            }
        });
        queue.add(stringRequest);
    }

}
