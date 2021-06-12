package com.example.activty7;

import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class TambahTeman extends AppCompatActivity {
    private EditText edNama,edTelp;
    private Button saveBtn;
    String nm,tlp;
    int success;

    private static String url_insert = "http://10.0.2.2:8080/umyTI/tambahteman.php";
    private static final String TAG = TambahTeman.class.getSimpleName();
    private static final String TAG_SUCCESS = "success";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_teman);

        edNama = findViewById(R.id.edNama);
        edTelp = findViewById(R.id.edTelpon);
        saveBtn= findViewById(R.id.saveBtn);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                saveData();
            }

        });
    }

    public void saveData(){
        if (edNama.getText().toString().equals("") || edTelp.getText().toString().equals("")){
            Toast.makeText(TambahTeman.this,"Semua harus diisi data",Toast.LENGTH_SHORT).show();
        }else{
            nm = edNama.getText().toString();
            tlp= edTelp.getText().toString();

            RequestQueue rq = Volley.newRequestQueue(getApplicationContext());
            StringRequest sr = new StringRequest(Request.Method.POST,url_insert, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    Log.d(TAG, "Response : " + response.toString());
                    try {
                        JSONObject obj = new JSONObject(response);
                        success = obj.getInt(TAG_SUCCESS);
                        if (success == 1) {
                            Toast.makeText(TambahTeman.this, "Sukses simpan data", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            },new Response.ErrorListener(){
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG,"Error : "+error.getMessage());
                    Toast.makeText(TambahTeman.this,"Gagal simpan data",Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                protected Map<String,String> getParams(){
                    Map<String,String> params = new HashMap<>();
                    params.put("nama",nm);
                    params.put("telpon",tlp);

                    return params;
                }
            };
            rq.add(sr);
            CallHomeActivity();
        }
    }
    public void CallHomeActivity() {
        Intent i = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(i);
        finish();
    }
}