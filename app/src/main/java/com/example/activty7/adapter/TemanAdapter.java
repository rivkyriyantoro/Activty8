package com.example.activty7.adapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.activty7.EditTeman;
import com.example.activty7.MainActivity;
import com.example.activty7.R;
import com.example.activty7.app.AppController;
import com.example.activty7.database.Teman;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TemanAdapter extends RecyclerView.Adapter<TemanAdapter.TemanViewHolder> {
    private ArrayList<Teman> listData;

    public TemanAdapter(ArrayList<Teman> listData){
        this.listData = listData;
    }

    @Override
    public TemanViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.row_data_teman,parent,false);
        return new TemanViewHolder(v);
    }

    @Override
    public void onBindViewHolder(TemanViewHolder holder, int position) {
        String id,nm,tlp;

        id = listData.get(position).getId();
        nm = listData.get(position).getNama();
        tlp = listData.get(position).getTelpon();

        holder.nama.setTextColor(Color.BLUE);
        holder.nama.setTextSize(20);
        holder.nama.setText(nm);
        holder.telp.setText(tlp);

        holder.card.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                PopupMenu pm = new PopupMenu(v.getContext(), v);
                pm.inflate(R.menu.popup1);
                pm.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.edit:
                                Bundle b = new Bundle();
                                b.putString("id",id);
                                b.putString("nm",nm);
                                b.putString("tlp",tlp);

                                Intent i = new Intent(v.getContext(), EditTeman.class);
                                i.putExtras(b);
                                v.getContext().startActivity(i);
                                break;

                            case R.id.hapus:
                                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                                alert.setTitle("Yakin "+nm+" akan dihapus?");
                                alert.setMessage("Tekan Ya untuk menghapus");
                                alert.setCancelable(false);
                                alert.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        deleteData(id);
                                        Toast.makeText(v.getContext(),"Data "+id+" telah dihapus",Toast.LENGTH_LONG).show();
                                        Intent i = new Intent(v.getContext(), MainActivity.class);
                                        v.getContext().startActivity(i);
                                    }
                                });
                                alert.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                                AlertDialog ad = alert.create();
                                ad.show();
                                break;
                        }
                        return true;
                    }
                });
                pm.show();
                return true;
            }
        });
    }

    private void deleteData(final String idx){
        String url_delete = "http://10.0.2.2:8080/umyTI/hapusteman.php";
        final String TAG = MainActivity.class.getSimpleName();
        final String TAG_SUCCESS = "success";
        final int[] success = new int[1];

        StringRequest sr = new StringRequest(Request.Method.POST, url_delete, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Respond : " + response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    success[0] = obj.getInt(TAG_SUCCESS);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError err){
                Log.e(TAG,"Error : "+err.getMessage());
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<>();
                params.put("id",idx);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(sr);
    }

    @Override
    public int getItemCount() {
        return listData != null ? listData.size() : 0;
    }

    public class TemanViewHolder extends RecyclerView.ViewHolder {
        private CardView card;
        private TextView nama,telp;
        public TemanViewHolder(View v) {
            super(v);
            card = v.findViewById(R.id.card);
            nama = v.findViewById(R.id.textNama);
            telp = v.findViewById(R.id.textTelp);
        }
    }
}
