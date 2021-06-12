package com.example.activty7;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EditTeman extends AppCompatActivity {
    TextView idTxt;
    EditText edNm, edTlp;
    Button edBtn;
    String id, nm, tlp, nmEdit, tlpEdit;
    int success;
    private static String url_update = "http://10.0.2.2:8080/umyTI/updateteman.php";
    private static final String TAG = EditTeman.class.getSimpleName();
    private static final String TAG_SUCCESS = "success";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_teman);

        idTxt = findViewById(R.id.textId);
        edNm = findViewById(R.id.editNm);
        edTlp = findViewById(R.id.editTlp);
        edBtn = findViewById(R.id.btnEdit);

        Bundle b = getIntent().getExtras();
        id = b.getString("id");
        nm = b.getString("nm");
        tlp = b.getString("tlp");

        idTxt.setText("ID : " + id);
        edNm.setText(nm);
        edTlp.setText(tlp);

        edBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditData();
            }
        });
    }

    public void EditData() {
        nmEdit = edNm.getText().toString();
        tlpEdit = edTlp.getText().toString();

        RequestQueue rq = Volley.newRequestQueue(getApplicationContext());
        StringRequest sr = new StringRequest(Request.Method.POST, url_update, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Respond : " + response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    success = obj.getInt(TAG_SUCCESS);
                    if (success == 1) {
                        Toast.makeText(EditTeman.this, "Sukses mengedit data", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(EditTeman.this, "Gagal", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error : " + error.getMessage());
                Toast.makeText(EditTeman.this, "Gagal Edit Data", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", id);
                params.put("nama", nmEdit);
                params.put("telp", tlpEdit);

                return params;
            }
        };
        rq.add(sr);
        CallHomeActivity();
    }

    public void CallHomeActivity() {
        Intent i = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(i);
        finish();
    }
}
